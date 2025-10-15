package com.avengers.musinsa.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantsResponse {
    private List<String> productOptionColor = new ArrayList<>();
    private List<String> productOptionMaterial= new ArrayList<>();
    private List<String> productOptionSize= new ArrayList<>();

}
