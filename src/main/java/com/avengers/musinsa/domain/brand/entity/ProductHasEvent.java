package com.avengers.musinsa.domain.brand.entity;

import com.avengers.musinsa.domain.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductHasEvent {
    private Product product;
    private Long brandId;

    private EventHashTag eventHashTagId;
}