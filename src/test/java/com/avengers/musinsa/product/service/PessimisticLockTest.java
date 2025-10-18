package com.avengers.musinsa.product.service;

import com.avengers.musinsa.domain.product.service.ProductService;
import com.avengers.musinsa.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class PessimisticLockTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    private static final int THREAD_COUNT = 1000;
    private static final Long TEST_PRODUCT_ID = 1L;

    @Test
    @DisplayName("비관적 락 성능 비교")
    void compareAtomicUpdateAndPessimisticLock() throws InterruptedException {

        System.out.println("\n" + "=".repeat(80));
        System.out.println("원자적 UPDATE vs 비관적 락 성능 비교 테스트");
        System.out.println("동시 요청 수: " + THREAD_COUNT + "명");
        System.out.println("=".repeat(80) + "\n");

//        // 1. 원자적 UPDATE 방식 테스트
//        long atomicTime = testAtomicUpdate();
//
//        Thread.sleep(1000);  // 잠시 대기

        // 2. 비관적 락 방식 테스트
        long pessimisticTime = testPessimisticLock();

        // 3. 결과 비교
//        printComparison(atomicTime, pessimisticTime);
    }

    private long testAtomicUpdate() throws InterruptedException {
        System.out.println("[1] 원자적 UPDATE 방식 테스트 시작...");

        // ✅ 초기화 추가!
        productMapper.resetProductLikeCnt(TEST_PRODUCT_ID);
        productMapper.deleteAllUserProductLikes(TEST_PRODUCT_ID);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (long userId = 1; userId <= threadCount; userId++) {
            long finalUserId = userId;
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    productService.ProductLikeToggle(finalUserId, TEST_PRODUCT_ID);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        doneLatch.await();
        long endTime = System.currentTimeMillis();

        executorService.shutdown();

        long elapsedTime = endTime - startTime;

        System.out.println("비관적 락:      " + elapsedTime + " ms");
        // ✅ 결과 확인 추가
        Long finalLikes = productMapper.getProductLikeCnt(TEST_PRODUCT_ID);
        System.out.println("최종 좋아요 수: " + finalLikes + " / " + threadCount);



        return elapsedTime;
    }

    private long testPessimisticLock() throws InterruptedException {
        System.out.println("[2] 비관적 락 방식 테스트 시작...");

        // ✅ 초기화 추가!
        productMapper.resetProductLikeCnt(TEST_PRODUCT_ID);
        productMapper.deleteAllUserProductLikes(TEST_PRODUCT_ID);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (long userId = 1; userId <= threadCount; userId++) {
            long finalUserId = userId;
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    productService.ProductLikeToggleByLock(finalUserId, TEST_PRODUCT_ID);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        doneLatch.await();
        long endTime = System.currentTimeMillis();

        executorService.shutdown();

        long elapsedTime = endTime - startTime;


        System.out.println("비관적 락:      " + elapsedTime + " ms");
        // ✅ 결과 확인 추가
        Long finalLikes = productMapper.getProductLikeCnt(TEST_PRODUCT_ID);
        System.out.println("최종 좋아요 수: " + finalLikes + " / " + threadCount);

        return elapsedTime;
    }

    private void printComparison(long atomicTime, long pessimisticTime) {
        double improvement = ((double)(pessimisticTime - atomicTime) / pessimisticTime) * 100;  

        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("최종 비교 결과");
        System.out.println("=".repeat(80));
        System.out.println("원자적 UPDATE:  " + atomicTime + " ms");
        System.out.println("비관적 락:      " + pessimisticTime + " ms");
        System.out.println("\n차이:           " + (pessimisticTime - atomicTime) + " ms");
        System.out.println("개선율:         " + String.format("%.1f", improvement) + "%");
        System.out.println("=".repeat(80) + "\n");
    }
}