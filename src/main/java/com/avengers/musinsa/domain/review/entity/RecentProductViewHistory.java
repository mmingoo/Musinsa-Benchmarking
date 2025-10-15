package com.avengers.musinsa.domain.review.entity;

import com.avengers.musinsa.domain.product.entity.Product;
import com.avengers.musinsa.domain.user.entity.User;
import java.sql.Timestamp;
import lombok.Getter;

@Getter
public class RecentProductViewHistory {
    private Long RecentProductViewHistoryId;

    private User user;
    private Long userId;

    private Product product;
    private Long productId;

    private Timestamp viewTime;
}
