package com.avengers.musinsa.domain.order.entity;

import lombok.Getter;

@Getter
public class PaymentCompany {
    private Long paymentCompanyId;

    private PaymentBenefit paymentBenefit;
    private Long paymentBenefitId;

    private String name;
}
