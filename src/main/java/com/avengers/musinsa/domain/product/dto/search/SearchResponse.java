package com.avengers.musinsa.domain.product.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SearchResponse {
    private String searchKeyword;
    private BrandInfo brandInfo;
    private Integer totalCount;
    private List<ProductInfo> products;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BrandInfo {
        private Long brandId;
        private String brandNameKr;
        private String brandNameEn;
        private String brandImage;
        private Integer brandLikes;
        private Integer totalCount;
        private List<ProductInfo> products;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductInfo {
        private Long brandId;
        private String brandNameKr;
        private String brandNameEn;
        private Long productId;
        private String productImage;
        private String productName;
        private Integer price;
        private Integer productLikes;
        private Double ratingAverage;
        private Integer reviewCount;
        private Boolean isLiked;  // 좋아요 여부
    }
}