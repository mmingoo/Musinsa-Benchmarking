package com.avengers.musinsa.domain.order.dto.request;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.border.Border;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    private Long userId;
    private Long addressId;
    private Long couponId;
    private Shipping shipping;
    private Payment payment;
    private List<ProductLine> product;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Shipping {
        // ★ selectKey target
        @lombok.Setter
        private Long shippingId;
        private Integer shippingStatusId;
        private String recipientName;
        private String recipientPhone;
        private String recipientAddress;
        private String shippingDirectRequest;
        private String postalCode;


    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payment {
        private Integer totalAmount;
        private Integer discountAmount;
        private Integer userOfReward;
        private Integer deliveryFee;
        private Long paymentMethodId;
        private Long orderId;


    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductLine {
        private Long productId;
        private Long variantId;
        private Map<String, String> options;
        private Integer finalPrice;
        private int quantity;
        private String optionName;
        private Integer discountRate;
        private Integer productDiscountAmount;
        private String color;
        private String orderItemSize;
        private String material;

        public Integer getTotalPrice() {
            return finalPrice != null ? finalPrice * quantity : 0;
        }
    }


}
