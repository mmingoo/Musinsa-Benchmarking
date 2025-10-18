package com.avengers.musinsa.domain.search.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SearchLogResponseDTO {
    private Long logId;
    private boolean success;
}
