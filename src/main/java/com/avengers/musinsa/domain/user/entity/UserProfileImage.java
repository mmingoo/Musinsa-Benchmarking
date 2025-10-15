package com.avengers.musinsa.domain.user.entity;

import lombok.Getter;

@Getter
public class UserProfileImage {
    private Integer userProfileImageId;

    private User user;
    private Long userId;

    private String profileImageUrl;
}
