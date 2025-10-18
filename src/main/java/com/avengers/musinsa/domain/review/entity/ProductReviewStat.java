package com.avengers.musinsa.domain.review.entity;

import java.sql.Timestamp;
import lombok.Getter;

@Getter
public class ProductReviewStat {
    private Long ProductRatingStatId;

    private Review review;
    private Long reviewId;

    private Integer reviewCount;
    private Integer totalReviews;
    private Integer ratingAvg;
    // 여기 애매함 컬럼 이름은 집계일자임
    private Timestamp updateAt;
}