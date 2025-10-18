package com.avengers.musinsa.domain.order.entity;

import com.avengers.musinsa.domain.coupons.entity.Coupon;
import com.avengers.musinsa.domain.product.entity.Product;
import lombok.Getter;

@Getter
public class OrderItem {
    private Long orderItemId;

    private Product product;
    private Long productId;

    private Coupon coupon;
    private Long couponId;

    private Order order;
    private Long orderId;

    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;
}
