package com.avengers.musinsa.domain.product.entity;

import com.avengers.musinsa.domain.brand.entity.Brand;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
//@Builder
@RequiredArgsConstructor
public class Product {
    private Long productId;

    private Brand brand;
    private Long brandId;

    private ProductCategory productCategory;
    private Long productCategoryId;

    private List<ProductImage> productImageList;
    private Long productImageId;

    private String productName;
    private String detailDescription;
    private Integer price;
    private Gender gender;
    private Integer productLikes;
    private Timestamp createdAt;

    private SizeDetailImage sizeDetailImage;
    private String sizeDetailImageName;
    private String sizeDetailImageUrl;

    public void addProductImage(ProductImage productImage){
        if(this.productImageList == null){
            this.productImageList = new ArrayList<>();
        }
    }
    public void removeProductImage(ProductImage productImage){
        if(this.productImageList != null){
            this.productImageList.remove(productImage);
            productImage.setProduct(null); // 관계 제거
        }
    }

}
