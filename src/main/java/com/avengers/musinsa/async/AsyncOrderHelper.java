package com.avengers.musinsa.async;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import com.avengers.musinsa.domain.order.service.OrderCompensationService;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class AsyncOrderHelper {

    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final OrderCompensationService compensationService; // 추가

    public AsyncOrderHelper(
            ProductVariantRepository productVariantRepository,
            OrderRepository orderRepository,
            OrderCompensationService compensationService,
            @Qualifier("orderTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.productVariantRepository = productVariantRepository;
        this.orderRepository = orderRepository;
        this.compensationService = compensationService;
        this.taskExecutor = taskExecutor;
    }

    /**
     * 비동기: 배치 OrderItems 저장 (재시도 적용)
     */
    @Async("orderTaskExecutor")
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public CompletableFuture<Void> asyncBatchSaveOrderItems(
            Long orderId,
            List<OrderCreateRequest.ProductLine> orderProducts,
            Long couponId) {

        long start = System.nanoTime();
        String threadName = Thread.currentThread().getName();

        try {
            log.debug("[{}] asyncBatchSaveOrderItems 시작", threadName);

            // 배치 INSERT
            orderRepository.batchCreateOrderItems(orderId, orderProducts, couponId);

            long end = System.nanoTime();
            log.debug("[{}] asyncBatchSaveOrderItems 완료 - 실행시간: {}μs",
                    threadName, (end - start) / 1000);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("[{}] OrderItems 저장 실패 - orderId: {}, 재시도 중...",
                    threadName, orderId, e);
            throw e; // 재시도를 위해 예외 전파
        }
    }

    /**
     * 재시도 실패 시 보상 처리
     */
    @Recover
    public CompletableFuture<Void> recoverBatchSaveOrderItems(
            Exception e,
            Long orderId,
            List<OrderCreateRequest.ProductLine> orderProducts,
            Long couponId) {

        log.error("[RECOVERY] OrderItems 저장 최종 실패 - orderId: {}", orderId, e);

        // 보상 트랜잭션: 주문 취소 또는 실패 상태로 변경
        compensationService.handleOrderItemsFailure(orderId, orderProducts, e);

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 비동기: 배치 재고 감소 (재시도 적용)
     */
    @Async("orderTaskExecutor")
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public CompletableFuture<Void> asyncBatchDecrementStock(
            List<OrderCreateRequest.ProductLine> orderProducts) {

        long start = System.nanoTime();
        String threadName = Thread.currentThread().getName();

        try {
            log.debug("[{}] asyncBatchDecrementStock 시작", threadName);

            // 배치 UPDATE
            productVariantRepository.batchDecrementStock(orderProducts);

            long end = System.nanoTime();
            log.debug("[{}] asyncBatchDecrementStock 완료 - 실행시간: {}μs",
                    threadName, (end - start) / 1000);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("[{}] 재고 감소 실패 - 재시도 중...", threadName, e);
            throw e; // 재시도를 위해 예외 전파
        }
    }

    /**
     * 재시도 실패 시 보상 처리
     */
    @Recover
    public CompletableFuture<Void> recoverBatchDecrementStock(
            Exception e,
            List<OrderCreateRequest.ProductLine> orderProducts) {

        log.error("[RECOVERY] 재고 감소 최종 실패", e);

        // 보상 트랜잭션: 관리자 알림 + 수동 처리 큐에 추가
        compensationService.handleStockFailure(orderProducts, e);

        return CompletableFuture.completedFuture(null);
    }


}