package com.avengers.musinsa.domain.category.dto.rquest;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private Long parentProductCategoryId;
    private String categoryName;
    private String categoryImage;
    private Integer categoryLevel;
}