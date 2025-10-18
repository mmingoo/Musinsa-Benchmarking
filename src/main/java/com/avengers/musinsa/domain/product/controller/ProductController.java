package com.avengers.musinsa.domain.product.controller;

import com.avengers.musinsa.domain.product.dto.response.*;
import com.avengers.musinsa.domain.product.dto.search.SearchResponse;
import com.avengers.musinsa.domain.product.entity.Gender;
import com.avengers.musinsa.domain.product.service.ProductService;
import com.avengers.musinsa.domain.review.dto.Request.RequestReview;
import java.util.List;

import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController // 이 클래스는 API용 컨트롤러라는 애너테이션
@RequestMapping("/api/v1/products") // 이 컨트롤러의 모든 API는 /api/v1/products로 시작
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final TokenProviderService tokenProviderService;

    // "해당 productId 상품 정보를 반환해준다.
    @GetMapping("{productId}")
    public ProductDetailResponse getDetailProduct(@PathVariable Long productId,
                                                  @CookieValue(value = "Authorization", required = false) String authorizationHeader) {

        Long userId = null;
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            try {
                userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 비로그인으로 처리
                userId = null;
            }
        }

        return productService.getProductById(productId, userId);
    }

    @GetMapping("/{productId}/options")
    public List<ProductVariantDetailDto> getProductVariants(@PathVariable Long productId) {
        return productService.getProductVariants(productId);
    }

    // 상풍 상세 페이지 카테고리 조회
    @GetMapping("/{productId}/categories")
    public ProductCategoryListResponse getProductCategories(@PathVariable Long productId) {
        return productService.getProductCategories(productId);
    }


    // 무신사 추천순
    @GetMapping("/recommendations/{gender}")
    public List<RecommendationResponse> recommendationProducts(@PathVariable String gender,
                                                                @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        Gender g = Gender.valueOf(gender.toUpperCase());
        Long userId = null;
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            try {
                userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            } catch (Exception e) {
                userId = null;
            }
        }
        return productService.getRecommendationProductList(g, userId);
    }

    //카테고리 선택 시 상품 목록 조회되는 화면
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductByCategoryResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "sortBy", required = false, defaultValue = "LIKE") String sortBy,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "lastValue", required = false) Integer lastValue,
            @RequestParam(value = "size", required = false, defaultValue = "12") int size,
            @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        System.out.println("category_id = " + categoryId);
        System.out.println("sortBy = " + sortBy);
        System.out.println("lastId = " + lastId + ", lastValue = " + lastValue);

        Long userId = null;
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            try {
                userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            } catch (Exception e) {
                userId = null;
            }
        }
        List<ProductByCategoryResponse> products = productService.getProductsByCategoryCursor(
                categoryId, userId, sortBy, lastId, lastValue, size);
        return ResponseEntity.ok(products);
    }

    // 상품 리뷰 목록 조회
    @GetMapping("{productId}/reviews")
    public List<ProductReviewsResponse> getProductReviews(@PathVariable Long productId) {
        return productService.getProductReviews(productId);
    }

    // 상품 리뷰 작성
    @PostMapping("{productId}/reviews/create")
    public ResponseEntity<?> createProductReview(@PathVariable Long productId,
                                                 @CookieValue(value = "Authorization", required = false) String authorizationHeader,
                                                 @RequestBody RequestReview requestReview) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        productService.createProductReview(productId, userId, requestReview);
        return ResponseEntity.ok().build();
    }

    // 상품 리뷰 수정
    @PatchMapping("reviews/{reviewId}/update")
    public ResponseEntity<?> updateProductReview(@PathVariable Long reviewId,
                                                 @CookieValue(value = "Authorization", required = false) String authorizationHeader,
                                                 @RequestBody RequestReview requestReview) {
        tokenProviderService.getUserIdFromToken(authorizationHeader);
        productService.updateProductReview(reviewId, requestReview);
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제 기능
    @DeleteMapping("reviews/{reviewId}/delete")
    public ResponseEntity<?> deleteProductReview(@PathVariable Long reviewId,
                                                 @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        tokenProviderService.getUserIdFromToken(authorizationHeader);
        productService.deleteProductReview(reviewId);
        return ResponseEntity.ok().build();
    }

    // 상품상세 사이즈 리스트 조회
    @GetMapping("{productId}/detail-size-list")
    public Object getProductDetailSizeList(@PathVariable Long productId,
                                           @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing");
        } else {
            Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);

            return productService.getProductDetailSizeList(productId, userId);
        }
    }

    // 상품 상세 설명 조회 api
    @GetMapping("/{productId}/detail-Info")
    public ProductDetailDescriptionResponse getProductDetailDescription(@PathVariable Long productId) {
        return productService.getProductDetailDescription(productId);

    }


    // 상품 검색에 따른 상품 목록 조회 (커서 기반 지원)
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam("keyword") String keyword,
                                            @RequestParam(value = "sortBy", required = false, defaultValue = "LIKE") String sortBy,
                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "size", required = false, defaultValue = "12") int size,
                                            @RequestParam(value = "lastId", required = false) Long lastId,
                                            @RequestParam(value = "lastValue", required = false) Integer lastValue,
                                            @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        System.out.println(keyword);
        System.out.println("sortBy = " + sortBy);
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);

        SearchResponse response;
        // 커서 값이 있으면 커서 기반, 없으면 OFFSET 기반
        if (lastId != null && lastValue != null) {
            response = productService.searchProductsCursor(keyword, userId, sortBy, lastId, lastValue, size);
        } else {
            response = productService.searchProducts(keyword, userId, sortBy, page, size);
        }

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok("검색 결과가 없습니다.");
        }

    }


    //상품 좋아요 토글 기능
    @PostMapping("/{productId}/liked")
    public ProductLikeResponse ProductLikeToggle(@PathVariable Long productId,
                                                 @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing");
        } else {
            Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            System.out.println("userId = " + userId);
            return productService.ProductLikeToggle(userId, productId);
        }
    }
}
