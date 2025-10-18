package com.avengers.musinsa.domain.product.entity;

import lombok.Getter;

@Getter
public class ProductInquiryImage {
    private Integer productInquiryImageId;

    private ProductInquiry productInquiry;
    private Long productInquiryId;

    private String ImageUrl;
}