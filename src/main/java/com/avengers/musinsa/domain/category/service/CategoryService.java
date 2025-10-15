package com.avengers.musinsa.domain.category.service;

import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.category.dto.response.CategoryProductResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryProductResponse> getCategoryProductList(Long parentCategoryId);

    //카테고리 - 브랜드 목록 전체 조회
    List<BrandResponse> getBrandList();

    //카테고리 - 카테고리 별로 브랜드 목록 조회
    List<BrandResponse> getBrandsByCategoryId(Long brandCategoryId);

    //카테고리 - 초성(ㄱ, A)로 브랜드 조회
    List<BrandResponse> getCategoryBrandsByFirstLetter(String brandFirstLetter);
}
