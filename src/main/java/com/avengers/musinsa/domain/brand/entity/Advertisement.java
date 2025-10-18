package com.avengers.musinsa.domain.brand.entity;

import lombok.Getter;

@Getter
public class Advertisement {
    private Integer advertisementId;

    private Brand brand;
    private Long brandId;

    private Integer advertisementPrice;
}