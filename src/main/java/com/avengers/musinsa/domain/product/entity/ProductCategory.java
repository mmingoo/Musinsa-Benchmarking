package com.avengers.musinsa.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ProductCategory {
    private Long productCategoryId;
    private Long parentProductCategoryId;
    private String categoryName;

    // 해당 애너테이션을 추가하면 리스트안의 null 요소는 JSON 출력에서 제외된다.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String categoryImage;

    private Integer categoryLevel;
}