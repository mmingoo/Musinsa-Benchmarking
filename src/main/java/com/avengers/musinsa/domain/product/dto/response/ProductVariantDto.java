package com.avengers.musinsa.domain.product.dto.response;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductVariantDto {
    private Long productVariantId;
    private Long productId;
    private String variantName;
    private Integer price;
    private Integer stockQuantity;
    private String sizeValue;
    private String colorValue;
    private String materialValue;
    private Integer extraPrice;

}
