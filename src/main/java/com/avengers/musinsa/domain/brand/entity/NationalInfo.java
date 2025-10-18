package com.avengers.musinsa.domain.brand.entity;

import lombok.Getter;

@Getter
public class NationalInfo {
    private Integer nationalInfoId;

    private Brand brand;
    private Long brandId;

    private String nationalImg;
    private String nationalName;
}