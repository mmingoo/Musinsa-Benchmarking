package com.avengers.musinsa.domain.order.entity;

import com.avengers.musinsa.domain.shipments.entity.Shipment;
import com.avengers.musinsa.domain.user.entity.UserAddress;
import com.avengers.musinsa.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long orderId;

    private User user;
    private Long userId;

    //배송지 이름
    private String recipientPhone;
    private String recipientName;
    private String recipientAddress;
    private String postalCode;

    private PaymentMethod paymentMethod;
    private Long paymentMethodId;

    private Long orderCode;

    private Integer shippingFee;
    private Integer totalPrice;
    private Integer orderDiscountAmount;
    private Integer finalPrice;
    private String orderStatus;

    //created_at
    private LocalDateTime orderDateTime;
}
