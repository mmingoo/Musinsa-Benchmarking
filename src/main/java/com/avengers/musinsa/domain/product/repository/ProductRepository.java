package com.avengers.musinsa.domain.product.repository;

import com.avengers.musinsa.domain.product.dto.ProductOptionRow;
import com.avengers.musinsa.domain.product.dto.response.BottomProductDetailSizeListResponse;
import com.avengers.musinsa.domain.product.dto.response.ProductByCategoryResponse;
import com.avengers.musinsa.domain.product.dto.response.ProductCategoryListResponse;
import com.avengers.musinsa.domain.product.dto.response.ProductDetailDescriptionResponse;
import com.avengers.musinsa.domain.product.dto.response.ProductDetailResponse;
import com.avengers.musinsa.domain.product.dto.response.ProductLikeResponse;
import com.avengers.musinsa.domain.product.dto.response.ProductReviewsResponse;
import com.avengers.musinsa.domain.product.dto.response.ProductVariantsResponse;
import com.avengers.musinsa.domain.product.dto.response.RecommendationResponse;
import com.avengers.musinsa.domain.product.dto.response.TopProductDetailSizeListResponse;
import com.avengers.musinsa.domain.product.dto.response.UserProductStatus;
import com.avengers.musinsa.domain.product.dto.response.*;
import com.avengers.musinsa.domain.product.dto.search.SearchResponse;
import com.avengers.musinsa.domain.product.entity.Gender;
import com.avengers.musinsa.domain.product.entity.Product;
import com.avengers.musinsa.domain.product.entity.ProductCategory;
import com.avengers.musinsa.domain.product.entity.ProductImage;
import com.avengers.musinsa.domain.review.dto.Request.RequestReview;
import java.util.List;

public interface ProductRepository {

    // 모든 상품을 조회하는 메서드, 메서드를 호출하면 ProductMapper의 findAllProducts()메소드를 호출해서 반환
    List<Product> findAllProducts();

    // 상품 ID를 받아서 특정 상품 하나를 조회하는 메서드
    ProductDetailResponse findProductById(Long productId, Long userId);

    ProductVariantsResponse getProductOption(Long productId);

    List<String> findProductOptionColors(Long productId);

    List<String> findProductOptionMaterials(Long productId);

    List<String> findProductOptionSizes(Long productId);

    List<RecommendationResponse> getRecommendationProductList(Gender gender, Long userId);

    List<ProductOptionRow> findOptionRowsByProductId(List<Long> productIds);

    List<ProductByCategoryResponse> getProductsByCategory(Long categoryId, Long userId, String sortBy, int offset, int limit);

    // 커서 기반 페이지네이션
    List<ProductByCategoryResponse> getProductsByCategoryCursor(Long categoryId, Long userId, String sortBy, Long lastId, Integer lastValue, int size);


    List<ProductImage> findProductImageById(Long productId);

    // 상품 리뷰 목록 조회
    List<ProductReviewsResponse> getProductReviews(Long productId);

    // 상의 실측 사이즈 조회
    List<TopProductDetailSizeListResponse> getTopProductDetailSizeList(Long productId);

    // 하의 실측 사이즈 조회
    List<BottomProductDetailSizeListResponse> getBottomProductDetailSizeList(Long productId);

    ProductCategoryListResponse getProductCategories(Long productId);

    // 상품 상세 페이지 카테고리 리스트 조회
    List<ProductCategory> getProductCategoriesList(Long productId);

    // 상품 상세 설명 조회 api
    ProductDetailDescriptionResponse getProductDetailDescription(Long productId);

    List<SearchResponse.ProductInfo> findProductsByBrandId(Long brandId, Long userId, String sortBy, int offset, int limit);

    List<SearchResponse.ProductInfo> findProductsByKeyword(String[] keywords, Long userId, String sortBy, int offset, int limit);

    // 커서 기반 검색 (브랜드)
    List<SearchResponse.ProductInfo> findProductsByBrandIdCursor(Long brandId, Long userId, String sortBy, Long lastId, Integer lastValue, int size);

    // 커서 기반 검색 (키워드)
    List<SearchResponse.ProductInfo> findProductsByKeywordCursor(String[] keywords, Long userId, String sortBy, Long lastId, Integer lastValue, int size);

    //검색 시 검색어 로그 테이블에 검색 정보 저장하기.
    void saveSearchKeywordLog(String keyword);

    //검색 시 브랜드 로그 테이블에 검색 정보 저장하기.
   // void saveSearchBrandLog(String brand);


    //상품 좋아요 토글
//좋아요 상태(null, 0,1) 반환하기
    UserProductStatus getUserProductStatus(Long userId, Long productId);

    //user_product_like 테이블에 레코드 추가
    void insertUserProductLike(Long userId, Long productId);

    //products 테이블의 좋아요 수 +1
    void plusProductLikeCnt(Long productId);

    //레코드 추가 후 회원과 상품의 현재 좋아요 상태를 반환
    ProductLikeResponse getIsLikedProduct(Long userId, Long productId);

    //liked 컬럼을 0 ↔ 1
    void switchProductLike(Long userId, Long productId);

    //products 테이블의 좋아요 수 -1
    void minusProductLikeCnt(Long productId);

    List<ProductVariantDetailDto> findVariantDetailsByProductId(Long productId);

    void createProductReview(Long productId, Long userId, RequestReview requestReview);

    void updateProductReview(Long reviewId, RequestReview requestReview);

    void deleteProductReview(Long reviewId);
}

