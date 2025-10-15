package com.avengers.musinsa.domain.user.entity;

import lombok.Getter;

@Getter
public class UserAddress {
    private Integer userAddressId;

    private User user;
    private Long userId;

    private String addressName;
    private String location;
    private String phoneNumber;
    private Integer isDefault;
    private Integer isRecent;
}
