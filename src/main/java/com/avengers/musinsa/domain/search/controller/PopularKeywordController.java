package com.avengers.musinsa.domain.search.controller;

import com.avengers.musinsa.domain.search.dto.KeywordCountDTO;
import com.avengers.musinsa.domain.search.dto.PopularKeywordResponseDTO;
import com.avengers.musinsa.domain.search.service.PopularKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class PopularKeywordController {
    private final PopularKeywordService popularKeywordService;

    @GetMapping("/popular-keyword")
    public PopularKeywordResponseDTO findPopularKeyword(){
        return popularKeywordService.getTrendingKeywords();
    }

    //수동 집계 수행 후 DTO 리스트 확인하려고 만든 코드 - 집계된 데이터 JSON으로 바로 확인 가능
    @GetMapping("/popular-keyword/rebuild")
    public ResponseEntity<List<KeywordCountDTO>> rebuildPopularKeyword() {
        Map<String, KeywordCountDTO> keywordMap = popularKeywordService.createPopularKeyword();

        List<KeywordCountDTO> body = keywordMap.values().stream()
                .peek(dto -> {
                    // insert 시에는 count 값을 누적, update 시에는 그대로 유지하도록 count를 보정
                    if (dto.getCount() <= 0) {
                        dto.setCount(1);
                    }
                })
                .toList();

        return ResponseEntity.ok(body);
    }

}
