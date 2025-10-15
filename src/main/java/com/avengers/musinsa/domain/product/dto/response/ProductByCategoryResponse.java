package com.avengers.musinsa.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductByCategoryResponse {
    private Long productId;
    private String productName;
    private String productImage;
    private String brandName;
    private Integer price;
    private Integer productLikes;
    private Double ratingAverage;
    private Integer reviewCount;
    private Boolean isLiked;  // 좋아요 여부
}




