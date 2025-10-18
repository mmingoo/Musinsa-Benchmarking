package com.avengers.musinsa.domain.user.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 소셜로그인용!! 괜히 건드렸다가 안되면 안되니까..
// User에 관한 DTO  sms UserResponse DTO에다가 작성해주세요.
public class UserDTO {
    private String role;
    private String name;
    private String username;
    private String gender;
    private String birthday;
    private Integer birthyear;
    private String phone;

}
