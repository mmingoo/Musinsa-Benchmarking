package com.avengers.musinsa.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationResponse {

    private Long productId;
    private Long brandId;
    private String productName;
    private String productBrand;
    private Integer productPrice;
    private String productImage;
    private Integer discountRate;
    private Boolean isLiked;  // 좋아요 여부
}
