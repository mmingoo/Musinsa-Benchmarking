package com.avengers.musinsa.domain.order.dto.request;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter

public class OrderPageRequest {
    private String type; // "FROM_CART" | "DIRECT_PURCHASE"

    // 장바구니에서 주문
    private List<Long> cartItemIds;

    // 직접 주문
    private Long productId;
    private Integer quantity;
    private Long productVariantId;
    private String optionName;

}
