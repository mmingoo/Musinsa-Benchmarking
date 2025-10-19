package com.avengers.musinsa.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rank;
    private String keyword;
}