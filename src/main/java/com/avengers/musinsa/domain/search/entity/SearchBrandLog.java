package com.avengers.musinsa.domain.search.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchBrandLog {
    private Long searchBrandLogId;
    private Long brandId;
    private Long userId;

}
