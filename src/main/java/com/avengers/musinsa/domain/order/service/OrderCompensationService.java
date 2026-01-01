package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;

import java.util.List;

public interface OrderCompensationService {
    void handleOrderItemsFailure(Long orderId, List<OrderCreateRequest.ProductLine> orderProducts, Exception e);

    void handleStockFailure(List<OrderCreateRequest.ProductLine> orderProducts, Exception e);
}
