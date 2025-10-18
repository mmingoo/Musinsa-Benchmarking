package com.avengers.musinsa.domain.category.service;

import com.avengers.musinsa.domain.brand.dto.request.BrandRequest;

import com.avengers.musinsa.domain.category.dto.rquest.CategoryRequest;
import com.avengers.musinsa.domain.category.respository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAdminService {
    
    private final CategoryRepository categoryRepository;
    
    // 브랜드 수정시 캐시 무효화
    @Transactional
    @CacheEvict(value = "brands", allEntries = true)
    public void updateBrand(Long brandId, BrandRequest request) {
        // TODO: brandRepository.update(brandId, request);
    }
    
    // 브랜드 생성시 캐시 무효화
    @Transactional
    @CacheEvict(value = "brands", allEntries = true)
    public void createBrand(BrandRequest request) {
        log.info("[Cache Evict] 브랜드 생성으로 인한 캐시 무효화");
        // TODO: brandRepository.save(request);
    }
    
    // 브랜드 삭제시 캐시 무효화
    @Transactional
    @CacheEvict(value = "brands", allEntries = true)
    public void deleteBrand(Long brandId) {
        log.info("[Cache Evict] 브랜드 삭제로 인한 캐시 무효화 (brandId: {})", brandId);
        // TODO: brandRepository.delete(brandId);
    }
    
    // 카테고리 수정시 캐시 무효화
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void updateCategory(Long categoryId, CategoryRequest request) {
        log.info("[Cache Evict] 카테고리 수정으로 인한 캐시 무효화 (categoryId: {})", categoryId);
        // TODO: categoryRepository.update(categoryId, request);
    }
    
    // 카테고리 생성시 캐시 무효화
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void createCategory(CategoryRequest request) {
        log.info("[Cache Evict] 카테고리 생성으로 인한 캐시 무효화");
        // TODO: categoryRepository.save(request);
    }
    
    // 카테고리 삭제시 캐시 무효화
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long categoryId) {
        log.info("[Cache Evict] 카테고리 삭제로 인한 캐시 무효화 (categoryId: {})", categoryId);
        // TODO: categoryRepository.delete(categoryId);
    }
}