package com.avengers.musinsa.domain.coupons.entity;

import com.avengers.musinsa.domain.user.entity.User;
import lombok.Getter;

//쿠폰 조건
@Getter
public class CouponCondition {
    private Integer couponConditionId;

    private com.avengers.musinsa.domain.coupons.entity.UserGrade userGrade;
    private Long userGradeId;

    private User user;
    private Long userId;

    private Coupon coupon;
    private Long couponId;

    private Integer conditionType;
    private Integer minPurchaseAmount;
    private Integer minQuantity;
    private Integer minProductCount;
}
