package com.avengers.musinsa.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionInfo {

    private String optionName;
    private Integer quantity;
}
