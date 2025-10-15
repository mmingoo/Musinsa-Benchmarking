package com.avengers.musinsa.domain.search.dto;

import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class KeywordCountDTO {
    private String keyword;
    private int count;
    private LocalDateTime basedOnTime;

    // 30분 검색 로그 집계 결과(키워드, 집계 건수, 기준 시각)를 전달하는 DTO
}
