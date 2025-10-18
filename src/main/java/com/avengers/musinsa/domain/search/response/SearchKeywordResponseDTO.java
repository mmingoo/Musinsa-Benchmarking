package com.avengers.musinsa.domain.search.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@RequiredArgsConstructor
public class SearchKeywordResponseDTO {
    private String searchText;
}
