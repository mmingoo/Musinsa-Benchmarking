package com.avengers.musinsa.domain.search.service;

import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.search.dto.SearchLogRequestDTO;
import com.avengers.musinsa.domain.search.dto.SearchLogResponseDTO;

public interface SearchLogService {
    // 검색 로그 저장
    SearchLogResponseDTO saveSearchLog(SearchLogRequestDTO requestDTO);

    // 검색 로그 저장
    void saveSearchKeywordLog(String keyword, Long userId);

    void saveSearchBrandLog(BrandResponse brandResponse, Long userId);
}
