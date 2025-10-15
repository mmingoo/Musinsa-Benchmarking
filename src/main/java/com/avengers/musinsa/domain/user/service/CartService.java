package com.avengers.musinsa.domain.user.service;

import com.avengers.musinsa.domain.product.service.ProductService;
import com.avengers.musinsa.domain.user.dto.*;
import com.avengers.musinsa.domain.product.service.ProductServiceImpl;
import com.avengers.musinsa.domain.user.dto.ProductOptionInfo;
import com.avengers.musinsa.domain.user.dto.ProductOptionUpdateRequest;
import com.avengers.musinsa.domain.user.dto.ProductsInCartInfoResponse;
import com.avengers.musinsa.domain.user.repository.CartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.avengers.musinsa.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    // 상품 상세 화면에서 장바구니 추가 POST Request
    @Transactional
    public void addProductUserCart(Long userId, AddCartRequest request) {
        // 3. 핵심 비즈니스 로직: 상품 존재 여부 확인
        CartItemDto existingItem = cartRepository.findCartItemByVariantId(userId, request);

        if (existingItem != null) {
            // 같은 상품, 같은 옵션, 같은 수량일 경우
            if (existingItem.getQuantity().equals(request.getQuantity())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "DUPLICATE_ITEM");
            }
            // 4. 수량만 다를 경우, 기존 수량 업데이트
            cartRepository.updateCartItemQuantity(existingItem.getCartId(), request.getQuantity());
        } else {
            // 5. 상품이 없으면: 새로 추가
            cartRepository.insertNewCartItem(userId, request);
        }
    }

    public List<ProductsInCartInfoResponse> getProductsInCart(Long userId) {
        // 장바구니에 담은 상품 가져옴
        List<ProductsInCartInfoResponse> productsInCart = cartRepository.getProductsInCart(userId);

        // 장바구니에서 가져온 상품들의 ID를 리스트에 저장
        List<Long> productIds = productsInCart.stream().map(ProductsInCartInfoResponse::getProductId).toList();

        // 상품 ID들의 옵션 그룹 정보를 가져와 상품 ID 별로 Map에 저장
        Map<Long, List<ProductsInCartInfoResponse.OptionGroup>> groupsMap = productService
                .getGroupsByProductIds(productIds);

        for (var group : productsInCart) {
            group.attachGroups(groupsMap.getOrDefault(group.getProductId(),
                    List.of()));
        }

        return productsInCart;
    }

    public List<ProductsInCartInfoResponse> updateProductOption(Long userId, Long productId,
                                                                ProductOptionUpdateRequest productOptionUpdateRequest) {
        // 해당 상품의 변경되는 옵션과 값 담기
        Map<Integer, String> productOptions = new HashMap<>();
        for (int i = 0; i < productOptionUpdateRequest.getOptionGroups().size(); i++) {
            productOptions.put(productOptionUpdateRequest.getOptionGroups().get(i).getOptionTypeId(),
                    productOptionUpdateRequest.getOptionGroups().get(i).getOptionValue());
        }

        // 변경하려는 재고 값
        Integer updateQuantity = productOptionUpdateRequest.getQuantity();

        // 변경하고자 하는 상품 옵션 명과 해당 상품의 남은 재고 가져오기
        ProductOptionInfo productOptionInfo = cartRepository.productOptionInfo(userId, productId, productOptions);

        // 변경하려는 재고값이 남은 재고보다 클 경우 예외처리
        Integer availableQuantity = productOptionInfo.getQuantity();
        if (updateQuantity > availableQuantity) {
            String message = "최대 " + availableQuantity + "개의 상품만 구매할 수 있습니다.";
            throw new ResponseStatusException(HttpStatus.CONFLICT, "QUANTITY_4091" + message);
        }
        String newOptionName = productOptionInfo.getOptionName();

        // 동일 옵션 중복 여부 확인
        List<ProductsInCartInfoResponse> cartItems = cartRepository.getProductsInCart(userId);

        // 현재 수정하려는 장바구니 항목의 userCartId 찾기
        Long userCartId = cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .map(ProductsInCartInfoResponse::getUserCartId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니 항목을 찾을 수 없습니다."));

        Optional<ProductsInCartInfoResponse> duplicate = cartItems.stream()
                .filter(item -> !item.getUserCartId().equals(userCartId)) // 현재 수정 대상 제외
                .filter(item -> item.getOptionName().equals(newOptionName)) // 동일 옵션 체크
                .findFirst();

        if (duplicate.isPresent()) {
            // 장바구니 내 기존 동일 옵션 항목 삭제
            cartRepository.deleteCartItems(userId, List.of(duplicate.get().getUserCartId()));
            // 수정 대상 항목은 업데이트
            cartRepository.updateProductOption(userCartId, newOptionName, updateQuantity);
        } else {
            // 중복 없으면 수정 대상 업데이트
            cartRepository.updateProductOption(userCartId, newOptionName, updateQuantity);
        }
        // 5. 최신 장바구니 반환
        return getProductsInCart(userId);
    }

    @Transactional
    public void deleteCartItems(Long userId, List<Long> cartIds) {
        cartRepository.deleteCartItems(userId, cartIds);
    }
}
