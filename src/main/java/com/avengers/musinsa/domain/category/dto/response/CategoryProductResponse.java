package com.avengers.musinsa.domain.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryProductResponse {
    private Long productCategoryId;
    private Long parentProductCategoryId;
    private String categoryName;
    private String categoryImage;
    private int categoryLevel;
}
