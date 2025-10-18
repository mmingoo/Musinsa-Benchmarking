package com.avengers.musinsa.domain.brand.service;

import com.avengers.musinsa.domain.brand.dto.response.BrandLikeResponse;
import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.dto.response.UserBrandStatus;
import com.avengers.musinsa.domain.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService{
    private final BrandRepository brandRepository;

    //최근 방문한 브랜드 조회
    @Override
    public List<BrandDto> getRecentVisitBrands(Long userId) {
        //null 검증
        if(userId == null) {
            throw new IllegalArgumentException("userId is null");
        }

        return brandRepository.selectRecentVisitedBrands(userId);
    }


    @Override
    public List<BrandResponse> findByBrandName(String brandName) {
        return brandRepository.findByBrandName(brandName);
    }

    @Override
    @Transactional
    public BrandLikeResponse BrandLikeToggle(Long userId, Long brandId) {
        UserBrandStatus status = brandRepository.getUserBrandStatus(userId, brandId);
        //레코드가 없을 때
        if( status == null){
            //user_brand_like 테이블에 레코드 추가
            brandRepository.insertUserBrandLike(userId, brandId);
            //brands 테이블 좋아요 수 +1
            brandRepository.plusBrandLikeCnt(brandId);
            //레코드 추가 후 회원과 브랜드의 현재 좋아요 상태를 반환
            return brandRepository.getIsLikedBrand(userId, brandId);
        }
        //이미 좋아요 한 브랜드 좋아요 상태 바꾸기
        else{
            //liked 값 확인 (0 또는 1)
            Integer currentLiked = status.getLiked();
            //liked 컬럼을 0 ↔ 1
            brandRepository.switchBrandLike(userId,brandId);
            //브랜드 테이블의 좋아요 수를 동기화
            if (currentLiked != null && currentLiked == 1){
                brandRepository.minusBrandLikeCnt(brandId);
            } else{
                brandRepository.plusBrandLikeCnt(brandId);}
            //좋아요상태 변경 후 현재 좋아요 상태를 반환
            return brandRepository.getIsLikedBrand(userId, brandId);
        }
    }
}
