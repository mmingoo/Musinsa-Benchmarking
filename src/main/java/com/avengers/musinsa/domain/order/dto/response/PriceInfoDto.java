package com.avengers.musinsa.domain.order.dto.response;

import lombok.*;

public class PriceInfoDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class priceInfoDto{
        Integer finalPrice;
        Integer orderDiscountAmount;
        Integer shippingFee;
        Integer totalPrice;

    }
}
