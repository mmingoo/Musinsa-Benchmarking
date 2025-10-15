package com.avengers.musinsa.domain.brand.entity;

import lombok.Getter;

@Getter
public class BrandHasCategory {
    private Integer brandHasCategoryId;

    private BrandCategory brandCategory;
    private Long brandCategoryId;

    private Brand brandId;
}