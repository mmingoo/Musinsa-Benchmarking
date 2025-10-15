package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.request.OrderPageRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;
import com.avengers.musinsa.domain.order.repository.OrderPageRepository;
import com.avengers.musinsa.mapper.OrderPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderPageServiceImpl implements OrderPageService{
    private final OrderPageMapper orderPageMapper;
    private final OrderPageRepository orderPageRepository;

    // 베이지에 주문에 관한 모든 값들 조회
    @Override
    public OrderPageResponse getOrderPageInfo(Long userId, OrderPageRequest request) {
        // 1. 주문자 정보 조회
        OrderPageResponse.BuyerInfo buyerInfo = getBuyerInfo(userId);

        // 2. 상품 정보 조회 (장바구니 or 직접구매)
        List<OrderPageResponse.OrderProductInfo> products = getOrderProducts(request);


        // 4. 적립금 정보 조회
        OrderPageResponse.UserMileageInfo mileageInfo = getUserMileageInfo(userId);


        return OrderPageResponse.builder()
                .buyerInfo(buyerInfo)
                .products(products)
                .mileageInfo(mileageInfo)
                .build();
    }

    // 주문자 정보 조회
    private OrderPageResponse.BuyerInfo getBuyerInfo(Long userId) {
        OrderPageResponse.BuyerInfo buyerInfo = orderPageRepository.getBuyerInfo(userId);
        OrderPageResponse.BuyerInfo.DefaultAddress defaultAddress = orderPageRepository.getDefaultAddress(userId);
        System.out.println(defaultAddress.getAddress());
        System.out.println(defaultAddress.getAddressId());
        System.out.println(defaultAddress.getDetailAddress());
        return OrderPageResponse.BuyerInfo.builder()
                .name(buyerInfo.getName())
                .phone(buyerInfo.getPhone())
                .email(buyerInfo.getEmail())
                .defaultAddress(defaultAddress)
                .build();
    }

    private List<OrderPageResponse.OrderProductInfo> getOrderProducts(OrderPageRequest request) {
        if ("FROM_CART".equals(request.getType())) {
            return orderPageRepository.getProductsFromCart(request.getCartItemIds());
        } else if ("DIRECT_PURCHASE".equals(request.getType())) {
            OrderPageResponse.OrderProductInfo product = orderPageRepository.getDirectPurchaseProduct(
                    request.getProductId(),
                    request.getQuantity(),
                    request.getProductVariantId()
            );
            return List.of(product);
        }
        throw new IllegalArgumentException("Invalid request type: " + request.getType());
    }


    private OrderPageResponse.UserMileageInfo getUserMileageInfo(Long userId) {
        OrderPageResponse.UserMileageInfo mileageInfo = orderPageRepository.getUserMileageInfo(userId);
        return mileageInfo;
    }

}
