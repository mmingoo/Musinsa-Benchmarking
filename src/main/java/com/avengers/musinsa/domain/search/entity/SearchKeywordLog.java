package com.avengers.musinsa.domain.search.entity;

import com.avengers.musinsa.domain.user.entity.User;

import java.sql.Timestamp;

public class SearchKeywordLog {
    private Long SearchKeywordLogId;

    private User user;
    private Long userId;

    private String searchText;
    private Timestamp searchDateTime;
    private Integer searchCount;
}
