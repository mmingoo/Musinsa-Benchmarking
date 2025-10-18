package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.category.dto.response.CategoryProductResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    List<CategoryProductResponse> getCategoryProductList(@Param("parentCategoryId") Long parentCategoryId);

    List<BrandResponse> getBrandList();

    List<BrandResponse> findBrandsByEnglishFirstLetter(String brandFirstLetter);

    List<BrandResponse> findBrandsByKoreanFirstLetter(String brandFirstLetter);

    List<BrandResponse> getBrandsByCategoryId(Long brandCategoryId);
}
