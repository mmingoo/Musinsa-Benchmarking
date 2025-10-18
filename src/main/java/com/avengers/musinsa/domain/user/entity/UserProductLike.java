package com.avengers.musinsa.domain.user.entity;

import com.avengers.musinsa.domain.product.entity.Product;
import lombok.Getter;

@Getter
public class UserProductLike {
    private Long userProductLikeId;

    private User user;
    private Long userId;

    private Product product;
    private Long productId;
}
