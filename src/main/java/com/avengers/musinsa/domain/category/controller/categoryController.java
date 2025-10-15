package com.avengers.musinsa.domain.category.controller;

import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.category.dto.response.CategoryProductResponse;
import com.avengers.musinsa.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스는 API용 컨트롤러라는 애너테이션
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class categoryController {

    private final CategoryService categoryService;

    // 상품 대중소 카테고리 가져오기
    @GetMapping("products")
    public List<CategoryProductResponse> categoryProducts(@RequestParam(required = false) Long parentCategoryId) {
        return categoryService.getCategoryProductList(parentCategoryId);
    }

    //카테고리 - 브랜드 목록 전체 조회
    @GetMapping("/brands")
    public List<BrandResponse> categoryBrands() {
        return categoryService.getBrandList();
    }

    //카테고리 - 브랜드 초성으로 조회
    @GetMapping("/letter/{brand-first-letter}/brands")
    public List<BrandResponse> getCategoryBrandsByFirstLetter(
            @PathVariable("brand-first-letter") String brandFirstLetter) {
        return categoryService.getCategoryBrandsByFirstLetter(brandFirstLetter);
    }

    //카테고리 - 카테고리 별로 브랜드 목록 조회
    @GetMapping("/{brandCategoryId}/brands")
    public List<BrandResponse> getBrandsByCategory(@PathVariable Long brandCategoryId) {
        return categoryService.getBrandsByCategoryId(brandCategoryId);
    }
}
