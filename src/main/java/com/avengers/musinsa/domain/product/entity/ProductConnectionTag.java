package com.avengers.musinsa.domain.product.entity;

import lombok.Getter;

@Getter
public class ProductConnectionTag {
    private Integer productConnectionTagId;

    private Product product;
    private Long productId;

    private String tagName;
}