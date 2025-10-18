package com.avengers.musinsa.domain.brand.repository;

import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.dto.response.BrandLikeResponse;
import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.brand.dto.response.UserBrandStatus;

import java.util.List;

public interface BrandRepository {

    List<BrandDto> selectRecentVisitedBrands(Long userId);

    void insertUserBrandLike(Long userId, Long brandId);

    List<BrandResponse> findByBrandName(String brandName);


    BrandLikeResponse getIsLikedBrand(Long userId, Long brandId);

    void switchBrandLike(Long userId, Long brandId);

    void minusBrandLikeCnt(Long brandId);

    void plusBrandLikeCnt(Long brandId);

    UserBrandStatus getUserBrandStatus(Long userId, Long brandId);


}
