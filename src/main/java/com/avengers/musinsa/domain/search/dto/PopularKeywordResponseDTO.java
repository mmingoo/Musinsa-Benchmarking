package com.avengers.musinsa.domain.search.dto;


import com.avengers.musinsa.domain.search.dto.ResultDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class PopularKeywordResponseDTO {
    private String basedOnTime; //현재 시간
    private String message; //성공 여부 메세지
    private boolean success; //http 상태 반환 - 실패/성공
    private List<ResultDTO> results; // 결과 리스트로 반환

}