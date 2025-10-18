package com.avengers.musinsa.domain.order.entity;

import lombok.Getter;

@Getter
public class PaymentBenefit {
    private Long paymentBenefitId;

    private PaymentMethod paymentMethod;
    private Long paymentMethodId;

    private String benefitType;
}
