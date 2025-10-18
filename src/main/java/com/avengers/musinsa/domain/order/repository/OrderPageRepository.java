package com.avengers.musinsa.domain.order.repository;

import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;

import java.util.List;

public interface OrderPageRepository {
    OrderPageResponse.BuyerInfo getBuyerInfo(Long userId);

    OrderPageResponse.BuyerInfo.DefaultAddress getDefaultAddress(Long userId);

    List<OrderPageResponse.OrderProductInfo> getProductsFromCart(List<Long> cartItemIds);

    OrderPageResponse.OrderProductInfo getDirectPurchaseProduct(Long productId, Integer quantity, Long productVariantId);


    OrderPageResponse.UserMileageInfo getUserMileageInfo(Long userId);



}
