package com.avengers.musinsa.domain.review.repository;


import com.avengers.musinsa.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewMapper reviewMapper;

    @Override
    public void updateReviewStatus(Integer countDelta, Long product, Integer ratingDelta) {
        reviewMapper.updateReviewStatus(countDelta, product, ratingDelta);
    }
}
