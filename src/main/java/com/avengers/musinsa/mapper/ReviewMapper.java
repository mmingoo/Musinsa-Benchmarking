package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.review.dto.ReviewMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
    void updateReviewStatus(
            @Param("countDelta") Integer countDelta,
            @Param("productId") Long productId,
            @Param("ratingDelta") Integer ratingDelta);

    ReviewMeta findReviewMetaById(@Param("reviewId") Long reviewId);

}
