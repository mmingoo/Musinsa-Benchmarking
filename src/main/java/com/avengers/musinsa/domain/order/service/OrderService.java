package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest.ProductLine;
import com.avengers.musinsa.domain.order.dto.response.*;
import com.avengers.musinsa.domain.order.entity.Order;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import com.avengers.musinsa.domain.product.entity.ProductVariant;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import com.avengers.musinsa.domain.user.dto.UserResponseDto;
import com.avengers.musinsa.domain.user.repository.UserRepository;
import com.avengers.musinsa.global.exception.OutOfStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;

    @Qualifier("orderTaskExecutor")
    private final Executor orderTaskExecutor;

    // 주문자 기본정보 조회
    public UserInfoDTO getUserInfo(Long userId) {
        return orderRepository.getUserInfo(userId);
    }



    // 주문하기
    @Transactional
    public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
        log.info("주문 시작 - userId: {}, 상품 개수: {}", userId, request.getProduct().size());

        // ========== Phase 1: 동기 처리 (필수, 실시간 정합성) ==========

        // 1. 재고 확인 + 감소 (비관적 락)
        for (ProductLine product : request.getProduct()) {
            ProductVariant variant = productVariantRepository
                    .findByIdWithLock(product.getVariantId()); // SELECT ... FOR UPDATE

            if (variant == null) {
                throw new IllegalArgumentException("존재하지 않는 상품입니다. variantId: " + product.getVariantId());
            }

            if (variant.getStockQuantity() < product.getQuantity()) {
                throw new OutOfStockException(
                        String.format("재고 부족 - 상품: %s, 요청: %d개, 재고: %d개",
                                variant.getVariantName(),
                                product.getQuantity(),
                                variant.getStockQuantity())
                );
            }

            // 재고 즉시 감소 (같은 트랜잭션)
            productVariantRepository.decrementStock(
                    product.getVariantId(),
                    product.getQuantity()
            );

            log.debug("재고 감소 완료 - variantId: {}, quantity: {}",
                    product.getVariantId(), product.getQuantity());
        }

        // 2. 주문 생성 (같은 트랜잭션)
        Long orderId = createOrderEntity(userId, request);
        log.info("주문 생성 완료 - orderId: {}", orderId);

        // ========== Phase 2: 비동기 처리 (부가 작업) ==========

        // 3. OrderItems 생성 (비동기)
        CompletableFuture.runAsync(() -> {
            try {
                orderRepository.batchCreateOrderItems(
                        orderId,
                        request.getProduct(),
                        request.getCouponId()
                );
                log.info("OrderItems 생성 완료 (비동기) - orderId: {}", orderId);
            } catch (Exception e) {
                log.error("OrderItems 생성 실패 - orderId: {}", orderId, e);

            }
        }, orderTaskExecutor);

        // TODO: 추가 비동기 작업
        // - 알림 발송
        // - 포인트 적립
        // - 쿠폰 사용 처리

        return new OrderCreateResponse(orderId);
    }

    /**
     * 주문 엔티티 생성 (private 메서드)
     */
    private Long createOrderEntity(Long userId, OrderCreateRequest request) {
        // 배송 정보 저장
        Long shippingId = orderRepository.createShipment(request);

        // 주문 저장
        orderRepository.createOrder(userId, shippingId, request.getPayment());

        // 생성된 주문 ID 반환
        return request.getPayment().getOrderId();
    }

    // 주문 완료 정보 조회
    public OrderSummaryResponse.OrderSummaryDto getCompletionOrderSummary(Long orderId, Long userId) {
        Order order = orderRepository.getOrder(orderId);

        UserResponseDto.UserNameAndEmailAndMobileDto userInfo =
                userRepository.findUserNameAndEmailAndMobileById(userId);

        List<OrderDto.OrderItemInfo> orderItems = orderRepository.findOrderItems(orderId);

        ShippingAddressDto.shippingAddressDto shippingAddress =
                ShippingAddressDto.shippingAddressDto.builder()
                        .recipientName(order.getRecipientName())
                        .phone(order.getRecipientPhone())
                        .postCode(order.getPostalCode())
                        .address(order.getRecipientAddress())
                        .build();

        PriceInfoDto.priceInfoDto priceInfo = PriceInfoDto.priceInfoDto.builder()
                .finalPrice(order.getFinalPrice())
                .orderDiscountAmount(order.getOrderDiscountAmount())
                .shippingFee(order.getShippingFee())
                .totalPrice(order.getTotalPrice())
                .build();

        return OrderSummaryResponse.OrderSummaryDto.builder()
                .orderCode(order.getOrderCode())
                .orderDateTime(order.getOrderDateTime())
                .buyerDto(userInfo)
                .orderItemsDto(orderItems)
                .shippingAddressDto(shippingAddress)
                .priceInfoDto(priceInfo)
                .build();
    }

    // 배송지 목록 조회
    public List<ShippingAddressOrderDTO> getShippingAddressesUserId(Long userId) {
        return orderRepository.getShippingAddressesUserId(userId);
    }
}