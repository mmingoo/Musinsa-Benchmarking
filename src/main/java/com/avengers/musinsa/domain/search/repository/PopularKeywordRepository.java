package com.avengers.musinsa.domain.search.repository;

import com.avengers.musinsa.domain.search.dto.KeywordCountDTO;
import com.avengers.musinsa.domain.search.dto.ResultDTO;
import com.avengers.musinsa.mapper.PopularKeywordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PopularKeywordRepository {
    //private final getRecentSearchesMapper getRecentSearchesMapper;
    private final PopularKeywordMapper popularKeywordMapper;

    public List<ResultDTO> findTrendingKeywords() {
        log.info("[PopularKeywordRepository] selectTrendingKeywords 호출");
        return popularKeywordMapper.selectTrendingKeywords();
    }

    // 인기 검색어 리스트가 마지막으로 갱신된 시각을 조회
    public String findTrendingKeywordsUpdatedAt() {
        log.info("[PopularKeywordRepository] selectTrendingKeywordsUpdatedAt 호출");
        return popularKeywordMapper.selectTrendingKeywordsUpdatedAt();
    }

    // 최근 30분 동안의 검색 로그를 집계해 키워드별 카운트를 조회
    public List<KeywordCountDTO> countKeywords(LocalDateTime from, LocalDateTime to) {
        log.info("[PopularKeywordRepository] selectRecentKeywordCounts 호출 - from={}, to={} ", from, to);
        return popularKeywordMapper.selectRecentKeywordCounts(from, to);
    }

    // 집계된 카운트로 기존 인기 검색어 레코드를 갱신
    public int updateKeyword(KeywordCountDTO keywordCountDTO) {
        log.debug("[PopularKeywordRepository] updateKeyword 호출 - {}", keywordCountDTO);
        return popularKeywordMapper.updateKeyword(keywordCountDTO);
    }

    // 집계 결과에 해당하는 신규 인기 검색어를 추가
    public int insertKeyword(KeywordCountDTO keywordCountDTO) {
        log.debug("[PopularKeywordRepository] insertKeyword 호출 - {}", keywordCountDTO);
        return popularKeywordMapper.insertKeyword(keywordCountDTO);
    }

}
