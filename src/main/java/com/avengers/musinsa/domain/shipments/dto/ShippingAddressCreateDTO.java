package com.avengers.musinsa.domain.shipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
//입력용
public class ShippingAddressCreateDTO {
    private Long shippingAddressId;
    private Long userId;
    private String recipientName;
    private String recipientPhone;
    private String addressLine1; // recipientAddress 분리
    private String addressLine2;
    private String postalCode;
    private Boolean isDefault; // defaultAddress > isDefault


}
