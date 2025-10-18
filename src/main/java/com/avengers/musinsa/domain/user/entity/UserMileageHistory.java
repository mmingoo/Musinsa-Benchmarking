package com.avengers.musinsa.domain.user.entity;

import com.avengers.musinsa.domain.order.entity.OrderItem;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserMileageHistory {
    private Integer userMileageId;

    private User user;
    private Long userId;

    private OrderItem orderItem;
    private Long orderItemId;

    private UserMileageType userMileageType;
    private Long userMileageTypeId;


    private Integer amount;
    private String transactionType;
    private Timestamp earnedAt;
    private Timestamp expiresAt;
}
