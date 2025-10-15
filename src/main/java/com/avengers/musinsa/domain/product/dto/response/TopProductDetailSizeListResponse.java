package com.avengers.musinsa.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TopProductDetailSizeListResponse {
    private Long topSizeDetailId;

    private  Long sizeDetailImageId;

    private String cm;
    private BigDecimal length;
    private BigDecimal shoulderWidth;
    private BigDecimal chestWidth;
    private BigDecimal sleaveLength;


}
