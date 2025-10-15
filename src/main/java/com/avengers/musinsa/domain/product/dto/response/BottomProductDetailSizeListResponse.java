package com.avengers.musinsa.domain.product.dto.response;

import com.avengers.musinsa.domain.product.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BottomProductDetailSizeListResponse {
    private Long BottomProductDetailSizeId;

    private Long sizeDetailImageId;

    private Size cm;
    private BigDecimal length;
    private BigDecimal waist;
    private BigDecimal hip;
    private BigDecimal thigh;
    private BigDecimal rise;
    private BigDecimal hemWidth;
}
