package com.avengers.musinsa.domain.user.repository;

import com.avengers.musinsa.domain.user.dto.*;
import com.avengers.musinsa.mapper.CartMapper;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final CartMapper cartMapper;

    public List<ProductsInCartInfoResponse> getProductsInCart(Long userId) {
        return cartMapper.getProductsInCart(userId);
    }

    public ProductOptionInfo productOptionInfo(Long userId, Long productId, Map<Integer, String> productOptions) {
        return cartMapper.productOptionInfo(userId, productId, productOptions);
    }

    public void updateProductOption(Long userCartId, String productOptionName,
                                    Integer quantity) {
        cartMapper.updateProductOption(userCartId, productOptionName, quantity);
    }

    // userId와 productVariantId로 이전에 장바구니를 추가했는지 확인하는 메서드
    public CartItemDto findCartItemByVariantId(Long userId, AddCartRequest request) {
        return cartMapper.findCartItemByVariantId(userId, request);
    }

    //
    public void updateCartItemQuantity(Long cartId, int newQuantity) {
        cartMapper.updateCartItemQuantity(cartId, newQuantity);
    }

    public void insertNewCartItem(Long userId, AddCartRequest request) {
        cartMapper.insertNewCartItem(userId, request);
    }

    public void deleteCartItems(Long userId, List<Long> cartIds) {
        cartMapper.deleteCartItems(userId, cartIds);
    }
}
