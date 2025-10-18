package com.avengers.musinsa.domain.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionUpdateRequest {

    private List<OptionGroup> optionGroups;

    private Integer quantity;

    @Getter
    @AllArgsConstructor
    public static class OptionGroup {
        // 1: color, 2: size, 3: material
        private Integer optionTypeId;

        private String optionValue;
    }
}
