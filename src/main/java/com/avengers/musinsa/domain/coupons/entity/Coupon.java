package com.avengers.musinsa.domain.coupons.entity;

import com.avengers.musinsa.domain.brand.entity.Brand;
import com.avengers.musinsa.domain.user.entity.User;
import lombok.Getter;

import java.util.Date;

//쿠폰
@Getter
public class Coupon {
    private Integer couponId;

    private User user;
    private Long userId;

    private Brand brand;
    private Long brandId;

    private String couponName;
    private String couponCode;
    private String description;
    private Date startDate;
    private Date endDate;
    private char isStackable;

}
