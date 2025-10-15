package com.avengers.musinsa.domain.order.dto.response;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPageResponse {
    private BuyerInfo buyerInfo;
    private List<OrderProductInfo> products;
    private UserMileageInfo mileageInfo;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BuyerInfo {
        private String name;
        private String phone;
        private String email;
        private DefaultAddress defaultAddress;
        
        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @ToString
        public static class DefaultAddress {
            private Long addressId;
            private String recipientName;
            private String recipientPhone;
            private String postCode;
            private String address;
            private String detailAddress;
        }
    }
    
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderProductInfo {
        private Long brandId;
        private String brandName;
        private String brandImage;
        private Long productId;
        private String productName;
        private String productImage;
        private Integer originalPrice;
        private Integer discountRate;
        private Integer finalPrice;
        private String optionName;
        private Integer quantity;
        private Integer totalPrice;
        private Long cartItemId;
        private ShippingInfo shippingInfo;
        
        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class ShippingInfo {
            private Integer shippingFee;
            private String shippingMethod;
            private String estimatedDelivery;
        }
    }
    

    
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserMileageInfo {
        private Integer totalMileage;

    }
    

}