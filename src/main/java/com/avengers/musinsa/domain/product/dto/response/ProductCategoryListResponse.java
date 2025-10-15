package com.avengers.musinsa.domain.product.dto.response;

import com.avengers.musinsa.domain.product.entity.ProductCategory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductCategoryListResponse {
    private Long productId;
    private String productName;


    private List<ProductCategory> productCategoryList = new ArrayList<>();
}
