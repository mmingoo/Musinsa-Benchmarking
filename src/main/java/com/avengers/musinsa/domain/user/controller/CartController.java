package com.avengers.musinsa.domain.user.controller;

import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import com.avengers.musinsa.domain.user.auth.oauth2.dto.CustomOAuth2User;
import com.avengers.musinsa.domain.user.dto.AddCartRequest;
import com.avengers.musinsa.domain.user.dto.AddProductUserCartResponse;
import com.avengers.musinsa.domain.user.dto.ProductOptionUpdateRequest;
import com.avengers.musinsa.domain.user.dto.ProductsInCartInfoResponse;
import com.avengers.musinsa.domain.user.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final TokenProviderService tokenProviderService;

    // 상품 상세 화면에서 장바구니 추가 POST Request
    @PostMapping("/carts")
    public void addProductUserCart(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody AddCartRequest addCartRequest) {

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing");
        } else {
            Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            System.out.println(addCartRequest.getProductId());
            System.out.println(addCartRequest.getQuantity());
            System.out.println(addCartRequest.getVariantName());
            cartService.addProductUserCart(userId, addCartRequest);
        }
    }

    // 위 코드와 아래 코드는 같은데 postman용 userId를 url에 넣은 겁니다 나중엔 위에껄로 하시면 됩니다.
//    @PostMapping("/{userId}/carts")
//    public void addProductUserCart(
//            @PathVariable Long userId,
//            @RequestBody AddCartRequest addCartRequest) {
//            cartService.addProductUserCart(userId,addCartRequest);
//    }
    @PostMapping("/{userId}/carts")
    public void addProductUserCart(
            @PathVariable Long userId,
            @RequestBody AddCartRequest addCartRequest) {
        cartService.addProductUserCart(userId, addCartRequest);
    }


    @GetMapping("/carts")
    public List<ProductsInCartInfoResponse> productsInCart(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);

        return cartService.getProductsInCart(userId);
    }

    @PatchMapping("/carts/{productId}")
    public List<ProductsInCartInfoResponse> updateProductInCart(
            @PathVariable Long productId,
            @CookieValue(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody ProductOptionUpdateRequest productOptionUpdateRequest) {

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return cartService.updateProductOption(userId, productId, productOptionUpdateRequest);
    }

    //헤더 장바구니 갯수 뱃지
    @GetMapping("/carts/count")
    public Integer getCartItemCount(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return 0;
        }
        try {
            Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            List<ProductsInCartInfoResponse> cartItems = cartService.getProductsInCart(userId);
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    //장바구니 아이템 삭제
    @DeleteMapping("/carts")
    public void deleteCartItems(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody List<Long> cartIds) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing");
        }
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        cartService.deleteCartItems(userId, cartIds);
    }

}
