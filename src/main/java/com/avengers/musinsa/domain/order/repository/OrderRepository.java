package com.avengers.musinsa.domain.order.repository;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderDto;
import com.avengers.musinsa.domain.order.dto.response.UserInfoDTO;
import com.avengers.musinsa.domain.order.entity.Order;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import java.util.List;

public interface OrderRepository {
    UserInfoDTO getUserInfo(Long userId);

    Order getOrder(Long orderId);

    List<OrderDto.OrderItemInfo> findOrderItems(Long orderId);

    List<ShippingAddressOrderDTO> getShippingAddressesUserId(Long userId);

    Long createShipment(OrderCreateRequest orderCreateRequest);

    Long createOrder(Long userId, Long shippingId, OrderCreateRequest.Payment payment);

    void createOrderItems(Long orderId, OrderCreateRequest.ProductLine orderProduct, Long couponId);

    void batchCreateOrderItems(Long orderId, List<OrderCreateRequest.ProductLine> orderProducts, Long couponId);
}
