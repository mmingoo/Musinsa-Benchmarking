package com.avengers.musinsa.domain.coupons.entity;

import com.avengers.musinsa.domain.user.entity.User;
import lombok.Getter;

//쿠폰타입
@Getter
public class CouponType {
    private Integer couponTypeId;

    private Coupon coupon;
    private Long couponId;

    private User user;
    private Long userId;

    private String typeCode;
    private String typeName;
    private String applyScope;
    private String discountType;

}