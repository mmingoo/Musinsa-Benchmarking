package com.avengers.musinsa.domain.product.entity;

import lombok.Getter;

@Getter
public class ProductVariant {
    private Integer productVariantId;

    private Product product;
    private Long productId;

    private String skuCode;
    private String variantName;
    private Integer price;
    private Integer stockQuantity;
    private String sizeValue;
    private String colorValue;
    private String materialValue;
    private Integer extraPrice;
}