package com.avengers.musinsa.domain.order.dto.response;

import com.avengers.musinsa.domain.user.dto.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;



public class OrderSummaryResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class OrderSummaryDto{

        Long orderCode;
        LocalDateTime orderDateTime;
        UserResponseDto.UserNameAndEmailAndMobileDto buyerDto;
        List<OrderDto.OrderItemInfo> orderItemsDto;
        ShippingAddressDto.shippingAddressDto shippingAddressDto;
        PriceInfoDto.priceInfoDto priceInfoDto;
    }



}
