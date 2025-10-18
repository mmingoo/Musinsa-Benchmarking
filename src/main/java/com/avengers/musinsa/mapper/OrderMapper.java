package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest.Payment;
import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest.ProductLine;
import java.util.List;
import com.avengers.musinsa.domain.order.dto.response.OrderDto;
import com.avengers.musinsa.domain.order.dto.response.UserInfoDTO;
import com.avengers.musinsa.domain.order.entity.Order;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    //주문자 기본정보 조회
    UserInfoDTO getUserInfo(@Param("userId") Long userId);


    // 주문 조회
    Order getOrder(@Param("orderId") Long orderId);

    // 주문 상품 리스트 조회
    List<OrderDto.OrderItemInfo> findOrderItems(@Param("orderId") Long orderId);

    List<OrderDto.OrderItemInfoTest> getOrderItemInfoTest(@Param("orderId") Long orderId);

    //배송지 목록 조회
    List<ShippingAddressOrderDTO> getShippingAddressesUserId(@Param("userId") Long userId);


    //주문하기
    void createShipment(OrderCreateRequest orderCreateRequest);

    void createOrder(Long userId, Long shippingId, Payment payment);

    void createOrderItems(@Param("orderId") Long orderId,
                          @Param("product") ProductLine product,
                          @Param("couponId") Long couponId);

    void batchCreateOrderItems(@Param("orderId") Long orderId,
                               @Param("orderProducts") List<ProductLine> orderProducts,
                               @Param("couponId") Long couponId);


}
