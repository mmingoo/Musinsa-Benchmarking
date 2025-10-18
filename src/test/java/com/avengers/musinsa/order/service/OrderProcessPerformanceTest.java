package com.avengers.musinsa.order.service;

import com.avengers.musinsa.async.AsyncOrderHelper;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest.ProductLine;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import com.avengers.musinsa.domain.product.service.ProductVariantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
class OrderProcessPerformanceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private AsyncOrderHelper asyncOrderHelper;

    @Test
    @DisplayName("개선 전 Order 와 개선 후 Order 비교")
    @Rollback
    @Transactional
    void compareIndividualVsBatchSelect() {
        int iterations = 1000;
        
        //========== 개선전 Order 비교 ==========
        System.out.println("\n========== [1단계] 개선 전 주문 프로세스 ==========");

        long beforeStart = 0;
        long beforeEnd = 0;
        long totalBeforeTime = 0;
        long afterStart = 0;
        long afterEnd = 0;
        long totalAfterTime = 0;

        // 1000개 주문
        for (int i = 0; i < iterations; i++) {

            Long userId = 1L;
            OrderCreateRequest request = createCartOrderRequest(userId);

            beforeStart = System.currentTimeMillis();
            // 1. 개별 Variant 조회
            for (ProductLine productLine : request.getProduct()) {
                ProductVariantDto variant = productVariantRepository.findProductVariantByOptionName(
                        productLine.getProductId(),
                        productLine.getOptionName()
                );

                if (variant == null) {
                    throw new IllegalArgumentException("상품 옵션을 찾을 수 없습니다.");
                }

                productLine.setVariantId(variant.getProductVariantId());
                productLine.setOrderItemSize(variant.getSizeValue());
                productLine.setColor(variant.getColorValue());

                Map<String, String> options = new HashMap<>();
                options.put("size", variant.getSizeValue());
                options.put("color", variant.getColorValue());
                productLine.setOptions(options);
            }

            // 2. Shipment, Order 생성
            Long shippingId = orderRepository.createShipment(request);
            orderRepository.createOrder(userId, shippingId, request.getPayment());
            Long orderId = request.getPayment().getOrderId();

            // 3. 개별 orderItem 생성 및 개별 재고  감소
            for (ProductLine orderProduct : request.getProduct()) {
                orderRepository.createOrderItems(orderId, orderProduct, request.getCouponId());

                // 재고 감소
                productVariantRepository.decrementStock(orderProduct.getVariantId(), orderProduct.getQuantity());
            }

            beforeEnd = System.currentTimeMillis();
            totalBeforeTime += (beforeEnd - beforeStart);
        }



        //========== 개선 후 Order 비교 ==========
        System.out.println("\n========== [2단계] 개선 후 주문 프로세스 ==========");

        // 1000개 주문
        for (int i = 0; i < iterations; i++) {

            Long userId = 1L;
            OrderCreateRequest request = createCartOrderRequest(userId);

            afterStart = System.currentTimeMillis();

            //1. 배치 variant 조회
            List<ProductVariantDto> variants = productVariantRepository.findProductVariantsByOptionNames(request.getProduct());

            // Map으로 변환
            Map<String, ProductVariantDto> variantMap = new HashMap<>();
            for (ProductVariantDto variant : variants) {
                String key = variant.getProductId() + "_" + variant.getVariantName();
                variantMap.put(key, variant);
            }

            // 각 상품에 설정
            for (ProductLine productLine : request.getProduct()) {
                String key = productLine.getProductId() + "_" + productLine.getOptionName();
                ProductVariantDto variant = variantMap.get(key);
                if (variant != null) {
                    productLine.setVariantId(variant.getProductVariantId());
                }
            }

            // 2. Shipment, Order 생성
            Long shippingId = orderRepository.createShipment(request);
            orderRepository.createOrder(userId, shippingId, request.getPayment());
            Long orderId = request.getPayment().getOrderId();

            // 3. 배치 INSERT - OrderItems 저장
            orderRepository.batchCreateOrderItems(orderId, request.getProduct(), request.getCouponId());

            // 4. 배치 UPDATE - 재고 감소
            productVariantRepository.batchDecrementStock(request.getProduct());
            afterEnd = System.currentTimeMillis();
            totalAfterTime += (afterEnd -afterStart);

        }


        System.out.println("개선 전 전체 주문 시간: " + (totalBeforeTime) + "ms");
        System.out.println("개선 후 전체 주문 시간: " + (totalAfterTime) + "ms");
        double totalImprovement = ((double) (totalBeforeTime-totalAfterTime) / (totalBeforeTime)) * 100;
        System.out.println("전체 주문 개선율: " + String.format("%.2f", totalImprovement) + "%");


        printSelectComparisonResults(iterations,totalBeforeTime, totalAfterTime, totalImprovement);
    }

    private void printSelectComparisonResults(int iterations, long totalBeforeTime, long totalAfterTime,
                                              double totalImprovement) {
        System.out.println("\n========== 최종 성능 비교 결과 ==========");
        System.out.println("반복 횟수: " + iterations + "회, 상품 개수: 10개\n");

        System.out.println("[개선 전후 전체주문 조회 성능 비교]");
        System.out.println("개선 전 전체 주문 시간: " + totalBeforeTime + "ms");
        System.out.println("개선 후 전체 주문 시간: " + totalAfterTime + "ms");
        System.out.println("시간 차이: " + (totalBeforeTime - totalAfterTime) + "ms");
        System.out.println("개선율: " + String.format("%.2f", totalImprovement) + "%\n");

        System.out.println("========================================================\n");
    }

    private OrderCreateRequest createCartOrderRequest(Long userId) {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(userId);
        request.setAddressId(1L);
        request.setCouponId(null);

        OrderCreateRequest.Shipping shipping = new OrderCreateRequest.Shipping();
        shipping.setRecipientName("홍길동");
        shipping.setRecipientPhone("010-1234-5678");
        shipping.setRecipientAddress("서울시 강남구 테헤란로 123");
        shipping.setShippingDirectRequest("문 앞에 놓아주세요");
        shipping.setPostalCode("12345");
        request.setShipping(shipping);

        OrderCreateRequest.Payment payment = new OrderCreateRequest.Payment();
        payment.setTotalAmount(100000);
        payment.setDiscountAmount(0);
        payment.setUserOfReward(0);
        payment.setDeliveryFee(3000);
        payment.setPaymentMethodId(1L);
        request.setPayment(payment);

        List<ProductLine> products = new ArrayList<>();
        List<Long> cartItemIds = Arrays.asList(1L, 3L, 5L, 10L, 11L, 13L, 15L, 16L, 18L, 20L);

        for (Long cartItemId : cartItemIds) {
            ProductLine productLine = new ProductLine();
            productLine.setProductId(cartItemId);
            productLine.setVariantId(3L);
            productLine.setFinalPrice(30000);
            productLine.setOptionName("Red / S");
            productLine.setDiscountRate(0);
            productLine.setQuantity(3);
            productLine.setColor("Red");
            productLine.setProductDiscountAmount(0);
            products.add(productLine);
        }

        request.setProduct(products);
        return request;
    }
}