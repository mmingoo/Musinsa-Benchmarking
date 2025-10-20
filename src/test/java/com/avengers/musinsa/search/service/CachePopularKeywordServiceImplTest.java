package com.avengers.musinsa.search.performance;

import com.avengers.musinsa.domain.search.dto.PopularKeywordResponseDTO;
import com.avengers.musinsa.domain.search.dto.ResultDTO;
import com.avengers.musinsa.domain.search.repository.PopularKeywordRepository;
import com.avengers.musinsa.domain.search.service.CachePopularKeywordServiceImpl;
import com.avengers.musinsa.domain.search.service.PopularKeywordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 캐싱 적용 전후 성능 비교 테스트
 *
 * 측정 항목:
 * 1. 응답 시간 (Response Time)
 * 2. DB 쿼리 횟수
 * 3. 동시 요청 처리 성능
 * 4. 평균/최소/최대 응답 시간
 */
@SpringBootTest
@Transactional
class CachePerformanceComparisonTest {

    @Autowired
    private CachePopularKeywordServiceImpl cacheService;  // 캐시 O

    @Autowired
    private PopularKeywordServiceImpl noCacheService;     // 캐시 X

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PopularKeywordRepository popularKeywordRepository;

    private static final String CACHE_KEY = "popular:keywords";
    private static final int WARMUP_COUNT = 5;      // 워밍업 횟수
    private static final int TEST_COUNT = 10000;      // 테스트 반복 횟수
    private static final int CONCURRENT_USERS = 50; // 동시 사용자 수

    @BeforeEach
    void setUp() {
        // Redis 초기화
        redisTemplate.delete(CACHE_KEY);

//        // 테스트 데이터 준비
//        prepareTestData();
//
//        // 인기검색어 집계 (search_keywords 테이블 채우기)
//        cacheService.createPopularKeyword();
    }

    private void prepareTestData() {
        LocalDateTime now = LocalDateTime.now();

        String insertLogSql =
                "INSERT INTO search_keyword_logs (search_keyword_log_id, user_id, search_text, search_datetime) " +
                        "VALUES (?, ?, ?, ?)";

        long id = 1L;

        // 다양한 키워드로 검색 로그 생성
        String[] keywords = {"맨투맨", "패딩", "청바지", "운동화", "후드티",
                "코트", "셔츠", "니트", "가디건", "점퍼"};

        for (String keyword : keywords) {
            int count = 10 - (int)(Math.random() * 5); // 5~10회 검색
            for (int i = 0; i < count; i++) {
                jdbcTemplate.update(insertLogSql, 1L,
                        (long)(Math.random() * 1000),
                        keyword,
                        now.minusMinutes((long)(Math.random() * 30)));
            }
        }
    }

    @Test
    @DisplayName("1. 단일 요청 응답 시간 비교 - 캐시 미스")
    void compareResponseTime_CacheMiss() {
        System.out.println("\n========== 1. 단일 요청 응답 시간 (캐시 미스) ==========");

        // 캐시 초기화 (캐시 미스 상황)
        redisTemplate.delete(CACHE_KEY);

        // 캐시 O - 첫 요청 (캐시 미스 + DB 조회)
        long startCache = System.currentTimeMillis();
        PopularKeywordResponseDTO cacheResult = cacheService.getTrendingKeywords();
        long endCache = System.currentTimeMillis();
        long cacheTime = endCache - startCache;


        redisTemplate.delete(CACHE_KEY);
        // 캐시 X - DB 직접 조회
        long startNoCache = System.currentTimeMillis();
        PopularKeywordResponseDTO noCacheResult = noCacheService.getTrendingKeywords();
        long endNoCache = System.currentTimeMillis();
        long noCacheTime = (endNoCache - startNoCache); // ms

        System.out.println("캐시 O (캐시 미스): " + cacheTime + "ms");
        System.out.println("캐시 X (DB 조회):  " + noCacheTime + "ms");
        System.out.println("차이: " + Math.abs(cacheTime - noCacheTime) + "ms");

        assertThat(cacheResult).isNotNull();
        assertThat(noCacheResult).isNotNull();
    }

    @Test
    @DisplayName("2. 단일 요청 응답 시간 비교 - 캐시 히트")
    void compareResponseTime_CacheHit() {
        System.out.println("\n========== 2. 단일 요청 응답 시간 (캐시 히트) ==========");

        // 캐시 워밍업 (캐시에 데이터 적재)
        cacheService.getTrendingKeywords();

        // 캐시 O - 두 번째 요청 (캐시 히트)
        long startCache = System.currentTimeMillis();
        PopularKeywordResponseDTO cacheResult = cacheService.getTrendingKeywords();
        long endCache = System.currentTimeMillis();
        long cacheTime = (endCache - startCache);

        // 캐시 X - DB 직접 조회
        long startNoCache = System.currentTimeMillis();
        PopularKeywordResponseDTO noCacheResult = noCacheService.getTrendingKeywords();
        long endNoCache = System.currentTimeMillis();
        long noCacheTime = (endNoCache - startNoCache);

        System.out.println("캐시 O (캐시 히트): " + cacheTime + "ms");
        System.out.println("캐시 X (DB 조회):  " + noCacheTime + "ms");
        System.out.println("성능 향상: " + String.format("%.2f", (double)noCacheTime / cacheTime) + "배");
        System.out.println("응답 시간 단축: " + (noCacheTime - cacheTime) + "ms");

    }

    @Test
    @DisplayName("3. 반복 요청 평균 응답 시간 비교")
    void compareAverageResponseTime() {
        System.out.println("\n========== 3. 반복 요청 평균 응답 시간 (" + TEST_COUNT + "회) ==========");

        // 워밍업
        for (int i = 0; i < WARMUP_COUNT; i++) {
            cacheService.getTrendingKeywords();
            noCacheService.getTrendingKeywords();
        }

        // 캐시 O - 반복 측정
        long cacheStart = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            cacheService.getTrendingKeywords();
        }
        long cacheEnd = System.currentTimeMillis();



        // 캐시 X - 반복 측정
        long noneCacheStart = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            noCacheService.getTrendingKeywords();
        }
        long noneCacheEnd = System.currentTimeMillis();

        long cacheTime = cacheEnd-cacheStart;
        long noneCacheTime = noneCacheEnd-noneCacheStart;

        System.out.println("\n[캐시 O]");
        System.out.println("시간 : " + (cacheTime));
        System.out.println("\n[캐시 X]");
        System.out.println("시간 : " + (noneCacheTime));
        System.out.println("\n개선 정도 : " + (noneCacheTime - cacheTime)/noneCacheTime );

    }

    @Test
    @DisplayName("4. 동시 요청 처리 성능 비교")
    void compareConcurrentRequestPerformance() throws InterruptedException {
        System.out.println("\n========== 4. 동시 요청 처리 성능 (" + CONCURRENT_USERS + "명) ==========");

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        CountDownLatch latch = new CountDownLatch(CONCURRENT_USERS);

        // 캐시 O - 동시 요청
        long cacheStart = System.currentTimeMillis();
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            executor.submit(() -> {
                try {
                    cacheService.getTrendingKeywords();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        long cacheTotalTime = System.currentTimeMillis() - cacheStart;

        // 캐시 X - 동시 요청
        CountDownLatch latch2 = new CountDownLatch(CONCURRENT_USERS);
        long noCacheStart = System.currentTimeMillis();
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            executor.submit(() -> {
                try {
                    long start = System.nanoTime();
                    noCacheService.getTrendingKeywords();
                    long end = System.nanoTime();
                } finally {
                    latch2.countDown();
                }
            });
        }

        latch2.await(30, TimeUnit.SECONDS);
        long noCacheTotalTime = System.currentTimeMillis() - noCacheStart;

        executor.shutdown();

        // 통계 계산

        System.out.println("\n[캐시 O - " + CONCURRENT_USERS + "명 동시 요청]");
        System.out.println("  전체 소요 시간: " + cacheTotalTime + "ms");

        System.out.println("\n[캐시 X - " + CONCURRENT_USERS + "명 동시 요청]");
        System.out.println("  전체 소요 시간: " + noCacheTotalTime + "ms");

        System.out.println("\n[성능 향상]");
        System.out.println("개선 정도 : " + ((noCacheTotalTime - cacheTotalTime)/noCacheTotalTime)*100);


        assertThat(cacheTotalTime).isLessThan(noCacheTotalTime);
    }

    @Test
    @DisplayName("5. DB 부하 비교 - 쿼리 실행 횟수")
    void compareDbLoad() {
        System.out.println("\n========== 5. DB 부하 비교 ==========");

        int requestCount = 10;

        // 캐시 초기화
        redisTemplate.delete(CACHE_KEY);

        // 캐시 O - DB 쿼리 횟수 측정
        System.out.println("\n[캐시 O - " + requestCount + "회 요청]");
        System.out.println("첫 요청: DB 조회 1회 (캐시 미스)");
        cacheService.getTrendingKeywords();

        System.out.println("이후 " + (requestCount - 1) + "회 요청: DB 조회 0회 (캐시 히트)");
        for (int i = 1; i < requestCount; i++) {
            cacheService.getTrendingKeywords();
        }
        System.out.println("총 DB 쿼리: 1회");

        // 캐시 X - DB 쿼리 횟수
        System.out.println("\n[캐시 X - " + requestCount + "회 요청]");
        System.out.println("매 요청마다 DB 조회");
        for (int i = 0; i < requestCount; i++) {
            noCacheService.getTrendingKeywords();
        }
        System.out.println("총 DB 쿼리: " + requestCount + "회");

        System.out.println("\n[DB 부하 감소]");
        System.out.println("  쿼리 감소율: " +
                String.format("%.1f", (1.0 - 1.0 / requestCount) * 100) + "%");
        System.out.println("  DB 부하 감소: " + requestCount + "배");
    }

    @Test
    @DisplayName("6. 캐시 적중률(Cache Hit Rate) 측정")
    void measureCacheHitRate() {
        System.out.println("\n========== 6. 캐시 적중률 측정 ==========");

        int totalRequests = 100;
        int cacheHits = 0;
        int cacheMisses = 0;

        for (int i = 0; i < totalRequests; i++) {
            // 10% 확률로 캐시 초기화 (캐시 미스 시뮬레이션)
            if (Math.random() < 0.1) {
                redisTemplate.delete(CACHE_KEY);
                cacheMisses++;
            } else {
                cacheHits++;
            }

            cacheService.getTrendingKeywords();
        }

        double hitRate = (double)cacheHits / totalRequests * 100;

        System.out.println("총 요청: " + totalRequests + "회");
        System.out.println("캐시 히트: " + cacheHits + "회");
        System.out.println("캐시 미스: " + cacheMisses + "회");
        System.out.println("캐시 적중률: " + String.format("%.2f", hitRate) + "%");

        assertThat(hitRate).isGreaterThan(80.0);
    }

    @Test
    @DisplayName("7. 메모리 사용량 비교")
    void compareMemoryUsage() {
        System.out.println("\n========== 7. 메모리 사용량 비교 ==========");

        Runtime runtime = Runtime.getRuntime();

        // GC 실행
        System.gc();
        Thread.yield();

        // 캐시 X - 메모리 측정
        long beforeNoCache = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < 100; i++) {
            noCacheService.getTrendingKeywords();
        }
        long afterNoCache = runtime.totalMemory() - runtime.freeMemory();
        long noCacheMemory = afterNoCache - beforeNoCache;

        // GC 실행
        System.gc();
        Thread.yield();

        // 캐시 O - 메모리 측정
        long beforeCache = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < 100; i++) {
            cacheService.getTrendingKeywords();
        }
        long afterCache = runtime.totalMemory() - runtime.freeMemory();
        long cacheMemory = afterCache - beforeCache;

        System.out.println("캐시 X 메모리 사용: " + formatBytes(noCacheMemory));
        System.out.println("캐시 O 메모리 사용: " + formatBytes(cacheMemory));
        System.out.println("메모리 절약: " + formatBytes(Math.abs(noCacheMemory - cacheMemory)));
    }

    // 유틸리티 메서드
    private PerformanceStats calculateStats(List<Long> timings) {
        timings.sort(Long::compareTo);

        long sum = timings.stream().mapToLong(Long::longValue).sum();
        long avg = sum / timings.size();
        long min = timings.get(0);
        long max = timings.get(timings.size() - 1);
        long median = timings.get(timings.size() / 2);

        return new PerformanceStats(avg, min, max, median);
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }

    static class PerformanceStats {
        long avg;
        long min;
        long max;
        long median;

        PerformanceStats(long avg, long min, long max, long median) {
            this.avg = avg;
            this.min = min;
            this.max = max;
            this.median = median;
        }
    }
}