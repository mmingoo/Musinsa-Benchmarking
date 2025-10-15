package com.avengers.musinsa.domain.category.respository;

import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.category.dto.response.CategoryProductResponse;
import com.avengers.musinsa.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 이 클래스는 데이터 접근 계층
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    //상품 카테고리 조회 - 대중소 분류
    @Override
    public List<CategoryProductResponse> getCategoryProductList(Long parentCategoryId) {
        return categoryMapper.getCategoryProductList(parentCategoryId);
    }
    //브랜드 목록 전체 조회
    @Override
    public List<BrandResponse> getBrandList() {
        return categoryMapper.getBrandList();
    }
    //카테고리별 브랜드 목록 조회
    @Override
    public List<BrandResponse> getBrandsByCategoryId(Long brandCategoryId) {
        return categoryMapper.getBrandsByCategoryId(brandCategoryId);
    }
    //브랜드 초성으로 목록 조회
    @Override
    public List<BrandResponse> findBrandsByEnglishFirstLetter(String brandFirstLetter) {
        return categoryMapper.findBrandsByEnglishFirstLetter(brandFirstLetter);
    }
    @Override
    public List<BrandResponse> findBrandsByKoreanFirstLetter(String brandFirstLetter) {
        return categoryMapper.findBrandsByKoreanFirstLetter(brandFirstLetter);
    }

}
