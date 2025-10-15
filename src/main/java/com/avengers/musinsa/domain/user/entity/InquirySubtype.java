package com.avengers.musinsa.domain.user.entity;

import lombok.Getter;

@Getter
public class InquirySubtype {
    private Integer inquirySubTypeId;

    private InquiryTypes inquiryType;
    private Long inquiryTypeId;

    private String subtypeName;
}
