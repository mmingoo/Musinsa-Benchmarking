package com.avengers.musinsa.domain.brand.entity;

import lombok.Getter;

@Getter
public class CourierCompany {
    private Integer courierCompanyId;

    private Brand brand;
    private Long brandId;

    private String courierCompanyName;
    private Integer shippingPrice;
}