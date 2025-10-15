package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.product.dto.response.*;
import com.avengers.musinsa.domain.product.dto.search.SearchResponse;
import com.avengers.musinsa.domain.product.entity.Gender;
import com.avengers.musinsa.domain.product.dto.ProductOptionRow;
import com.avengers.musinsa.domain.review.dto.Request.RequestReview;
import java.util.List;
import com.avengers.musinsa.domain.product.entity.Product;
import com.avengers.musinsa.domain.product.entity.ProductCategory;
import com.avengers.musinsa.domain.product.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ProductMapper {
    List<Product> findAllProducts();

    ProductDetailResponse findProductById(@Param("productId") Long productId, @Param("userId") Long userId);

    List<RecommendationResponse> getRecommendationProductList(@Param("gender") Gender gender, @Param("userId") Long userId);

    List<ProductOptionRow> findOptionsByProductIds(@Param("productIds") List<Long> productIds);

    List<ProductByCategoryResponse> getProductsByCategory(@Param("categoryId") Long categoryId, @Param("userId") Long userId, @Param("sortBy") String sortBy, @Param("offset") int offset, @Param("limit") int limit);

    // 커서 기반 페이지네이션
    List<ProductByCategoryResponse> getProductsByCategoryCursor(@Param("categoryId") Long categoryId, @Param("userId") Long userId, @Param("sortBy") String sortBy, @Param("lastId") Long lastId, @Param("lastValue") Integer lastValue, @Param("size") int size);

    List<ProductImage> findProductImageById(Long productId);

    ProductVariantsResponse getProductOption(Long productId);

    List<String> findProductOptionColors(Long productId);

    List<String> findProductOptionMaterials(Long productId);

    List<String> findProductOptionSizes(Long productId);

    // 상품 리뷰 목록 조회
    List<ProductReviewsResponse> getProductReviews(Long productId);

    // 상의 실측 사이즈 조회
    List<TopProductDetailSizeListResponse> getTopProductDetailSizeList(Long productId);

    // 하의 실측 사이즈 조회
    List<BottomProductDetailSizeListResponse> getBottomProductDetailSizeList(Long productId);

    // 상품 상세 페이지 카테고리 조회
    ProductCategoryListResponse getProductCategories(Long productId);

    // 상품 상세 페이지 카테고리 리스트 조회
    List<ProductCategory> getProductCategoriesList(Long productId);

    ProductDetailDescriptionResponse getProductDetailDescription(Long productId);

    List<SearchResponse.ProductInfo> findProductsByBrandId(@Param("brandId") Long brandId, @Param("userId") Long userId, @Param("sortBy") String sortBy, @Param("offset") int offset, @Param("limit") int limit);

    List<SearchResponse.ProductInfo> findProductsByKeyword(@Param("keywords") String[] keywords, @Param("userId") Long userId, @Param("sortBy") String sortBy, @Param("offset") int offset, @Param("limit") int limit);

    // 커서 기반 검색 (브랜드)
    List<SearchResponse.ProductInfo> findProductsByBrandIdCursor(@Param("brandId") Long brandId, @Param("userId") Long userId, @Param("sortBy") String sortBy, @Param("lastId") Long lastId, @Param("lastValue") Integer lastValue, @Param("size") int size);

    // 커서 기반 검색 (키워드)
    List<SearchResponse.ProductInfo> findProductsByKeywordCursor(@Param("keywords") String[] keywords, @Param("userId") Long userId, @Param("sortBy") String sortBy, @Param("lastId") Long lastId, @Param("lastValue") Integer lastValue, @Param("size") int size);

    void saveSearchKeywordLog(String keyword);

    void saveSearchBrandLog(String Brand);

    //상품 좋아요 토글
    //좋아요 상태(null, 0,1) 반환하기
    UserProductStatus getUserProductStatus(Long userId, Long productId);

    //user_product_like 테이블에 레코드 추가
    void insertUserProductLike(Long userId, Long productId);

    //products 테이블의 좋아요 수+1
    void plusProductLikeCnt(Long productId);

    //레코드 추가 후 회원과 상품의 현재 좋아요 상태를 반환
    ProductLikeResponse getIsLikedProduct(Long userId, Long productId);

    //liked 컬럼을 0 ↔ 1
    void switchProductLike(Long userId, Long productId);

    //products 테이블의 좋아요 수 -1
    void minusProductLikeCnt(Long productId);

    List<ProductVariantDetailDto> findVariantDetailsByProductId(Long productId);

    void createProductReview(Long productId, Long userId, @Param("request") RequestReview requestReview);

    void updateProductReview(Long reviewId, @Param("request") RequestReview requestReview);

    void deleteProductReview(Long reviewId);
}