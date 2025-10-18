package com.avengers.musinsa.product.service;

import com.avengers.musinsa.domain.product.service.ProductService;
import com.avengers.musinsa.mapper.ProductMapper;
import com.avengers.musinsa.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductLikeConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    private static final Long TEST_PRODUCT_ID = 1L;
    private static final int THREAD_COUNT = 1000;

    @BeforeEach
    void setUp() {
        // 테스트용 상품의 좋아요 수 초기화
        productMapper.resetProductLikeCnt(TEST_PRODUCT_ID);
        productMapper.deleteAllUserProductLikes(TEST_PRODUCT_ID);

        // 테스트용 사용자들 생성
        for (long i = 1; i <= THREAD_COUNT; i++) {
            if (!userMapper.existsById(i)) {
                userMapper.insertTestUser(i, "TestUser" + i, "TestNick" + i, "KAKAO");
            }
        }
    }

    @Test
    @DisplayName("동시에 1000명이 좋아요를 누르기 테스트")
    void testConcurrentLikesLostUpdate() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        Long initialLikeCount = productMapper.getProductLikeCnt(TEST_PRODUCT_ID);

        // when - 1000명이 정확히 동시에 좋아요 클릭
        for (long userId = 1; userId <= THREAD_COUNT; userId++) {
            long finalUserId = userId;
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // 모든 스레드가 여기서 대기

                    // 실제 좋아요 토글 실행
                    productService.ProductLikeToggleByRMW(finalUserId, TEST_PRODUCT_ID);

                } catch (Exception e) {
                    System.err.println("Error for userId " + finalUserId + ": " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await(); // 모든 스레드 준비 완료 대기
        startLatch.countDown(); // 모든 스레드 동시 시작
        doneLatch.await(); // 모든 작업 완료 대기

        executorService.shutdown();

        // then - 결과 확인
        Long actualLikeCount = productMapper.getProductLikeCnt(TEST_PRODUCT_ID);
        long expectedLikeCount = initialLikeCount + THREAD_COUNT;
        long lostUpdates = expectedLikeCount - actualLikeCount;

        // user_product_likes 테이블의 실제 레코드 수 확인
        int actualUserLikeRecords = productMapper.countUserProductLikes(TEST_PRODUCT_ID);

        System.out.println("============================================");
        System.out.println("테스트 결과 - Lost Update 발생 여부");
        System.out.println("============================================");
        System.out.println("   - user_product_likes 테이블 레코드 수: " + actualUserLikeRecords);
        System.out.println("   - products.product_likes 실제 값: " + actualLikeCount);
        System.out.println("   - products.product_likes 예상 값: " + expectedLikeCount);
        System.out.println("   - 손실된 업데이트 수: " + lostUpdates);

        // 검증
        assertThat(actualUserLikeRecords).isEqualTo(THREAD_COUNT); // 중간 테이블은 정상
        assertThat(actualLikeCount).isLessThan(expectedLikeCount); // product_likes는 Lost Update 발생
    }



    @Test
    @DisplayName("Lost Update 상세 분석 - 중간 테이블과 카운터 불일치 확인")
    void testDetailedLostUpdateAnalysis() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        Long initialCount = productMapper.getProductLikeCnt(TEST_PRODUCT_ID);



        // when
        for (long userId = 1; userId <= threadCount; userId++) {
            long finalUserId = userId;
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    productService.ProductLikeToggleByRMW(finalUserId, TEST_PRODUCT_ID);
                    successCount.incrementAndGet();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();

        // then
        Long actualCount = productMapper.getProductLikeCnt(TEST_PRODUCT_ID);
        int userLikeRecords = productMapper.countUserProductLikes(TEST_PRODUCT_ID);
        long expectedCount = initialCount + threadCount;
        long lostUpdates = expectedCount - actualCount;

        System.out.println("\n========================================");
        System.out.println("Lost Update 상세 분석");
        System.out.println("========================================");
        System.out.println("초기 product_likes: " + initialCount);
        System.out.println("\n[데이터베이스 상태]");
        System.out.println("1. user_product_likes 테이블:");
        System.out.println("   - 삽입된 레코드 수: " + userLikeRecords);
        System.out.println("   - 각 사용자의 좋아요 이력은 정확히 기록됨");

        System.out.println("\n2. products 테이블:");
        System.out.println("   - 예상 product_likes: " + expectedCount);
        System.out.println("   - 실제 product_likes: " + actualCount);
        System.out.println("   - Lost Update 발생: " + lostUpdates + "건");


        assertThat(userLikeRecords).isEqualTo(threadCount);
        assertThat(actualCount).isLessThan(expectedCount);
    }


}