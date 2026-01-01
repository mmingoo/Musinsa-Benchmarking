package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.repository.FailureQueueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFailureQueueServiceImpl implements OrderFailureQueueService {

    private final FailureQueueRepository failureQueueRepository;
    private final ObjectMapper objectMapper;

    /**
     * 실패 큐에 추가 (새 트랜잭션)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addToFailureQueue(
            String failureType,
            Long orderId,
            List<OrderCreateRequest.ProductLine> orderProducts,
            String errorMessage) {

        try {
            Map<String, Object> failureData = new HashMap<>();
            failureData.put("failureType", failureType);
            failureData.put("orderId", orderId);
            failureData.put("orderProducts", orderProducts);
            failureData.put("errorMessage", errorMessage);
            failureData.put("occurredAt", LocalDateTime.now());

            String failureJson = objectMapper.writeValueAsString(failureData);

            failureQueueRepository.insert(failureType, orderId, failureJson);

            log.info("[실패 큐 추가] type={}, orderId={}", failureType, orderId);

        } catch (Exception e) {
            log.error("[실패 큐 추가 실패] 큐 저장 자체가 실패", e);
            // 최악의 경우: 로그만 남기고 진행
        }
    }
}