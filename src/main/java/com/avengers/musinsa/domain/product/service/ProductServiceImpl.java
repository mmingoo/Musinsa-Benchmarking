package com.avengers.musinsa.domain.product.service;

import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.brand.repository.BrandRepository;
import com.avengers.musinsa.domain.product.dto.response.*;
import com.avengers.musinsa.domain.product.dto.search.SearchResponse;
import com.avengers.musinsa.domain.product.entity.ProductCategory;
import com.avengers.musinsa.domain.product.entity.ProductImage;
import com.avengers.musinsa.domain.product.entity.Gender;
import com.avengers.musinsa.domain.product.repository.ProductRepository;
import com.avengers.musinsa.domain.product.dto.ProductOptionRow;
import com.avengers.musinsa.domain.search.repository.PopularKeywordRepository;
import com.avengers.musinsa.domain.review.dto.Request.RequestReview;
import com.avengers.musinsa.domain.review.dto.ReviewMeta;
import com.avengers.musinsa.domain.review.repository.ReviewRepository;
import com.avengers.musinsa.domain.search.service.SearchLogService;
import com.avengers.musinsa.domain.user.dto.ProductsInCartInfoResponse;

import com.avengers.musinsa.mapper.ReviewMapper;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    // 의존성 주입
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ReviewRepository reviewRepository;
    private final SearchLogService searchLogService;
    private final PopularKeywordRepository popularKeywordRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ProductDetailResponse getProductById(Long productId, Long userId) {
        ProductDetailResponse productInfo = productRepository.findProductById(productId, userId);
        List<ProductImage> productImage = productRepository.findProductImageById(productId);
        productInfo.getProductImageList().addAll(productImage);
        return productInfo;
    }

    @Override
    public List<ProductVariantDetailDto> getProductVariants(Long productId) {
        return productRepository.findVariantDetailsByProductId(productId);
    }


    @Override
    public List<RecommendationResponse> getRecommendationProductList(Gender gender, Long userId) {
        return productRepository.getRecommendationProductList(gender, userId);
    }

    @Override
    public Map<Long, List<ProductsInCartInfoResponse.OptionGroup>> getGroupsByProductIds(List<Long> productIds) {
        // 상품 ID 검증
        if (productIds == null || productIds.isEmpty()) {
            return Map.of();
        }

        // 각 상품들의 옵션 가져오기
        List<ProductOptionRow> productOptionRows = productRepository.findOptionRowsByProductId(productIds);

        // Map<상품ID, Map<optionType, OptionGroupBuilder(옵션종류, 옵션종류에 대한 값들) >>
        Map<Long, Map<String, OptionGroupBuilder>> optionOfProducts = new HashMap<>();

        // 상품에 대한 옵션 순회
        for (ProductOptionRow productOptionRow : productOptionRows) {
            Long productId = productOptionRow.getProductId(); // 상품ID
            String optionType = productOptionRow.getOptionType();
            // productId에 대한 옵션 정보 가져오기
            Map<String, OptionGroupBuilder> groupMap = optionOfProducts.get(productId);
            // 없으면 optionOfProducts Map 생성 후 put
            if (groupMap == null) {
                groupMap = new HashMap<>();
                optionOfProducts.put(productId, groupMap);
            }

            // groupId에 대한 옵션 정보 빌더 가져오기
            OptionGroupBuilder builder = groupMap.get(optionType);
            // 없으면 groupMap 생성 후 put
            if (builder == null) {
                builder = new OptionGroupBuilder(productOptionRow.getOptionType());
                groupMap.put(optionType, builder);
            }
//            System.out.println(productOptionRow.getOptionType() + " " + productOptionRow.getValue());
            // 한 상품에 대해 옵션에 대한 값 추가
            builder.addValue(productOptionRow.getValue());
        }

        // productId -> List<OptionGroup>
        Map<Long, List<ProductsInCartInfoResponse.OptionGroup>> result = new HashMap<>();

        for (Map.Entry<Long, Map<String, OptionGroupBuilder>> entry : optionOfProducts.entrySet()) {
            List<ProductsInCartInfoResponse.OptionGroup> groups = new ArrayList<>();
            for (OptionGroupBuilder builder : entry.getValue().values()) {
                groups.add(builder.build());
            }
            result.put(entry.getKey(), groups);
        }
        return result;
    }

    // 상품 리뷰 목록 조회
    @Override
    public List<ProductReviewsResponse> getProductReviews(Long productId) {
        return productRepository.getProductReviews(productId);
    }

    @Transactional
    @Override
    public void createProductReview(Long productId, Long userId, RequestReview requestReview) {
        productRepository.createProductReview(productId, userId, requestReview);

        Integer rating = requestReview.getRating();

        reviewRepository.updateReviewStatus(1, productId, rating);
    }

    @Transactional
    @Override
    public void updateProductReview(Long reviewId, RequestReview requestReview) {
        // 수정 전 productId, 평점 가져오기
        ReviewMeta meta = reviewMapper.findReviewMetaById(reviewId);

        // 본문 수정
        productRepository.updateProductReview(reviewId, requestReview);

        // 차이 계산
        int oldRating = meta.getRating();
        int newRating = requestReview.getRating();

        if (newRating - oldRating != 0) {
            reviewRepository.updateReviewStatus(0, meta.getProductId(), newRating - oldRating);
        }
    }

    @Transactional
    @Override
    public void deleteProductReview(Long reviewId) {
        // 기존 productId, 평점 가져오기
        ReviewMeta meta = reviewMapper.findReviewMetaById(reviewId);

        // 삭제
        productRepository.deleteProductReview(reviewId);

        int oldRating = meta.getRating();
        reviewRepository.updateReviewStatus(-1, meta.getProductId(), -oldRating);
    }

    // 상품상세 사이즈 리스트 조회
    @Override
    public Object getProductDetailSizeList(Long productId, Long userId) {
        // 상품 정보 가져오기
        ProductDetailResponse product = productRepository.findProductById(productId, userId);

        // 상품 컬럼에서 sizeDetailImageId를 찾는다.
        Long sizeDetailImageId = product.getSizeDetailImageId();

        // 1, 2 면 상의 실측 사이즈 조회
        // 3, 4 면 하의 실측 사이즈 조회
        if (sizeDetailImageId == 1L || sizeDetailImageId == 2L) {
            return productRepository.getTopProductDetailSizeList(productId);
        } else if (sizeDetailImageId == 3L || sizeDetailImageId == 4L) {
            return productRepository.getBottomProductDetailSizeList(productId);
        } else {
            return Collections.emptyList();
        }
    }

    // 상품 상세 설명 조회 api
    @Override
    public ProductDetailDescriptionResponse getProductDetailDescription(Long productId) {
        return productRepository.getProductDetailDescription(productId);
    }

    // 내부 전용 빌더: 중복 제거 + 입력 순서 보존
    private static class OptionGroupBuilder {
        private final String optionType;
        private final LinkedHashSet<String> values = new LinkedHashSet<>();

        OptionGroupBuilder(String name) {
            this.optionType = name;
        }

        // 옵션에 대한 값 추가
        // ex) color라면 add("green"), add("blue"), ... 등등
        void addValue(String value) {
            if (value != null) {
                values.add(value);
            }
        }

        ProductsInCartInfoResponse.OptionGroup build() {
            return new ProductsInCartInfoResponse.OptionGroup(
                    optionType, List.copyOf(values)
            );
        }
    }

    @Override
    public List<ProductByCategoryResponse> getProductsByCategory(Long categoryId, Long userId, String sortBy, int page, int size) {
        log.info("카테고리 ID로 상품 조회 시작: {}", categoryId);
        log.info("정렬 기준: {}", sortBy);

        int offset = page * size;
        List<ProductByCategoryResponse> result = productRepository.getProductsByCategory(categoryId, userId, sortBy, offset, size);

        log.info("조회 결과 개수: {}", result != null ? result.size() : 0);
        log.debug("조회 결과: {}", result);

        return result;
    }

    @Override
    public List<ProductByCategoryResponse> getProductsByCategoryCursor(Long categoryId, Long userId, String sortBy, Long lastId, Integer lastValue, int size) {
        log.info("커서 기반 카테고리 상품 조회 - categoryId: {}, sortBy: {}, lastId: {}, lastValue: {}",
                 categoryId, sortBy, lastId, lastValue);

        List<ProductByCategoryResponse> result = productRepository.getProductsByCategoryCursor(
                categoryId, userId, sortBy, lastId, lastValue, size);

        log.info("조회 결과 개수: {}", result != null ? result.size() : 0);

        return result;
    }

    // 상품 상세 페이지 카테고리 조회
    @Override
    public ProductCategoryListResponse getProductCategories(Long productId) {
        ProductCategoryListResponse productCategoryListResponse = productRepository.getProductCategories(productId);
        List<ProductCategory> productCategoryList = productRepository.getProductCategoriesList(productId);

        productCategoryListResponse.getProductCategoryList().addAll(productCategoryList);
        return productCategoryListResponse;
    }


    // 상품 검색
    @Override
    public SearchResponse searchProducts(String keyword, Long userId, String sortBy, int page, int size) {

        String processedKeyword = preprocessKeyword(keyword);
        //searchLogService.saveSearchKeywordLog(keyword, userId);
        System.out.println("검색어 : " + processedKeyword);
        System.out.println("정렬 : " + sortBy);

        int offset = page * size;

        // 브랜드 검색 먼저 시도
        // 브랜드 두 개 검색될 경우도 고려하여 코드 작성
        List<BrandResponse> brandList = brandRepository.findByBrandName(processedKeyword);

        if (!brandList.isEmpty()) {
            // 브랜드 검색인 경우
            BrandResponse brand = brandList.getFirst();


            // 브랜드 상품 불러오기
            List<SearchResponse.ProductInfo> brandProducts =
                    productRepository.findProductsByBrandId(brand.getBrandId(), userId, sortBy, offset, size);

            SearchResponse.BrandInfo brandInfo = SearchResponse.BrandInfo.builder()
                    .brandId(brand.getBrandId())
                    .brandNameKr(brand.getBrandNameKr())
                    .brandNameEn(brand.getBrandNameEn())
                    .brandImage(brand.getBrandImage())
                    .brandLikes(brand.getBrandLikes())
                    .totalCount(brandProducts.size())
                    .products(brandProducts)
                    .build();

            return SearchResponse.builder()
                    .searchKeyword(keyword)
                    .brandInfo(brandInfo)
                    .build();
        } else {
            // 상품 검색인 경우, 키워드 저장
//            searchLogService.saveSearchKeywordLog(keyword, userId);
            String[] keywords = keyword.trim().split("\\s+");
            for (String key : keywords) {
                System.out.println("키워드 = " + key);
            }
            List<SearchResponse.ProductInfo> products =
                    productRepository.findProductsByKeyword(keywords, userId, sortBy, offset, size);

            if (!products.isEmpty()) {
                return SearchResponse.builder()
                        .searchKeyword(keyword)
                        .brandInfo(null)
                        .totalCount(products.size())
                        .products(products)
                        .build();
            } else {
                return null;
            }

        }
    }

    // 상품 검색 (커서 기반)
    @Override
    public SearchResponse searchProductsCursor(String keyword, Long userId, String sortBy, Long lastId, Integer lastValue, int size) {
        String processedKeyword = preprocessKeyword(keyword);
        System.out.println("커서 기반 검색어 : " + processedKeyword);
        System.out.println("정렬 : " + sortBy);

        // 브랜드 검색 먼저 시도
        List<BrandResponse> brandList = brandRepository.findByBrandName(processedKeyword);

        if (!brandList.isEmpty()) {
            // 브랜드 검색인 경우
            BrandResponse brand = brandList.getFirst();

            // 브랜드 상품 불러오기 (커서 기반)
            List<SearchResponse.ProductInfo> brandProducts =
                    productRepository.findProductsByBrandIdCursor(brand.getBrandId(), userId, sortBy, lastId, lastValue, size);

            SearchResponse.BrandInfo brandInfo = SearchResponse.BrandInfo.builder()
                    .brandId(brand.getBrandId())
                    .brandNameKr(brand.getBrandNameKr())
                    .brandNameEn(brand.getBrandNameEn())
                    .brandImage(brand.getBrandImage())
                    .brandLikes(brand.getBrandLikes())
                    .totalCount(brandProducts.size())
                    .products(brandProducts)
                    .build();

            return SearchResponse.builder()
                    .searchKeyword(keyword)
                    .brandInfo(brandInfo)
                    .build();
        } else {
            // 상품 검색인 경우 (커서 기반)
            String[] keywords = keyword.trim().split("\\s+");
            List<SearchResponse.ProductInfo> products =
                    productRepository.findProductsByKeywordCursor(keywords, userId, sortBy, lastId, lastValue, size);

            if (!products.isEmpty()) {
                return SearchResponse.builder()
                        .searchKeyword(keyword)
                        .brandInfo(null)
                        .totalCount(products.size())
                        .products(products)
                        .build();
            } else {
                return null;
            }
        }
    }

    private String preprocessKeyword(String keyword) {
        System.out.println(keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return keyword;
        }

        // 영어 문자가 포함되어 있는지 확인
        if (keyword.matches(".*[a-zA-Z].*")) {
            return keyword.toUpperCase();
        }
        return keyword;

    }

    public ProductLikeResponse ProductLikeToggle(Long userId, Long productId) {
        UserProductStatus status = productRepository.getUserProductStatus(userId, productId);
        //레코드가 없을 때
        if (status == null) {
            //user_product_like 테이블에 레코드 추가
            productRepository.insertUserProductLike(userId, productId);
            //brands 테이블 좋아요 수 +1
            productRepository.plusProductLikeCnt(productId);
            //레코드 추가 후 회원과 브랜드의 현재 좋아요 상태를 반환
            return productRepository.getIsLikedProduct(userId, productId);
        }
        //이미 좋아요 한 브랜드 좋아요 상태 바꾸기
        else {
            //liked 값 확인 (0 또는 1)
            Integer currentLiked = status.getLiked();
            //liked 컬럼을 0 ↔ 1
            productRepository.switchProductLike(userId, productId);
            //브랜드 테이블의 좋아요 수를 동기화
            if (currentLiked != null && currentLiked == 1) {
                productRepository.minusProductLikeCnt(productId);
            } else {
                productRepository.plusProductLikeCnt(productId);
            }
            //좋아요상태 변경 후 현재 좋아요 상태를 반환
            return productRepository.getIsLikedProduct(userId, productId);
        }
    }
}
