package com.avengers.musinsa.domain.shipments.entity;

import lombok.Getter;

//배송
@Getter
public class Shipment {
    private Integer shippingId;

    private ScheduledDeliveryInformation scheduledDeliveryInformation;
    private Long scheduledDeliveryInformationId;

    private ShippingRequestTypes shippingRequestType;
    private Long shippingRequestTypeId;


    private ShippingStatuses shippingStatuses;
    private Long shippingStatusesId;

    private String shippingInquiry;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    private String shippingDirectRequest;
    private String postalCode;

}
