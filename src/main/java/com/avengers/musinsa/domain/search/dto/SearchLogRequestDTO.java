package com.avengers.musinsa.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class SearchLogRequestDTO {
    private String searchText;
    private long userId;
    private Integer searchCount;
}
