package com.avengers.musinsa.domain.review.repository;


public interface ReviewRepository {

    void updateReviewStatus(Integer countDelta, Long product, Integer ratingDelta);
}
