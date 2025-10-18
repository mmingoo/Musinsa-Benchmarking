package com.avengers.musinsa.domain.order.repository;

import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;
import com.avengers.musinsa.mapper.OrderPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderPageRepositoryImpl implements OrderPageRepository{
    private final OrderPageMapper orderPageMapper;

    //주문자 정보 조회
    @Override
    public OrderPageResponse.BuyerInfo getBuyerInfo(Long userId) {
        return orderPageMapper.getBuyerInfo(userId);
    }

    // 회원의 기본 배송지 조회
    @Override
    public OrderPageResponse.BuyerInfo.DefaultAddress getDefaultAddress(Long userId) {
        System.out.println("리포지토리에서 아이디 출력 : " + userId);
        return orderPageMapper.getDefaultAddress(userId);
    }

    @Override
    public List<OrderPageResponse.OrderProductInfo> getProductsFromCart(List<Long> cartItemIds) {
        return orderPageMapper.getProductsFromCart(cartItemIds);
    }

    @Override
    public OrderPageResponse.OrderProductInfo getDirectPurchaseProduct(Long productId, Integer quantity, Long productVariantId) {
        return orderPageMapper.getDirectPurchaseProduct(productId,quantity,productVariantId );
    }


    @Override
    public OrderPageResponse.UserMileageInfo getUserMileageInfo(Long userId) {
        return orderPageMapper.getUserMileageInfo(userId);
    }

}
