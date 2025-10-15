package com.avengers.musinsa.domain.product.entity;

import lombok.Getter;

@Getter
public class ProductOptionType {
    private Integer productOptionTypeId;

    private Product product;
    private Long productId;

    private OptionType optionType;
    private Long optionTypeId;

    private Integer displayOrder;
}