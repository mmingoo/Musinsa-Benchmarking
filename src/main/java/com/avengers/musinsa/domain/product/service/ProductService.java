package com.avengers.musinsa.domain.product.service;

import com.avengers.musinsa.domain.product.dto.response.*;
import com.avengers.musinsa.domain.product.dto.search.SearchResponse;
import com.avengers.musinsa.domain.product.entity.Gender;
import com.avengers.musinsa.domain.review.dto.Request.RequestReview;
import com.avengers.musinsa.domain.user.dto.ProductsInCartInfoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface ProductService {

    ProductDetailResponse getProductById(Long productId, Long userId);

    //ProductVariantsResponse getProductVariants(Long productId);

    // productId로 productVariantId 찾기
    List<ProductVariantDetailDto> getProductVariants(Long productId);

    List<RecommendationResponse> getRecommendationProductList(Gender gender, Long userId);


    Map<Long, List<ProductsInCartInfoResponse.OptionGroup>> getGroupsByProductIds(List<Long> productIds);

    // 상품 리뷰 목록 조회
    List<ProductReviewsResponse> getProductReviews(Long productId);

    // 상품 리뷰 작성
    void createProductReview(Long productId, Long userId, RequestReview requestReview);

    void updateProductReview(Long reviewId, RequestReview requestReview);

    void deleteProductReview(Long reviewId);

    // 상품상세 사이즈 리스트 조회
    Object getProductDetailSizeList(Long productId, Long userId);

    // 상품 상세 설명 조회 api
    ProductDetailDescriptionResponse getProductDetailDescription(Long productId);


    List<ProductByCategoryResponse> getProductsByCategory(Long categoryId, Long userId, String sortBy, int page, int size);

    // 커서 기반 페이지네이션
    List<ProductByCategoryResponse> getProductsByCategoryCursor(Long categoryId, Long userId, String sortBy, Long lastId, Integer lastValue, int size);


    // 상품 상세 페이지 카테고리 조회
    ProductCategoryListResponse getProductCategories(Long productId);

    // 상품 검색
    SearchResponse searchProducts(String keyword, Long userId, String sortBy, int page, int size);

    // 상품 검색 (커서 기반)
    SearchResponse searchProductsCursor(String keyword, Long userId, String sortBy, Long lastId, Integer lastValue, int size);

    //상품 좋아요 토글
    ProductLikeResponse ProductLikeToggle(Long userId, Long productId);

    ProductLikeResponse ProductLikeToggleByLock(Long userId, Long productId);

    ProductLikeResponse ProductLikeToggleByRMW(Long userId, Long productId);
}