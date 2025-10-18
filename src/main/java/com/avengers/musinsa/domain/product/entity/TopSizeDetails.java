package com.avengers.musinsa.domain.product.entity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TopSizeDetails {
    private Long topSizeDetailId;

    private Product product;
    private Long productId;

    private Size CM;
    private BigDecimal length;
    private BigDecimal shoulderWidth;
    private BigDecimal chestWidth;
    private BigDecimal sleaveLength;

}
