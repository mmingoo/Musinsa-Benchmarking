package com.avengers.musinsa.domain.product.entity;

import com.avengers.musinsa.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ProductInquiry {
    private Integer productInquiryId;

    private Product product;
    private Long productId;

    private User user;
    private Long userId;


    private String inquiryType;
    private String title;
    private String answerState;
    private String nickName;
    private String content;
}