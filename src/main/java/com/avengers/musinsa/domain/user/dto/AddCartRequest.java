package com.avengers.musinsa.domain.user.dto;

import lombok.Getter;

@Getter
public class AddCartRequest {
    private Long productId;
    private Integer quantity;
    private String variantName;
}
