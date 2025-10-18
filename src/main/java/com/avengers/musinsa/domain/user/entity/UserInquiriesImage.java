package com.avengers.musinsa.domain.user.entity;

import lombok.Getter;

@Getter
public class UserInquiriesImage {
    private Integer userInquiryImageId;

    private UserInquirty userInquiry;
    private Long userInquiryId;

    private String imageUrl;
}

