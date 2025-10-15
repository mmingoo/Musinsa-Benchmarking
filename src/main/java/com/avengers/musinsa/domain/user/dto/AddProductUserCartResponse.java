package com.avengers.musinsa.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class AddProductUserCartResponse {
    private Long cartId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer productPrice;
    private Integer totalPrice;

    private Long productVariantId;
    private String productVariantName;

    private List<OptionGroup> optionGroups = new ArrayList<>();

    public static class OptionGroup {
        private String optionType;
        private List<String> optionValues;
    }
}
