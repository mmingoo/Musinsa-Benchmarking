package com.avengers.musinsa.category.service;

import com.avengers.musinsa.domain.category.service.CategoryServiceImpl;
import com.avengers.musinsa.domain.category.service.UseCacheCategoryTestServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CategoryBrandApiByCacheTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    UseCacheCategoryTestServiceImpl useCacheCategoryTestService;

    @Autowired
    private CacheManager cacheManager;

    private static final int THREAD_COUNT = 1000;
    private static final int WARMUP_COUNT = 10; // 워밍업 횟수


    @Test
    @DisplayName("동시 1000명이 카테고리(7개) 조회할 때 - 캐시 적용 전후 성능 비교")
    void compareCategoryConcurrentPerformance() throws InterruptedException {

        System.out.println("==========================================");
        System.out.println("동시 1000명 카테고리 조회 성능 테스트");
        System.out.println("==========================================");

        // 1. 캐시 없이 원래 방식을 카테고리를 조회하는 경우
        clearCache();
        long noCacheTime = NotUseCacheMeasureCategoryTime("캐시 사용 안함");

        // 2. 캐시 워밍업 (캐시 채우기)
        warmupCategoryCache();

        // 3. 캐시 있을 때 동시 실행
        long withCacheTime = UseCacheMeasureCategoryTime("캐시 사용함");

        // 4. 결과 출력
        printResults(noCacheTime, withCacheTime, "카테고리 조회");

        // 5. 검증
        assertThat(withCacheTime).isLessThan(noCacheTime);
    }

    @Test
    @DisplayName("동시 1000명이 카테고리별(10개) 브랜드 조회할 때 - 캐시 적용 전후 성능 비교")
    void compareBrandByCategoryConcurrentPerformance() throws InterruptedException {

        System.out.println("==========================================");
        System.out.println("동시 1000명 카테고리 조회 성능 테스트");
        System.out.println("==========================================");

        // 1. 캐시 없이 원래 방식을 카테고리를 조회하는 경우
        clearCache();
        long noCacheTime = NotUseCacheMeasureBrandyCategoryTime("카테고리 조회 테스트");

        // 2. 캐시 워밍업 (캐시 채우기)
        warmupBradCache();

        // 3. 캐시 있을 때 동시 실행
        long withCacheTime = UseCacheMeasureBrandyCategoryTime("캐시 사용함");

        // 4. 결과 출력
        printResults(noCacheTime, withCacheTime, "브랜드 조회");

        // 5. 검증
        assertThat(withCacheTime).isLessThan(noCacheTime);
    }













/// ################### 테스트 실행 메소드 ###############################
    /**
     * 캐시 사용할 경우 카테고리별 브랜드 조회 시간 측정 (7개 대분류)
     */
    private long UseCacheMeasureBrandyCategoryTime(String testName) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        // 1000개 작업 제출
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // 동시 출발 대기

                    // 각 스레드가 카테고리 1~7번 조회
                    for (long categoryId = 1; categoryId <= 7; categoryId++) {
                        useCacheCategoryTestService.getBrandsByCategoryId(categoryId);
                    }

                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
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

        long duration = endTime - startTime;
        System.out.println("[" + testName + "] 1000명 동시 실행 시간: " + duration + "ms");

        return duration;
    }
    /**
     * 캐시 사용하지 않을 경우 카테고리별 브랜드 조회 시간 측정 (7개 대분류)
     */
    private long NotUseCacheMeasureBrandyCategoryTime(String testName) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        // 1000개 작업 제출
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // 동시 출발 대기

                    // 각 스레드가 카테고리 1~7번 조회
                    for (long categoryId = 1; categoryId <= 7; categoryId++) {
                        categoryService.getBrandsByCategoryId(categoryId);
                    }

                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
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

        long duration = endTime - startTime;
        System.out.println("[" + testName + "] 1000명 동시 실행 시간: " + duration + "ms");

        return duration;
    }















    /**
     * 캐시 사용할 경우 카테고리 조회 시간 측정 (7개 대분류)
     */
    private long UseCacheMeasureCategoryTime(String testName) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        // 1000개 작업 제출
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // 동시 출발 대기

                    // 각 스레드가 카테고리 1~7번 조회
                    for (long categoryId = 1; categoryId <= 7; categoryId++) {
                        useCacheCategoryTestService.getCategoryProductList(categoryId);
                    }

                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
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

        long duration = endTime - startTime;
        System.out.println("[" + testName + "] 1000명 동시 실행 시간: " + duration + "ms");

        return duration;
    }



    /**
     * 캐시 사용하지 않을 경우(기존 방식)카테고리 조회 시간 측정 (7개 대분류) - 1000 명 동시 조회
     */
    private long NotUseCacheMeasureCategoryTime(String testName) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        // 1000개 작업 제출
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // 동시 출발 대기

                    // 각 스레드가 카테고리 1~7번 조회
                    for (long categoryId = 1; categoryId <= 7; categoryId++) {
                        categoryService.getCategoryProductList(categoryId);
                    }

                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
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

        long duration = endTime - startTime;
        System.out.println("[" + testName + "] 1000명 동시 실행 시간: " + duration + "ms");

        return duration;
    }










    /// ################### 부가적인 메소드 ###############################3


    /**
     * 카테고리 캐시 워밍업 (캐시에 데이터 미리 채우기)
     */
    private void warmupCategoryCache() {
        for (long categoryId = 1; categoryId <= 7; categoryId++) {
            useCacheCategoryTestService.getCategoryProductList(categoryId);
        }
    }

    /**
     * 브랜드 캐시 워밍업 (캐시에 데이터 미리 채우기)
     */
    private void warmupBradCache() {
        for (long categoryId = 1; categoryId <= 7; categoryId++) {
            useCacheCategoryTestService.getBrandsByCategoryId(categoryId);
        }
    }

    /**
     * 캐시 초기화
     */
    private void clearCache() {
        if (cacheManager != null) {
            cacheManager.getCacheNames().forEach(cacheName -> {
                var cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                }
            });
            System.out.println("[캐시 초기화] 모든 캐시 삭제 완료\n");
        }
    }

    /**
    * 결과 출력
     */
    private void printResults(long noCacheTime, long withCacheTime,String testName) {
        long improvement = noCacheTime - withCacheTime;
        double improvementPercent = ((double) improvement / noCacheTime) * 100;

        System.out.println("\n==========================================");
        System.out.println(testName+" 성능 비교 결과");
        System.out.println("==========================================");
        System.out.println("캐시 없는 경우: " + noCacheTime + "ms");
        System.out.println("캐시 있는 경우: " + withCacheTime + "ms");
        System.out.println("------------------------------------------");
        System.out.println("개선 시간: " + improvement + "ms");
        System.out.println("개선율: " + String.format("%.2f", improvementPercent) + "%");
        System.out.println("==========================================\n");
    }
}
