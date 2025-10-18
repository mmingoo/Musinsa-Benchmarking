package com.avengers.musinsa.domain.order.entity;

import java.sql.Timestamp;
import lombok.Getter;

@Getter
public class OrderCancellationReturnExchange {
    private Long orderCancellationReturnExchangesId;

    private OrderItem orderItem;
    private Long orderItemId;

    private String cancellationReturnExchangeReason;
    private String returnMethod;
    private String returnPickupLocation;
    private String returnDestination;
    private Integer returnShippingFee;
    private Timestamp returnDate;
    private String exchangeMethod;
    private String exchangePickupLocation;
    private String exchangeDestination;
    private Integer exchangeShippingFee;
    private String cancellationReturnExchangeStatus;
    private String returnCarrier;
    private String returnTrackingNumber;
    private String reshipmentCarrier;
    private String reshipTrackingNumber;
    private String refundMethod;
    private String refundStatus;
    private String noticesId;
    private Timestamp completionDate;
}
