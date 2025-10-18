package com.avengers.musinsa.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductLikeResponse {
    private Long productId;
    private Long userId;
    private boolean liked;
    private Integer likeCount;
}
