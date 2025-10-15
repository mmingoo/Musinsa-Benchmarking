package com.avengers.musinsa.domain.brand.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//브랜드 카테고리를 눌렀을 때 나오는 전체 브랜드 목록 조회
@Getter
@Builder
@AllArgsConstructor
public class BrandResponse {
    private Long brandId;
    private String brandNameKr;
    private String brandNameEn;
    private String brandImage;
    private Integer brandLikes;
}
