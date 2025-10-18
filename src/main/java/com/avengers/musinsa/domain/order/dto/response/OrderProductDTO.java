package com.avengers.musinsa.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class OrderProductDTO {
    private Long productId;
    private String productName;
    private String brand;
    private String productImage;
    private String size;
    private String color;
    private String material;
    private Integer quantity;
    private Integer totalPrice;


}
