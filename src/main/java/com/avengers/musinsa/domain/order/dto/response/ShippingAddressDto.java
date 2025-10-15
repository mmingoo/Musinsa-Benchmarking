package com.avengers.musinsa.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;

public class ShippingAddressDto {

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    public static class shippingAddressDto{
        String recipientName;
        String phone;
        String postCode;
        String address;

    }

}
