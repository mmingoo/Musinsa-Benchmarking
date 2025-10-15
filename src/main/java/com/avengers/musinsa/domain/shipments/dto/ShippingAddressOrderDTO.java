package com.avengers.musinsa.domain.shipments.dto;

import com.avengers.musinsa.domain.order.dto.response.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ShippingAddressOrderDTO {

    private Long shippingAddressId;
    //private UserInfoDTO userInfo;
    private Long userId;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    private String postalCode;
    private Boolean isDefault; // defaultAddress > isDefault

}
