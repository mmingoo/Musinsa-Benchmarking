package com.avengers.musinsa.domain.product.entity;

import com.avengers.musinsa.domain.user.entity.User;
import java.sql.Timestamp;
import lombok.Getter;

@Getter
public class ProductViewLog {
    private Long productViewLogId;

    private Product product;
    private Long productId;

    private User user;
    private Long userId;

    private Timestamp viewAt;
    private ProductViewType productViewType;
}