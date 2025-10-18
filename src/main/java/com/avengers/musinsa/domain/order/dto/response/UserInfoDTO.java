package com.avengers.musinsa.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
    private Long userId;
    private String userName;
    private String phoneNumber;
    private String location;
}
