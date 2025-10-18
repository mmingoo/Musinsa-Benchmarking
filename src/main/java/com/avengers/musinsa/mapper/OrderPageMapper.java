package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface OrderPageMapper {
    // 주문자 정보 조회
    OrderPageResponse.BuyerInfo getBuyerInfo(@Param("userId") Long userId);

    // 기본 배송지 조회
    OrderPageResponse.BuyerInfo.DefaultAddress getDefaultAddress(@Param("userId") Long userId);

    // 장바구니에서 상품 정보 조회
    List<OrderPageResponse.OrderProductInfo> getProductsFromCart(@Param("cartItemIds") List<Long> cartItemIds);

    // 직접 구매 상품 정보 조회
    OrderPageResponse.OrderProductInfo getDirectPurchaseProduct(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity,
            @Param("productVariantId") Long productVariantId
    );


    // 사용자 적립금 정보 조회
    OrderPageResponse.UserMileageInfo getUserMileageInfo(@Param("userId") Long userId);

}
