package com.avengers.musinsa.domain.product.entity;

import lombok.Getter;

@Getter
public class ProductImage {
    private Long productImageId;

    private Product product;
    private Long productId;

    private String imageType;
    private String imageUrl;

    public void setProduct(Product product) {
        this.product = product;
    }
}