package com.avengers.musinsa.domain.shipments.entity;

import lombok.Getter;

import java.sql.Timestamp;

//배송상태
@Getter
public class ShippingStatuses {
    private Integer shippingStatuses;
    private String status;
    private String hubName;
    private String hubStatus;
    private Timestamp arrivalTime;
    private Timestamp departureTime;

}
