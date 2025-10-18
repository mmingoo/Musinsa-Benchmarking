package com.avengers.musinsa.domain.user.entity;

import lombok.Getter;

@Getter
public class UserBankRefundAccounts {
    private Integer userBankRefundAccountId;
    private User user;
    private Long userId;

    private BankNameList bankNameList;
    private Long bankNameListId;

    private String userBankRefundAccount;
}
