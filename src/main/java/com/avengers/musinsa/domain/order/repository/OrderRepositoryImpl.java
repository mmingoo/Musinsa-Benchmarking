package com.avengers.musinsa.domain.order.repository;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderDto;
import com.avengers.musinsa.domain.order.dto.response.UserInfoDTO;
import com.avengers.musinsa.domain.order.entity.Order;

import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import com.avengers.musinsa.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderMapper orderMapper;

    //주문자 기본정보 조회
    public UserInfoDTO getUserInfo(Long userId) {
        return this.orderMapper.getUserInfo(userId);
    }

    // 완료한 주문 정보 조회
    public Order getOrder(Long orderId) {
        System.out.println("order Id : " + orderId);
        return this.orderMapper.getOrder(orderId);
    }

    @Override
    public List<OrderDto.OrderItemInfo> findOrderItems(Long orderId) {
        List<OrderDto.OrderItemInfo> orderItemInfoList = this.orderMapper.findOrderItems(orderId);
        System.out.println(orderItemInfoList.getFirst().toString());
        System.out.println("첫번째값 호출");
        return orderItemInfoList;
    }

    //배송지 목록 조회
    @Override
    public List<ShippingAddressOrderDTO> getShippingAddressesUserId(Long userId) {
        return this.orderMapper.getShippingAddressesUserId(userId);
    }


    //주문하기
    @Override
    public Long createShipment(OrderCreateRequest orderCreateRequest) {
        orderMapper.createShipment(orderCreateRequest);
        return orderCreateRequest.getShipping().getShippingId();
    }

    @Override
    public Long createOrder(Long userId, Long shippingId, OrderCreateRequest.Payment payment) {
        orderMapper.createOrder(userId, shippingId, payment);

        return payment.getOrderId();
    }

    @Override
    public void createOrderItems(Long orderId, OrderCreateRequest.ProductLine orderProduct, Long couponId) {
        orderMapper.createOrderItems(orderId, orderProduct, couponId);
    }

    @Override
    public void batchCreateOrderItems(Long orderId, List<OrderCreateRequest.ProductLine> orderProducts, Long couponId) {
        orderMapper.batchCreateOrderItems(orderId, orderProducts, couponId);
    }



}
