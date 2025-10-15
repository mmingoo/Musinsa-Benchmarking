package com.avengers.musinsa.domain.review.entity;

import lombok.Getter;

@Getter
public class ReviewImage {
    private Long reviewImageId;

    private Review reviews;
    private Long reviewId;

    private String imagesUrl;
}