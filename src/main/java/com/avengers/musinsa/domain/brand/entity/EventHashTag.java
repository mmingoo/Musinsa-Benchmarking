package com.avengers.musinsa.domain.brand.entity;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class EventHashTag {
    private Integer eventHashTagId;
    private String hashTagName;
    private String hashTagImageUrl;
    private Timestamp eventStartDate;
    private Timestamp eventEndDate;
}