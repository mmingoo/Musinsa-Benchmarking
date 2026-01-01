package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCompensationServiceImpl implements  OrderCompensationService {

    private final OrderRepository orderRepository;
    private final OrderFailureQueueService failureQueueService; // 실패 큐 서비스

    /**
     * OrderItems 저장 실패 시 보상 처리
     */
    @Transactional
    public void handleOrderItemsFailure(
            Long orderId,
            List<OrderCreateRequest.ProductLine> orderProducts,
            Exception e) {

        log.error("[보상 트랜잭션] OrderItems 저장 실패 - orderId: {}", orderId);

        try {
            // 1. 주문 상태를 "실패"로 변경
            orderRepository.updateOrderStatus(orderId, "FAILED");
            log.info("[보상 완료] 주문 상태 변경: orderId={}, status=FAILED", orderId);

            // 2. 실패 큐에 추가 (관리자 수동 처리용)
            failureQueueService.addToFailureQueue(
                    "ORDER_ITEMS_FAILURE",
                    orderId,
                    orderProducts,
                    e.getMessage()
            );

            // 3. 관리자에게 알림 발송 (Slack, 이메일 등)
            // notificationService.sendAlert("OrderItems 저장 실패", orderId);

        } catch (Exception compensationError) {
            log.error("[보상 실패] 보상 트랜잭션 자체가 실패: orderId={}", orderId, compensationError);
            // 최악의 경우 수동 개입 필요
        }
    }

    /**
     * 재고 감소 실패 시 보상 처리
     */
    public void handleStockFailure(
            List<OrderCreateRequest.ProductLine> orderProducts,
            Exception e) {

        log.error("[보상 트랜잭션] 재고 감소 실패");

        try {
            // 1. 실패한 재고 감소 작업을 큐에 추가
            failureQueueService.addToFailureQueue(
                    "STOCK_DECREMENT_FAILURE",
                    null,
                    orderProducts,
                    e.getMessage()
            );

            // 2. 관리자 알림
            // notificationService.sendAlert("재고 감소 실패", orderProducts);

            log.info("[보상 완료] 재고 감소 실패 큐에 추가");

        } catch (Exception compensationError) {
            log.error("[보상 실패] 재고 감소 보상 트랜잭션 실패", compensationError);
        }
    }
}