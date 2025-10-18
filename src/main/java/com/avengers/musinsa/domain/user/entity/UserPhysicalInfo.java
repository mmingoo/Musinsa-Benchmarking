package com.avengers.musinsa.domain.user.entity;

import lombok.Getter;

@Getter
public class UserPhysicalInfo {
    private Integer userPhysicalInfoId;

    private User user;
    private Long userId;

    private Integer height;
    private Integer weight;
}
