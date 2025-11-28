package com.avengers.musinsa.async;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class AsyncOrderHelper {

    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final ThreadPoolTaskExecutor taskExecutor;

    public AsyncOrderHelper(
            ProductVariantRepository productVariantRepository,
            OrderRepository orderRepository,
            @Qualifier("orderTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.productVariantRepository = productVariantRepository;
        this.orderRepository = orderRepository;
        this.taskExecutor = taskExecutor;
    }

    /**
     * 비동기: 배치 OrderItems 저장
     */
    @Async("orderTaskExecutor")
    public CompletableFuture<Void> asyncBatchSaveOrderItems(
            Long orderId,
            List<OrderCreateRequest.ProductLine> orderProducts,
            Long couponId) {

        long start = System.nanoTime();
        String threadName = Thread.currentThread().getName();

        log.debug("[{}] asyncBatchSaveOrderItems 시작", threadName);

        // 배치 INSERT
        orderRepository.batchCreateOrderItems(orderId, orderProducts, couponId);

        long end = System.nanoTime();
        log.debug("[{}] asyncBatchSaveOrderItems 완료 - 실행시간: {}μs",
                threadName, (end - start) / 1000);

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 비동기: 배치 재고 감소
     */
    @Async("orderTaskExecutor")
    public CompletableFuture<Void> asyncBatchDecrementStock(
            List<OrderCreateRequest.ProductLine> orderProducts) {

        long start = System.nanoTime();
        String threadName = Thread.currentThread().getName();

        log.debug("[{}] asyncBatchDecrementStock 시작", threadName);

        // 배치 UPDATE
        productVariantRepository.batchDecrementStock(orderProducts);

        long end = System.nanoTime();
        log.debug("[{}] asyncBatchDecrementStock 완료 - 실행시간: {}μs",
                threadName, (end - start) / 1000);

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 스레드 풀 통계 조회
     */
    public Map<String, Object> getThreadPoolStats() {
        ThreadPoolExecutor threadPool = taskExecutor.getThreadPoolExecutor();

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeCount", threadPool.getActiveCount());
        stats.put("corePoolSize", threadPool.getCorePoolSize());
        stats.put("maxPoolSize", threadPool.getMaximumPoolSize());
        stats.put("currentPoolSize", threadPool.getPoolSize());
        stats.put("queueSize", threadPool.getQueue().size());
        stats.put("completedTaskCount", threadPool.getCompletedTaskCount());
        stats.put("totalTaskCount", threadPool.getTaskCount());
        stats.put("largestPoolSize", threadPool.getLargestPoolSize());

        return stats;
    }

    /**
     * 스레드 풀 통계 로깅
     */
    public void logThreadPoolStats(String prefix) {
        Map<String, Object> stats = getThreadPoolStats();
        log.info("{} - Active: {}, Pool: {}, Queue: {}, Completed: {}, Total: {}",
                prefix,
                stats.get("activeCount"),
                stats.get("currentPoolSize"),
                stats.get("queueSize"),
                stats.get("completedTaskCount"),
                stats.get("totalTaskCount"));
    }
}