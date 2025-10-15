package com.avengers.musinsa.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionRow {
    private Long productId;
    private String optionType;       // "Color"
    private String value;           // "XL"
}
