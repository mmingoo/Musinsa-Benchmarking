package com.avengers.musinsa.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    // 구매 완료 요약 응답 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompletionSummaryResponse {
        private Long orderCode;
        private LocalDateTime orderDateTime;
        private Buyer buyer;
        private List<OrderItemInfo> orderItems;  // 직접 List 사용
        private ShippingAddress shippingAddress;
        private PriceInfo priceInfo;
    }

    // 구매자 정보 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Buyer {
        private String name;
        private String email;
        private String phone;
    }

    // 개별 주문 상품 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfo {
        // 브랜드 정보
        private Long brandId;
        private String brandNameKr;
        private String brandNameEng;
        private String brandImage;

        // 상품 정보
        private Long productId;
        private String productName;
        private Integer price;
        private String imageUrl;

        // 주문 항목 정보
        private Integer quantity;
        private Integer unitPrice;
        private Integer discountRate;
        private Integer totalPrice;
    }

    // 상품 옵션 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Options {
        private String color;
        private String size;
        private String material;
        private String storage;
    }

    // 배송 주소 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddress {
        private String recipientName;
        private String phone;
        private String address;
        private String detailAddress;
    }

    // 가격 정보 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceInfo {
        private Integer subtotal;          // 상품 총액
        private Integer discountAmount;    // 할인 금액
        private Integer shippingFee;       // 배송비
        private Integer totalPrice;        // 최종 결제 금액
    }

    // 사용자 정보 DTO (기존 UserInfoDTO 개선)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String userName;
        private String phoneNumber;
        private String location;
    }

    // 주문 상품 DTO (기존 OrderProductDTO 개선)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProduct {
        private Long productId;
        private String productName;
        private String brand;
        private String productImage;
        private String size;
        private String color;
        private String material;
        private Integer quantity;
        private Integer totalPrice;
    }

    // MyBatis 조회 결과용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryResult {
        private Long orderCode;
        private LocalDateTime orderDateTime;
        private String buyerName;
        private String buyerEmail;
        private String buyerPhone;
        private String recipientName;
        private String recipientPhone;
        private String address;
        private String detailAddress;
        private Integer subtotal;
        private Integer discountAmount;
        private Integer shippingFee;
        private Integer totalPrice;

        // 상품 정보 (JOIN 결과)
        private Long productId;
        private String productName;
        private String brandName;
        private Integer quantity;
        private Integer unitPrice;
        private String productImage;
        private String options; // JSON 문자열로 저장
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfoTest {
        // 브랜드 정보
//        private Long brandId;
//        private String brandNameKr;
//        private String brandNameEng;
//        private String brandImage;

        // 상품 정보
        private Long productId;
        private Long couponId;
        private Long orderId;
        private Integer quantity;
        private Integer unitPrice;
        private Integer totalPrice;
        private Integer discountRate;

        // 주문 항목 정보
    }
}