package com.avengers.musinsa.domain.review.entity;

import lombok.Getter;

@Getter
public class Comment {
    private Long commentId;

    private Review review;
    private Long reviewId;

    private String nickName;
    private String content;
}