package com.avengers.musinsa.domain.user.entity;

import com.avengers.musinsa.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCart {
    private Long userCartId;

    private User user;
    private Long userId;

    private Product product;
    private Long productId;

    private Integer cartQuantity;
    private String cartOption;   // black/m
    private String cartStatus;


}
