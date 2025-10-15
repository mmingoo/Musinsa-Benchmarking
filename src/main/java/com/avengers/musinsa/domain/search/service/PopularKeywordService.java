package com.avengers.musinsa.domain.search.service;

import com.avengers.musinsa.domain.search.dto.KeywordCountDTO;
import com.avengers.musinsa.domain.search.dto.PopularKeywordResponseDTO;

import java.util.Map;

public interface PopularKeywordService {
    PopularKeywordResponseDTO getTrendingKeywords();

    Map<String, KeywordCountDTO> createPopularKeyword();




}
