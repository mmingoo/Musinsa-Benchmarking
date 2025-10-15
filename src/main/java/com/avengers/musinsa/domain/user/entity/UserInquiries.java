package com.avengers.musinsa.domain.user.entity;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserInquiries {
    private Integer userInquiriesId;

    private User user;
    private Long userId;

    private String title;
    private String content;
    private String inquiriesStatus;
    private String inquiryNumber;
    private Timestamp responseCompletedAt;
}
