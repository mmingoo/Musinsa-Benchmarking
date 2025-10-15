package com.avengers.musinsa.domain.user.dto;

import lombok.Data;

public class UserResponseDto {
    @Data
    public static class UserNameAndEmailAndMobileDto{
        private String name;
        private String email;
        private String mobile;
    }

}
