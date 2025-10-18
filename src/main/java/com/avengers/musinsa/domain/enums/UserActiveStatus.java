package com.avengers.musinsa.domain.enums;

import lombok.Getter;

@Getter
public enum UserActiveStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPENDED("SUSPENDED");

    private final String value;

    UserActiveStatus(String value) {
        this.value = value;
    }

}
