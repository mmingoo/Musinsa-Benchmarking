package com.avengers.musinsa.domain.order.dto.request;

import lombok.Data;

@Data
public class DirectPurchaseRequest {
    private String type;
    private Long productId;
    private Integer quantity;
    private Long productVariantId;
    private String optionName;
}