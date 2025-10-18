package com.avengers.musinsa.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreateResponse {
    private Long orderId;

    @Override
    public String toString() {
        return "OrderCreateResponse{" +
                "orderId=" + orderId +
                '}';
    }
}
