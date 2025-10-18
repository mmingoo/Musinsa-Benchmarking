package com.avengers.musinsa.domain.product.dto.response;

import com.avengers.musinsa.domain.product.entity.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductDetailResponse {
    private Long productId;
    private String productName;
    private int productLikeCnt;
    private int price;
    private int brandDiscount;
    private int finalprice;
    private String detailSizeImage;

    private Boolean isProductLiked;  // 상품 좋아요 여부
    private Boolean isBrandLiked;    // 브랜드 좋아요 여부

    private Long brandId;
    private String brandName;
    private int brandLikeCnt;
    private String brandImage;

    private List<ProductImage> productImageList = new ArrayList<>();

    private Long sizeDetailImageId;
    private String productSizeDetailImageURL;

}
