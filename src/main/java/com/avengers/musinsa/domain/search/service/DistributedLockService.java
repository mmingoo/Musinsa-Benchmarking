package com.avengers.musinsa.domain.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedLockService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 분산 락 획득
     * 
     * @param lockKey 락 키
     * @param lockValue 락 값 (UUID - 락 소유자 식별용)
     * @param expireTime 락 만료 시간
     * @return 락 획득 성공 여부
     */
    public boolean tryLock(String lockKey, String lockValue, Duration expireTime) {
        try {
            // SET NX EX 명령어 실행
            Boolean result = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, lockValue, expireTime);
            
            if (Boolean.TRUE.equals(result)) {
                log.debug("[Lock ACQUIRED] 락 획득 성공: {}", lockKey);
                return true;
            }
            
            log.debug("[Lock FAIL] 락 획득 실패 (다른 프로세스가 점유 중): {}", lockKey);
            return false;
            
        } catch (Exception e) {
            log.error("[Lock ERROR] 락 획득 실패: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 분산 락 해제
     * 
     * @param lockKey 락 키
     * @param lockValue 락 값 (본인이 획득한 락인지 확인)
     */
    public void unlock(String lockKey, String lockValue) {
        try {
            // 본인이 획득한 락인지 확인 후 삭제
            Object currentValue = redisTemplate.opsForValue().get(lockKey);
            
            if (lockValue.equals(currentValue)) {
                redisTemplate.delete(lockKey);
                log.debug("[Lock RELEASED] 락 해제 성공: {}", lockKey);
            } else {
                log.warn("[Lock MISMATCH] 다른 프로세스의 락이거나 이미 만료됨: {}", lockKey);
            }
            
        } catch (Exception e) {
            log.error("[Lock ERROR] 락 해제 실패: {}", e.getMessage());
        }
    }
    
    /**
     * 재시도 로직이 포함된 락 획득
     * 
     * @param lockKey 락 키
     * @param lockValue 락 값
     * @param expireTime 락 만료 시간
     * @param waitTime 재시도 대기 시간
     * @param retryCount 재시도 횟수
     * @return 락 획득 성공 여부
     */
    public boolean tryLockWithRetry(String lockKey, String lockValue, 
                                   Duration expireTime, Duration waitTime, 
                                   int retryCount) {
        for (int i = 0; i < retryCount; i++) {
            if (tryLock(lockKey, lockValue, expireTime)) {
                return true;
            }
            
            try {
                log.debug("[Lock RETRY] {}번째 재시도 대기 중...", i + 1);
                TimeUnit.MILLISECONDS.sleep(waitTime.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("[Lock INTERRUPTED] 재시도 중 인터럽트 발생");
                return false;
            }
        }
        
        log.warn("[Lock TIMEOUT] {}번 재시도 후 락 획득 실패", retryCount);
        return false;
    }
    
    /**
     * UUID 기반 락 값 생성
     */
    public String generateLockValue() {
        return UUID.randomUUID().toString();
    }
}