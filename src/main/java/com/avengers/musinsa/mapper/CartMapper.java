package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.user.dto.*;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {
    List<ProductsInCartInfoResponse> getProductsInCart(Long userId);

    ProductOptionInfo productOptionInfo(Long userId, Long productId, Map<Integer, String> productOptions);

    void updateProductOption(Long userCartId, String productOptionName, Integer quantity);

    CartItemDto findCartItemByVariantId(@Param("userId") Long userId, @Param("request") AddCartRequest request);

    void insertNewCartItem(@Param("userId") Long userId, @Param("request") AddCartRequest request);

    void updateCartItemQuantity(@Param("cartId") Long cartId, @Param("newQuantity") int newQuantity);

    void deleteCartItems(@Param("userId") Long userId, @Param("cartIds") List<Long> cartIds);
}
