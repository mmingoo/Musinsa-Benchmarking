package com.avengers.musinsa.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDetailDto {
    private Long productVariantId;
    private String productColor;
    private String productsSize;
    private String variantName;
}