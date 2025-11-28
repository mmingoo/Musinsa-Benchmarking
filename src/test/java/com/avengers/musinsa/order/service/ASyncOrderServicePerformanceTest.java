package com.avengers.musinsa.order.service;

import com.avengers.musinsa.async.AsyncOrderHelper;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest.ProductLine;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@SpringBootTest
class ASyncOrderServicePerformanceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private AsyncOrderHelper asyncOrderHelper;

    @Test
    @DisplayName("배치 처리: 동기 방식 vs 비동기 방식 성능 비교 + 스레드 오버헤드 측정")
    @Transactional(propagation = REQUIRES_NEW)
    void compareSyncVsAsyncBatchProcessing() throws Exception {
        int iterations = 1000;

        // ========== 초기 스레드 풀 상태 저장 ==========
        System.out.println("\n========== 테스트 시작 전 스레드 풀 상태 ==========");
        Map<String, Object> initialStats = asyncOrderHelper.getThreadPoolStats();
        asyncOrderHelper.logThreadPoolStats("초기 상태");

        // ========== 1단계: 동기 방식 (배치 처리) ==========
        System.out.println("\n========== [1단계] 동기 방식 - 배치 처리 ==========");
        System.out.println("주문 처리 방식: OrderItems 저장 → 재고 감소 (순차 실행)");

        long syncTotalTime = 0;
        long syncOrderItemsTime = 0;
        long syncStockTime = 0;

        for (int i = 0; i < iterations; i++) {
            Long userId = 1L;
            OrderCreateRequest request = createCartOrderRequest(userId);
            setVariantInfo(request);

            long iterationStart = System.nanoTime();

            // Shipment, Order 생성
            Long shippingId = orderRepository.createShipment(request);
            orderRepository.createOrder(userId, shippingId, request.getPayment());
            Long orderId = request.getPayment().getOrderId();

            // 동기 1: 배치 OrderItems 저장
            long orderItemsStart = System.nanoTime();
            orderRepository.batchCreateOrderItems(orderId, request.getProduct(), request.getCouponId());
            long orderItemsEnd = System.nanoTime();
            syncOrderItemsTime += (orderItemsEnd - orderItemsStart);

            // 동기 2: 배치 재고 감소
            long stockStart = System.nanoTime();
            productVariantRepository.batchDecrementStock(request.getProduct());
            long stockEnd = System.nanoTime();
            syncStockTime += (stockEnd - stockStart);

            long iterationEnd = System.nanoTime();
            syncTotalTime += (iterationEnd - iterationStart);

            // 100회마다 진행상황 출력
            if ((i + 1) % 100 == 0) {
                System.out.println("동기 처리 진행: " + (i + 1) + "/" + iterations);
            }
        }

        // 동기 방식 결과
        long syncTotalMs = syncTotalTime / 1_000_000;
        long syncOrderItemsMs = syncOrderItemsTime / 1_000_000;
        long syncStockMs = syncStockTime / 1_000_000;

        System.out.println("\n[동기 방식 결과]");
        System.out.println("전체 처리 시간: " + syncTotalMs + "ms");
        System.out.println("  - OrderItems 저장 시간: " + syncOrderItemsMs + "ms");
        System.out.println("  - 재고 감소 시간: " + syncStockMs + "ms");
        System.out.println("평균 주문 처리 시간: " + String.format("%.2f", (double) syncTotalMs / iterations) + "ms");

        // 동기 처리 후 스레드 풀 상태
        System.out.println("\n========== 동기 처리 완료 후 스레드 풀 상태 ==========");
        Map<String, Object> afterSyncStats = asyncOrderHelper.getThreadPoolStats();
        asyncOrderHelper.logThreadPoolStats("동기 처리 후");

        // ========== 2단계: 비동기 방식 (배치 처리) ==========
        System.out.println("\n========== [2단계] 비동기 방식 - 배치 처리 ==========");
        System.out.println("주문 처리 방식: OrderItems 저장 || 재고 감소 (병렬 실행)");

        long asyncTotalTime = 0;

        for (int i = 0; i < iterations; i++) {
            Long userId = 1L;
            OrderCreateRequest request = createCartOrderRequest(userId);
            setVariantInfo(request);

            long iterationStart = System.nanoTime();

            // Shipment, Order 생성
            Long shippingId = orderRepository.createShipment(request);
            orderRepository.createOrder(userId, shippingId, request.getPayment());
            Long orderId = request.getPayment().getOrderId();

            // 비동기: 배치 OrderItems 저장 + 배치 재고 감소 (병렬 실행)
            CompletableFuture<Void> orderItemsFuture = asyncOrderHelper.asyncBatchSaveOrderItems(
                    orderId, request.getProduct(), request.getCouponId());

            CompletableFuture<Void> stockFuture = asyncOrderHelper.asyncBatchDecrementStock(
                    request.getProduct());

            // 두 작업이 모두 완료될 때까지 대기
            CompletableFuture.allOf(orderItemsFuture, stockFuture).join();

            long iterationEnd = System.nanoTime();
            asyncTotalTime += (iterationEnd - iterationStart);

            // 100회마다 진행상황과 스레드 풀 상태 출력
            if ((i + 1) % 100 == 0) {
                System.out.println("비동기 처리 진행: " + (i + 1) + "/" + iterations);
                asyncOrderHelper.logThreadPoolStats("비동기 " + (i + 1) + "회");
            }
        }

        // 모든 비동기 작업 완료 대기
        Thread.sleep(1000);

        // 비동기 방식 결과
        long asyncTotalMs = asyncTotalTime / 1_000_000;

        System.out.println("\n[비동기 방식 결과]");
        System.out.println("전체 처리 시간: " + asyncTotalMs + "ms");
        System.out.println("평균 주문 처리 시간: " + String.format("%.2f", (double) asyncTotalMs / iterations) + "ms");

        // 비동기 처리 후 최종 스레드 풀 상태
        System.out.println("\n========== 비동기 처리 완료 후 스레드 풀 상태 ==========");
        Map<String, Object> finalStats = asyncOrderHelper.getThreadPoolStats();
        asyncOrderHelper.logThreadPoolStats("최종 상태");

        // ========== 3단계: 결과 분석 ==========
        printPerformanceComparison(syncTotalMs, asyncTotalMs, syncOrderItemsMs, syncStockMs, iterations);
        printThreadOverheadAnalysis(initialStats, afterSyncStats, finalStats, iterations,
                syncTotalMs, asyncTotalMs);
    }

    private void setVariantInfo(OrderCreateRequest request) {
        // 배치 조회
        List<ProductVariantDto> variants = productVariantRepository.findProductVariantsByOptionNames(
                request.getProduct());

        Map<String, ProductVariantDto> variantMap = new HashMap<>();
        for (ProductVariantDto variant : variants) {
            String key = variant.getProductId() + "_" + variant.getVariantName();
            variantMap.put(key, variant);
        }

        for (ProductLine productLine : request.getProduct()) {
            String key = productLine.getProductId() + "_" + productLine.getOptionName();
            ProductVariantDto variant = variantMap.get(key);
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

    /**
     * 성능 비교 분석
     */
    private void printPerformanceComparison(long syncTime, long asyncTime,
                                            long syncOrderItemsTime, long syncStockTime,
                                            int iterations) {
        System.out.println("\n========== 동기 vs 비동기 성능 비교 ==========");
        System.out.println("테스트 반복 횟수: " + iterations + "회");
        System.out.println("주문당 상품 개수: 10개\n");

        System.out.println("[전체 처리 시간 비교]");
        System.out.println("동기 방식:   " + syncTime + "ms");
        System.out.println("비동기 방식: " + asyncTime + "ms");
        System.out.println("시간 단축:   " + (syncTime - asyncTime) + "ms");

        double improvement = ((double) (syncTime - asyncTime) / syncTime) * 100;
        System.out.println("개선율:      " + String.format("%.2f", improvement) + "%");

        System.out.println("\n[평균 주문 처리 시간]");
        System.out.println("동기 방식:   " + String.format("%.2f", (double) syncTime / iterations) + "ms");
        System.out.println("비동기 방식: " + String.format("%.2f", (double) asyncTime / iterations) + "ms");

        System.out.println("\n[동기 방식 세부 시간]");
        System.out.println("OrderItems 저장: " + syncOrderItemsTime + "ms (" +
                String.format("%.1f", (double) syncOrderItemsTime / syncTime * 100) + "%)");
        System.out.println("재고 감소:       " + syncStockTime + "ms (" +
                String.format("%.1f", (double) syncStockTime / syncTime * 100) + "%)");

        System.out.println("\n[개선율 분석]");
        if (improvement < 5) {
            System.out.println("⚠️  개선율이 " + String.format("%.2f", improvement) + "%로 낮습니다.");
            System.out.println("원인:");
            System.out.println("  - 각 배치 작업의 실행 시간: OrderItems " +
                    String.format("%.2f", (double) syncOrderItemsTime / iterations) + "ms, " +
                    "재고 감소 " + String.format("%.2f", (double) syncStockTime / iterations) + "ms");
            System.out.println("  - 작업 시간이 짧아 비동기 오버헤드가 상대적으로 큼");
            System.out.println("  - 스레드 생성/관리, 컨텍스트 스위칭, CompletableFuture 대기 비용 발생");
        } else {
            System.out.println("✅ 비동기 처리로 " + String.format("%.2f", improvement) + "% 개선되었습니다.");
        }
        System.out.println("=============================================\n");
    }

    /**
     * 스레드 오버헤드 분석
     */
    private void printThreadOverheadAnalysis(Map<String, Object> initialStats,
                                             Map<String, Object> afterSyncStats,
                                             Map<String, Object> finalStats,
                                             int iterations,
                                             long syncTime,
                                             long asyncTime) {
        System.out.println("\n========== 스레드 오버헤드 분석 ==========");

        long syncTaskCount = (long) afterSyncStats.get("completedTaskCount") -
                (long) initialStats.get("completedTaskCount");
        long asyncTaskCount = (long) finalStats.get("completedTaskCount") -
                (long) afterSyncStats.get("completedTaskCount");

        System.out.println("[동기 처리]");
        System.out.println("비동기 작업 생성 수: " + syncTaskCount + "개");
        System.out.println("(동기 방식은 비동기 작업을 생성하지 않음)");

        System.out.println("\n[비동기 처리]");
        System.out.println("실행된 비동기 작업 수: " + asyncTaskCount + "개");
        System.out.println("예상 작업 수: " + (iterations * 2) + "개 (OrderItems + 재고감소)");
        System.out.println("최대 동시 실행 스레드: " + finalStats.get("largestPoolSize") + "개");
        System.out.println("스레드 풀 크기: " + finalStats.get("currentPoolSize") + "개");

        System.out.println("\n[오버헤드 계산]");
        long timeDiff = asyncTime - syncTime;
        System.out.println("비동기 추가 소요 시간: " + Math.abs(timeDiff) + "ms");

        if (asyncTaskCount > 0) {
            double overheadPerTask = (double) Math.abs(timeDiff) / asyncTaskCount;
            System.out.println("작업당 평균 오버헤드: " + String.format("%.2f", overheadPerTask) + "ms");
            System.out.println("총 오버헤드 비율: " +
                    String.format("%.2f", (double) Math.abs(timeDiff) / asyncTime * 100) + "%");
        }

        System.out.println("\n[오버헤드 구성 요소]");
        System.out.println("1. 스레드 생성 및 할당 비용");
        System.out.println("2. 컨텍스트 스위칭 비용");
        System.out.println("3. CompletableFuture 생성 및 관리 비용");
        System.out.println("4. allOf().join() 대기 비용");
        System.out.println("5. 스레드 풀 큐 관리 비용");

        System.out.println("\n[결론]");
        if (timeDiff > 0) {
            System.out.println("⚠️  비동기 처리가 오히려 " + timeDiff + "ms 더 느립니다.");
            System.out.println("→ 작업 실행 시간이 짧아 오버헤드가 실제 작업 시간보다 큼");
        } else {
            System.out.println("✅ 비동기 처리가 " + Math.abs(timeDiff) + "ms 빠릅니다.");
            System.out.println("→ 오버헤드보다 병렬 처리 이득이 더 큼");
        }
        System.out.println("=============================================\n");
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