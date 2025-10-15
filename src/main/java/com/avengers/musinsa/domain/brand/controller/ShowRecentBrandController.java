package com.avengers.musinsa.domain.brand.controller;

import com.avengers.musinsa.domain.brand.dto.BrandDto;
import com.avengers.musinsa.domain.brand.service.BrandServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


//최근 방문한 브랜드 조회
//userId 반환
//recentBrands - 최근 방문 브랜드 배열
//brandId - 브랜드ID
//brandName - 브랜드명
//lastVisitedAt
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class ShowRecentBrandController {
    //최근 방문한 브랜드 조회 API

    private final BrandServiceImpl brandServiceImpl;


    @GetMapping("/recent-keyword/{user_id}")
    public List<BrandDto> recentBrands(@PathVariable("user_id") Long userId) {
        return brandServiceImpl.getRecentVisitBrands(userId);
    }
}
