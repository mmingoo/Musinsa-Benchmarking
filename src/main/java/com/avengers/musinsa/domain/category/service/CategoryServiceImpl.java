package com.avengers.musinsa.domain.category.service;

import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.category.dto.response.CategoryProductResponse;
import com.avengers.musinsa.domain.category.respository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    // 의존성 주입
    private final CategoryRepository categoryRepository;


    @Override
    public List<CategoryProductResponse> getCategoryProductList(Long parentCategoryId) {
        return categoryRepository.getCategoryProductList(parentCategoryId);
    }

    //카테고리 - 브랜드 목록 전체 조회
    @Override
    public List<BrandResponse> getBrandList  () {
        return categoryRepository.getBrandList();
    }

    //카테고리 - 카테고리 별로 브랜드 목록 조회
    @Override
    public List<BrandResponse> getBrandsByCategoryId(Long brandCategoryId) {
        return categoryRepository.getBrandsByCategoryId(brandCategoryId);
    }

    //카테고리 - 초성(ㄱ, A)로 브랜드 조회
    @Override
    public List<BrandResponse> getCategoryBrandsByFirstLetter(String brandFirstLetter){
        if (brandFirstLetter == null || brandFirstLetter.isEmpty()) {
            return List.of();
        }

        // 영어인지 한글인지 판단
        if (brandFirstLetter.matches("[A-Za-z]")) {
            return categoryRepository.findBrandsByEnglishFirstLetter(brandFirstLetter);
        } else {
            return categoryRepository.findBrandsByKoreanFirstLetter(brandFirstLetter);
        }
    }
}
