package com.avengers.musinsa.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {
    private int rank; //result 리스트 내 순위 저장
    private String keyword; //리스트 내 키워드 저장
}
