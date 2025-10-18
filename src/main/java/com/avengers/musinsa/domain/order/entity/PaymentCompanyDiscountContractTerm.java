package com.avengers.musinsa.domain.order.entity;

import lombok.Getter;

@Getter
public class PaymentCompanyDiscountContractTerm {
    private Long paymentCompanyDiscountContractTermId;

    private PaymentBenefit paymentBenefit;
    private Long paymentBenefitId;

    private Integer contractTermPrice;
}
