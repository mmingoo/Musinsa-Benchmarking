package com.avengers.musinsa.domain.shipments.entity;

import com.avengers.musinsa.domain.brand.entity.Brand;
import lombok.Getter;

//배송비정책
@Getter
public class ShippingFeePolicy {
    private Integer shippingFeePolicyId;

    private ShippingFees shippingFee;
    private Long shippingFeeId;

    private ShippingFeeCondition shippingFeeCondition;
    private Long shippingFeeConditionId;

    private Brand brand;
    private Long brandId;

}
