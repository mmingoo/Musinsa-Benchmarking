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
public class PopularKeywordServiceImpl implements PopularKeywordService {
    private final PopularKeywordRepository popularKeywordRepository;

    @Override
    public PopularKeywordResponseDTO getTrendingKeywords(){
        List<ResultDTO> trendingKeywords = popularKeywordRepository.findTrendingKeywords();
        String basedOnTime = popularKeywordRepository.findTrendingKeywordsUpdatedAt();

        // Mapper.selectTrendingKeywordsUpdatedAt() 결과를 그대로 사용하고, 없으면 현재 시각 사용
        String responseTimestamp = basedOnTime != null ? basedOnTime : LocalDateTime.now().toString();

        return PopularKeywordResponseDTO.builder()
                .basedOnTime(responseTimestamp)
                .message("인기 검색어 조회 성공")
                .success(true)
                .results(trendingKeywords)
                .build();
    }

    @Transactional
    public Map<String, KeywordCountDTO> createPopularKeyword() {
        // 1. 검색 로그 집계 결과 조회
        LocalDateTime now = LocalDateTime.now();

        List<KeywordCountDTO> keywordCounts = popularKeywordRepository.countKeywords(now.minusMinutes(30), now);


        // 2. Map 변환 (keyword -> DTO)
        Map<String, KeywordCountDTO> keywordMap = new HashMap<>();
        for (KeywordCountDTO dto : keywordCounts) {
            dto.setBasedOnTime(now);

            // Mapper.updateKeyword()를 통해 기존 레코드 덮어쓰기, 없으면 insert로 신규 생성
            int updatedRows = popularKeywordRepository.updateKeyword(dto);


            if (updatedRows == 0) {
                popularKeywordRepository.insertKeyword(dto);
            }
            keywordMap.put(dto.getKeyword(), dto);
        }

        // 3. Map 반환
        return keywordMap;
    }
    // 위에서 만든 map 을 인기검색어 테이블에 저장하는 코드
    //어떤 DTO가 필요? : keyword map이 필요 map<String, Integer>
    // searchKeywordLog 테이블에서 각 키워드별로 몇 번 검색됐는지 개수를 세고, 이를 map 에 저장하는 메서드
    //근데 이건 30분 단위로 지난 30분 동안 검새된 키워드 기준으로 키워드 갯수 검색
}
