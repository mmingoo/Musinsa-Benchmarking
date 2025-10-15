package com.avengers.musinsa.domain.search.service;

import com.avengers.musinsa.domain.search.response.SearchKeywordResponseDTO;

import java.util.List;

public interface RecentSearchService {
    List<SearchKeywordResponseDTO> getRecentSearches(Long userId);

    void deleteAllRecentSearches(Long userId);

    void deleteRecentSearchKeyword(Long userId, String keyword);
}
