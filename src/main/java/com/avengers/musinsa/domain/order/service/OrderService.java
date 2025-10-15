package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.response.UserInfoDTO;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest.ProductLine;
import com.avengers.musinsa.domain.order.dto.response.OrderCreateResponse;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import java.util.List;
import com.avengers.musinsa.domain.order.dto.response.*;
import com.avengers.musinsa.domain.order.entity.Order;
import com.avengers.musinsa.domain.order.repository.OrderRepository;
import com.avengers.musinsa.domain.product.repository.ProductVariantRepository;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import com.avengers.musinsa.domain.user.dto.UserResponseDto;
import com.avengers.musinsa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;

    //주문자 기본정보 조회
    public UserInfoDTO getUserInfo(Long userId) {
        return orderRepository.getUserInfo(userId);
    }

    //주문하기
    @Transactional
    public OrderCreateResponse createOrder(Long userId, OrderCreateRequest orderCreateRequest) {
        // 배송 예정 정보ID, 배송 요청사항 타입ID, 배송 상태 ID와 여러 배송 정보 배송정보 테이블에 저장
        // 과 동시에 배송테이블 ID 가져오기
        Long shippingId = orderRepository.createShipment(orderCreateRequest);
        System.out.println(shippingId);

        // 주문 정보 저장 후 주문 ID 가져오기
        Long userAddressId = orderCreateRequest.getAddressId();
        orderRepository.createOrder(userId, shippingId, orderCreateRequest.getPayment());
        Long orderId = orderCreateRequest.getPayment().getOrderId();

        // 주문서 상품 내역 주문한 상품들 순회하며 저장
        List<OrderCreateRequest.ProductLine> orderProducts = orderCreateRequest.getProduct();
        for (ProductLine orderProduct : orderProducts) {

            orderRepository.createOrderItems(orderId, orderProduct, orderCreateRequest.getCouponId());

            // 재고 감소
            productVariantRepository.decrementStock(orderProduct.getVariantId(), orderProduct.getQuantity());

        }
        System.out.println("orderItems 샹성 완료");

        // 상품 판매 내역 - 해야하는데 주문서 상품 내역 저장과 같은 방식이라 안 함
        OrderCreateResponse orderCreateResponse = new OrderCreateResponse(orderId);

        return orderCreateResponse;
    }

    public OrderSummaryResponse.OrderSummaryDto getCompletionOrderSummary(Long orderId, Long userId) {

        // order 정보 가져오기 - orderCode, orderDate, 가격정보(총 금액, 할인 금액, 배송비, 최종금액)
        Order order = orderRepository.getOrder(orderId);

        System.out.println(order.getRecipientName());
        System.out.println(order.getRecipientPhone());
        System.out.println(order.getPostalCode());
        System.out.println(order.getRecipientAddress());

        // 회원 정보 가져오기(이름, 이메일, 전화번호) - Buyer(이름, 이메일, 폰번호)
        UserResponseDto.UserNameAndEmailAndMobileDto userNameAndEmailAndMobile = userRepository.findUserNameAndEmailAndMobileById(
                userId);

        //주문한 상품 목록 가져오기
        List<OrderDto.OrderItemInfo> orderItems = orderRepository.findOrderItems(orderId);

        //수령인 가져오기
        ShippingAddressDto.shippingAddressDto shippingAddressDto = ShippingAddressDto.shippingAddressDto.builder()
                .recipientName(order.getRecipientName())
                .phone(order.getRecipientPhone())
                .postCode(order.getPostalCode())
                .address(order.getRecipientAddress())
                .build();

        //가격 설정
        PriceInfoDto.priceInfoDto priceInfoDto = PriceInfoDto.priceInfoDto.builder()
                .finalPrice(order.getFinalPrice())
                .orderDiscountAmount(order.getOrderDiscountAmount())
                .shippingFee(order.getShippingFee())
                .totalPrice(order.getTotalPrice())
                .build();

        //반환 Dto 설정
        OrderSummaryResponse.OrderSummaryDto completionOrderSummaryResponse = OrderSummaryResponse.OrderSummaryDto.builder()
                .orderCode(order.getOrderCode())
                .orderDateTime(order.getOrderDateTime())
                .buyerDto(userNameAndEmailAndMobile)
                .orderItemsDto(orderItems)
                .shippingAddressDto(shippingAddressDto)
                .priceInfoDto(priceInfoDto)
                .build();

        return completionOrderSummaryResponse;
    }

    //배송지 목록 조회
    public List<ShippingAddressOrderDTO> getShippingAddressesUserId(Long userId) {
        return orderRepository.getShippingAddressesUserId(userId);
    }

}

