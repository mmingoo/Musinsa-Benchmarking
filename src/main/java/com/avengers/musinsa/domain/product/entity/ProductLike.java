package com.avengers.musinsa.domain.product.entity;

import com.avengers.musinsa.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ProductLike {
    private Long productLikeId;

    private User user;
    private Long userId;

    private Product product;
    private Long productId;
}