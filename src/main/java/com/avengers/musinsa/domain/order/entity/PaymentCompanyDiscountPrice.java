package com.avengers.musinsa.domain.order.entity;

import lombok.Getter;

@Getter
public class PaymentCompanyDiscountPrice {
    private Long paymentCompanyDiscountPriceId;

    private PaymentBenefit paymentBenefit;
    private Long paymentBenefitId;

    private Integer DiscountPrice;
}
