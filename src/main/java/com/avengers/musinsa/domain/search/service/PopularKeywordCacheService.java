package com.avengers.musinsa.domain.search.service;

import com.avengers.musinsa.domain.search.dto.ResultDTO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class PopularKeywordCacheService {

    private static final String CACHE_KEY = "popular:keywords";
    private static final String LOCK_KEY = "lock:popular:keywords";
    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    // ⭐ 분산 락 설정
    private static final Duration LOCK_EXPIRE_TIME = Duration.ofSeconds(10);  // 락 만료 시간
    private static final Duration LOCK_WAIT_TIME = Duration.ofMillis(100);    // 재시도 대기
    private static final int LOCK_RETRY_COUNT = 3;                             // 재시도 횟수

    private final RedisTemplate<String, Object> redisTemplate;
    private final DistributedLockService lockService;  // ⭐ 분산 락 서비스
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    public PopularKeywordCacheService(RedisTemplate<String, Object> redisTemplate,
                                      DistributedLockService lockService,  // ⭐ 추가
                                      MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.lockService = lockService;  // ⭐ 추가

        this.cacheHitCounter = Counter.builder("cache.popular.keywords.hit")
                .description("인기검색어 캐시 히트 수")
                .tag("cache", "popular_keywords")
                .register(meterRegistry);

        this.cacheMissCounter = Counter.builder("cache.popular.keywords.miss")
                .description("인기검색어 캐시 미스 수")
                .tag("cache", "popular_keywords")
                .register(meterRegistry);
    }

    /**
     * Cache Aside: 캐시 조회
     */
    @SuppressWarnings("unchecked")
    public List<ResultDTO> getFromCache() {
        try {
            Object cached = redisTemplate.opsForValue().get(CACHE_KEY);

            if (cached != null) {
                cacheHitCounter.increment();
                log.debug("[Cache HIT] 인기검색어 캐시 조회 성공");
                return (List<ResultDTO>) cached;
            }

            cacheMissCounter.increment();
            log.debug("[Cache MISS] 인기검색어 캐시 없음");
            return null;

        } catch (Exception e) {
            log.error("[Cache ERROR] Redis 조회 실패: {}", e.getMessage());
            cacheMissCounter.increment();
            return null;
        }
    }

    /**
     * Write Around + 분산 락: 캐시 갱신
     */
    public void updateCache(List<ResultDTO> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            log.warn("[Cache SKIP] 갱신할 데이터 없음");
            return;
        }

        // ⭐ 락 값 생성 (UUID - 본인 락 식별용)
        String lockValue = lockService.generateLockValue();

        // ⭐ 분산 락 획득 시도 (재시도 포함)
        boolean isLocked = lockService.tryLockWithRetry(
                LOCK_KEY,
                lockValue,
                LOCK_EXPIRE_TIME,
                LOCK_WAIT_TIME,
                LOCK_RETRY_COUNT
        );

        if (!isLocked) {
            log.warn("[Cache LOCK FAIL] 다른 서버가 캐시 갱신 중");
            return;
        }

        try {
            // ⭐ Double-Check: 락 획득 후 캐시 재확인
            Object existing = redisTemplate.opsForValue().get(CACHE_KEY);
            if (existing != null) {
                log.info("[Cache SKIP] 이미 다른 서버가 캐시 갱신 완료");
                return;
            }

            // 캐시 갱신
            redisTemplate.opsForValue().set(CACHE_KEY, keywords, CACHE_TTL);
            log.info("[Cache UPDATE] 인기검색어 캐시 갱신 ({}개)", keywords.size());

        } finally {
            // ⭐ 락 해제 (반드시 실행)
            lockService.unlock(LOCK_KEY, lockValue);
        }
    }

    /**
     * 캐시 삭제
     */
    public void evictCache() {
        try {
            Boolean deleted = redisTemplate.delete(CACHE_KEY);
            if (Boolean.TRUE.equals(deleted)) {
                log.info("[Cache EVICT] 인기검색어 캐시 삭제");
            }
        } catch (Exception e) {
            log.error("[Cache ERROR] Redis 삭제 실패: {}", e.getMessage());
        }
    }
}