package com.avengers.musinsa.domain.brand.repository;

import com.avengers.musinsa.domain.brand.dto.response.BrandLikeResponse;
import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.dto.response.UserBrandStatus;
import com.avengers.musinsa.mapper.BrandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {
    private final BrandMapper brandMapper;

    @Override
    public List<BrandDto> selectRecentVisitedBrands(Long userId){
        return this.brandMapper.selectRecentVisitedBrands(userId);
    }

    @Override
    public List<BrandResponse> findByBrandName(String brandName) {
        return this.brandMapper.findByBrandName(brandName);
    }


    //브랜드 좋아요 토글
    @Override
    public UserBrandStatus getUserBrandStatus(Long userId, Long brandId){
        return brandMapper.getUserBrandStatus(userId,brandId);
    }
    @Override
    public void insertUserBrandLike(Long userId, Long brandId) {
        brandMapper.insertUserBrandLike(userId,brandId);
    }

    public void updateBrandLikeCnt(Long brandId) {
        brandMapper.updateBrandLikeCnt(brandId);
    }

    @Override
    public BrandLikeResponse getIsLikedBrand(Long userId, Long brandId) {
        return brandMapper.getIsLikedBrand(userId, brandId);
    }
    @Override
    public void plusBrandLikeCnt(Long brandId) {
        brandMapper.plusBrandLikeCnt(brandId);
    }

    @Override
    public void minusBrandLikeCnt(Long brandId) {
        brandMapper.minusBrandLikeCnt(brandId);
    }

    @Override
    public void switchBrandLike(Long userId, Long brandId) {
        brandMapper.switchBrandLike(userId,brandId);
    }
    //

}
