package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;

import java.util.List;

public interface OrderFailureQueueService {
    void addToFailureQueue(String orderItemsFailure, Long orderId, List<OrderCreateRequest.ProductLine> orderProducts, String message);
}
