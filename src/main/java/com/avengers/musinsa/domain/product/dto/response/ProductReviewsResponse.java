package com.avengers.musinsa.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductReviewsResponse {
    private Long reviewId;

    private String nickName;
    private String content;
    private String purchaseOptions;
    private Integer helpCount;
    private Integer rating;

}
