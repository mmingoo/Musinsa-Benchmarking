package com.avengers.musinsa.domain.user.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageDto {
    private Long userId;
    private String name;
    private String username;
    private String nickname;
    private String email;
    private Integer userMileage;
    private String profileImage;
    private String gradeName;



}
