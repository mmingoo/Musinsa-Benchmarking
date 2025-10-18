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
class OrderServicePerformanceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private AsyncOrderHelper asyncOrderHelper;

    @Test
    @DisplayName("개별 UPDATE vs 배치 UPDATE 성능 비교")
    @Rollback
    @Transactional
    void compareSyncVsAsyncBatch() {
        int iterations = 1000;

        //========== 1단계: 재고 감소만 순수 비교 ==========
        System.out.println("\n========== [1단계] 재고 감소 순수 비교 ==========");

        long individualStockTime = 0;
        long batchStockTime = 0;

        // 개별 UPDATE 방식
        for (int i = 0; i < iterations; i++) {
            OrderCreateRequest request = createCartOrderRequest(1L);
            setVariantInfo(request);

            long start = System.currentTimeMillis();
            for (ProductLine product : request.getProduct()) {
                productVariantRepository.decrementStock(product.getVariantId(), product.getQuantity());
            }
            long end = System.currentTimeMillis();
            individualStockTime += (end - start);
        }

        // 배치 UPDATE 방식
        for (int i = 0; i < iterations; i++) {
            OrderCreateRequest request = createCartOrderRequest(1L);
            setVariantInfo(request);

            long start = System.currentTimeMillis();
            productVariantRepository.batchDecrementStock(request.getProduct());
            long end = System.currentTimeMillis();
            batchStockTime += (end - start);
        }

        System.out.println("개별 UPDATE 재고 감소 시간: " + individualStockTime + "ms");
        System.out.println("배치 UPDATE 재고 감소 시간: " + batchStockTime + "ms");
        System.out.println("재고 감소 시간 차이: " + (individualStockTime - batchStockTime) + "ms");
        double stockImprovement = ((double) (individualStockTime - batchStockTime) / individualStockTime) * 100;
        System.out.println("재고 감소 개선율: " + String.format("%.2f", stockImprovement) + "%");

        //========== 2단계: 전체 주문 프로세스 비교 ==========
        System.out.println("\n========== [2단계] 전체 주문 프로세스 비교 ==========");

        long individualTotalTime = 0;

        for (int i = 0; i < iterations; i++) {
            long iterationStart = System.currentTimeMillis();

            Long userId = 1L;
            OrderCreateRequest request = createCartOrderRequest(userId);

            // Variant 조회 및 설정
            setVariantInfo(request);

            // Shipment, Order 생성
            Long shippingId = orderRepository.createShipment(request);
            orderRepository.createOrder(userId, shippingId, request.getPayment());
            Long orderId = request.getPayment().getOrderId();

            // 배치 INSERT - OrderItems 저장
            orderRepository.batchCreateOrderItems(orderId, request.getProduct(), request.getCouponId());

            // 개별 UPDATE - 재고 감소
            for (ProductLine product : request.getProduct()) {
                productVariantRepository.decrementStock(product.getVariantId(), product.getQuantity());
            }

            long iterationEnd = System.currentTimeMillis();
            individualTotalTime += (iterationEnd - iterationStart);
        }

        long batchTotalTime = 0;

        for (int i = 0; i < iterations; i++) {
            long iterationStart = System.currentTimeMillis();

            Long userId = 1L;
            OrderCreateRequest request = createCartOrderRequest(userId);

            // Variant 조회 및 설정
            setVariantInfo(request);

            // Shipment, Order 생성
            Long shippingId = orderRepository.createShipment(request);
            orderRepository.createOrder(userId, shippingId, request.getPayment());
            Long orderId = request.getPayment().getOrderId();

            // 배치 INSERT - OrderItems 저장
            orderRepository.batchCreateOrderItems(orderId, request.getProduct(), request.getCouponId());

            // 배치 UPDATE - 재고 감소
            productVariantRepository.batchDecrementStock(request.getProduct());

            long iterationEnd = System.currentTimeMillis();
            batchTotalTime += (iterationEnd - iterationStart);
        }

        System.out.println("개별 UPDATE 전체 주문 시간: " + individualTotalTime + "ms");
        System.out.println("배치 UPDATE 전체 주문 시간: " + batchTotalTime + "ms");
        System.out.println("전체 주문 시간 차이: " + (individualTotalTime - batchTotalTime) + "ms");
        double totalImprovement = ((double) (individualTotalTime - batchTotalTime) / individualTotalTime) * 100;
        System.out.println("전체 주문 개선율: " + String.format("%.2f", totalImprovement) + "%");

        //========== 최종 결과 출력 ==========
        printFinalResults(iterations, individualStockTime, batchStockTime, individualTotalTime, batchTotalTime);
    }
    private void setVariantInfo(OrderCreateRequest request) {
        for (ProductLine productLine : request.getProduct()) {
            ProductVariantDto variant = productVariantService.findProductVariantByOptionName(
                    productLine.getProductId(),
                    productLine.getOptionName()
            );
            if (variant != null) {
                productLine.setVariantId(variant.getProductVariantId());
                productLine.setOrderItemSize(variant.getSizeValue());
                productLine.setColor(variant.getColorValue());
                Map<String, String> options = new HashMap<>();
                options.put("size", variant.getSizeValue());
                options.put("color", variant.getColorValue());
                productLine.setOptions(options);
            }
        }
    }


    private void printFinalResults(int iterations, long individualStockTime, long batchStockTime,
                                   long individualTotalTime, long batchTotalTime) {
        System.out.println("\n========== 최종 성능 비교 결과 ==========");
        System.out.println("반복 횟수: " + iterations + "회, 상품 개수: 10개\n");

        System.out.println("[ 재고 감소만 비교 ]");
        System.out.println("개별 UPDATE: " + individualStockTime + "ms");
        System.out.println("배치 UPDATE: " + batchStockTime + "ms");
        System.out.println("시간 차이: " + (individualStockTime - batchStockTime) + "ms");
        double stockImprovement = ((double) (individualStockTime - batchStockTime) / individualStockTime) * 100;
        System.out.println("개선율: " + String.format("%.2f", stockImprovement) + "%\n");

        System.out.println("[ 전체 주문 프로세스 비교 ]");
        System.out.println("개별 UPDATE: " + individualTotalTime + "ms");
        System.out.println("배치 UPDATE: " + batchTotalTime + "ms");
        System.out.println("시간 차이: " + (individualTotalTime - batchTotalTime) + "ms");
        double totalImprovement = ((double) (individualTotalTime - batchTotalTime) / individualTotalTime) * 100;
        System.out.println("개선율: " + String.format("%.2f", totalImprovement) + "%");
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
            productLine.setProductDiscountAmount(0);
            products.add(productLine);
        }

        request.setProduct(products);
        return request;
    }
}