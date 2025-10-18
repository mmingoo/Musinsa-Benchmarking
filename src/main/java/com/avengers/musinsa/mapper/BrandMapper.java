package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.dto.response.BrandLikeResponse;
import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.brand.dto.response.UserBrandStatus;
import com.avengers.musinsa.domain.brand.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BrandMapper {
    List<BrandDto> selectRecentVisitedBrands(Long userId);

    //브랜드 좋아요 토글
    UserBrandStatus getUserBrandStatus(Long userId, Long brandId);
    void insertUserBrandLike(@Param("userId") Long userId, @Param("brandId") Long brandId);
    BrandLikeResponse getIsLikedBrand(Long userId, Long brandId);
    void updateBrandLikeCnt(Long brandId);
    void switchBrandLike(Long userId, Long brandId);
    void plusBrandLikeCnt(Long brandId);
    void minusBrandLikeCnt(Long brandId);

    List<BrandResponse> findByBrandName(@Param("brandName")String brandName);
}
