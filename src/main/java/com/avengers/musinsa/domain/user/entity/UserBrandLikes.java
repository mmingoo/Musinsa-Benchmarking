package com.avengers.musinsa.domain.user.entity;

import com.avengers.musinsa.domain.brand.entity.Brand;
import lombok.Getter;

@Getter
public class UserBrandLikes {
    private Long userBrandLikeId;

    private Brand brand;
    private Long brandId;

    private User user;
    private Long userId;
}
