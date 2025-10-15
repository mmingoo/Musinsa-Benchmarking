package com.avengers.musinsa.domain.user.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.engine.jdbc.Size;

@Getter
public class ProductsInCartInfoResponse {
    private Long userCartId;
    private Long productId;
    private String productName;
    private Integer totalPrice;
    private String optionName;
    private Integer quantity;
    private String productBrand;
    private String imageUrl;

    private List<OptionGroup> optionGroups = new ArrayList<>();

    // ★ My Batis가 사용할 7-파라미터 생성자(SQL 컬럼 순서와 동일!)
    public ProductsInCartInfoResponse(
            Long userCartId,
            Long productId,
            String productName,
            Integer totalPrice,
            String optionName,
            Integer quantity,
            String productBrand,
            String imageUrl
    ) {
        this.userCartId = userCartId;
        this.productId = productId;
        this.productName = productName;
        this.totalPrice = totalPrice;
        this.optionName = optionName;
        this.quantity = quantity;
        this.productBrand = productBrand;
        this.imageUrl = imageUrl;
    }

    public void attachGroups(List<OptionGroup> optionGroups) {
        this.optionGroups = optionGroups;
    }

    @Getter
    @AllArgsConstructor
    public static class OptionGroup {
        private String optionType;
        private List<String> optionValues;
    }
}