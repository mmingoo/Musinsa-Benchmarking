package com.avengers.musinsa.domain.search.repository;

import com.avengers.musinsa.domain.search.response.SearchKeywordResponseDTO;
import com.avengers.musinsa.mapper.SearchMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor //필드를 초기화하는 생성자 코드를 자동으로 만들어주는 애너테이션
public class RecentSearchRepository {
    //getRecentSearchesMapper 주입
    private final SearchMapper searchMapper;

    // 최근 검색어 조회
    public List<SearchKeywordResponseDTO> findRecentSearches(Long userId) {
        List<SearchKeywordResponseDTO> rawKeywords = searchMapper.getRecentSearches(userId);

        Map<String, SearchKeywordResponseDTO> deduplicated = new LinkedHashMap<>();
        for (SearchKeywordResponseDTO keyword : rawKeywords) {
            if (keyword == null || !StringUtils.hasText(keyword.getSearchText())) {
                continue;
            }
            deduplicated.putIfAbsent(keyword.getSearchText(), keyword);
        }

        return new ArrayList<>(deduplicated.values());
    }

    public void deleteAllRecentSearches(Long userId) {
        searchMapper.deleteRecentSearches(userId);
    }

    public void deleteRecentSearchKeyword(Long userId, String keyword) {
        searchMapper.deleteRecentSearchKeyword(userId, keyword);
    }
}
