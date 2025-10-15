package com.avengers.musinsa.domain.brand.entity;

import lombok.Getter;

@Getter
public class Brand {
    private Long brandId;
    private String nameKr;
    private String nameEn;
    private String brandImage;
    private Integer brandLikes;
    private String brandInfo;
    private String brandBorn;
    private String nameKrFirstInitial;
    private Integer brandDiscount;
}