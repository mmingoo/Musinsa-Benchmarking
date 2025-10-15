package com.avengers.musinsa.domain.category.respository;

import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.category.dto.response.CategoryProductResponse;

import java.util.List;

public interface CategoryRepository {

    //상품 카테고리 조회 - 대중소 분류
    List<CategoryProductResponse> getCategoryProductList(Long parentCategoryId);

    //브랜드 목록 전체 조회
    List<BrandResponse> getBrandList();

    //카테고리별 브랜드 목록 조회
    List<BrandResponse> getBrandsByCategoryId(Long brandCategoryId);

    //브랜드 초성으로 목록 조회
    List<BrandResponse> findBrandsByEnglishFirstLetter(String brandFirstLetter);
    List<BrandResponse> findBrandsByKoreanFirstLetter(String brandFirstLetter);
}
