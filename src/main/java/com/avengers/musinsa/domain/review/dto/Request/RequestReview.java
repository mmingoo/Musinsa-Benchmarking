package com.avengers.musinsa.domain.review.dto.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestReview {
    private Long productId;
    private String content;
    private String purchaseOption;
    private Integer rating;
}
