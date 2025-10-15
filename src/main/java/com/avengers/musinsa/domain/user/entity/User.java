package com.avengers.musinsa.domain.user.entity;

import com.avengers.musinsa.domain.enums.UserActiveStatus;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long userId;
    private String nickname;
    private String username; // OAuth2에서 사용하는 복합키
    private String gender;
    private String birthday;
    private String birthyear;
    private String socialId;
    private String socialType;

    private String name;
    private String activeStatus;
    private Timestamp inactiveTime;
    private Integer userMileage;
    private String isMember;
    private String email;
    private String mobile;
    private String ageGroup;
    private String role;

    private Timestamp activeTime;



}
