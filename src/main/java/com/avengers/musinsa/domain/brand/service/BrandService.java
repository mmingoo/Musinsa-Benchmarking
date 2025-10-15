package com.avengers.musinsa.domain.brand.service;

import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.dto.response.BrandLikeResponse;
import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;

import java.util.List;

public interface BrandService {

    //최근 방문한 브랜드 조회
    List<BrandDto> getRecentVisitBrands(Long userId);


    List<BrandResponse> findByBrandName(String brandName);

    //브랜드 좋아요 토글
    BrandLikeResponse BrandLikeToggle(Long userId, Long brandId);
}