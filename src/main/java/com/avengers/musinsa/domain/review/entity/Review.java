package com.avengers.musinsa.domain.review.entity;

import com.avengers.musinsa.domain.product.entity.Product;
import com.avengers.musinsa.domain.user.entity.User;
import lombok.Getter;

@Getter
public class Review {
    private Long reviewId;

    private Product product;
    private Long productId;

    private User user;
    private Long userId;

    private String nickname;
    private String content;
    private String purchaseOptions;
    private int helpCount;
}
