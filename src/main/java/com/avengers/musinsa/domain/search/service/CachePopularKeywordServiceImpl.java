package com.avengers.musinsa.domain.search.service;

import com.avengers.musinsa.domain.search.dto.KeywordCountDTO;
import com.avengers.musinsa.domain.search.dto.PopularKeywordResponseDTO;
import com.avengers.musinsa.domain.search.dto.ResultDTO;
import com.avengers.musinsa.domain.search.repository.PopularKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CachePopularKeywordServiceImpl {
    private final PopularKeywordRepository popularKeywordRepository;
    private final PopularKeywordCacheService cacheService;


    public PopularKeywordResponseDTO getTrendingKeywords() {
        log.debug("인기검색어 조회 시작");

        // 1. Cache Aside: 캐시 먼저 조회
        List<ResultDTO> cachedKeywords = cacheService.getFromCache();

        if (cachedKeywords != null && !cachedKeywords.isEmpty()) {
            String basedOnTime = popularKeywordRepository.findTrendingKeywordsUpdatedAt();
            return buildResponse(cachedKeywords, basedOnTime, true);
        }

        // 2. Cache Miss: DB 조회
        log.debug("DB에서 인기검색어 조회");
        List<ResultDTO> dbKeywords = popularKeywordRepository.findTrendingKeywords();
        String basedOnTime = popularKeywordRepository.findTrendingKeywordsUpdatedAt();

        // 3. 캐시에 저장
        if (dbKeywords != null && !dbKeywords.isEmpty()) {
            cacheService.updateCache(dbKeywords);
        }

        return buildResponse(dbKeywords, basedOnTime, false);
    }

    @Transactional
    public Map<String, KeywordCountDTO> createPopularKeyword() {
        log.info("인기검색어 집계 시작");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusMinutes(30);

        // 1. 최근 30분 검색어 집계
        List<KeywordCountDTO> keywordCounts = popularKeywordRepository.countKeywords(from, now);
        log.debug("집계된 키워드 수: {}", keywordCounts.size());

        Map<String, KeywordCountDTO> keywordMap = new HashMap<>();

        // 2. DB 업데이트 (Write Around)
        for (KeywordCountDTO dto : keywordCounts) {
            dto.setBasedOnTime(now);

            int updatedRows = popularKeywordRepository.updateKeyword(dto);
            if (updatedRows == 0) {
                popularKeywordRepository.insertKeyword(dto);
            }
            keywordMap.put(dto.getKeyword(), dto);
        }

        // 3. 캐시 갱신
        List<ResultDTO> freshKeywords = popularKeywordRepository.findTrendingKeywords();
        cacheService.updateCache(freshKeywords);

        log.info("인기검색어 집계 완료 - DB 갱신 및 캐시 업데이트");
        return keywordMap;
    }

    private PopularKeywordResponseDTO buildResponse(List<ResultDTO> keywords,
                                                    String basedOnTime,
                                                    boolean fromCache) {
        String timestamp = basedOnTime != null ? basedOnTime : LocalDateTime.now().toString();
        String source = fromCache ? "캐시" : "DB";

        log.debug("인기검색어 응답 생성 - 출처: {}, 키워드 수: {}", source, keywords != null ? keywords.size() : 0);

        return PopularKeywordResponseDTO.builder()
                .basedOnTime(timestamp)
                .message("인기 검색어 조회 성공")
                .success(true)
                .results(keywords)
                .build();
    }
}
