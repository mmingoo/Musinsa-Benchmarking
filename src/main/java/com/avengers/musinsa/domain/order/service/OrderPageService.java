package com.avengers.musinsa.domain.order.service;

import com.avengers.musinsa.domain.order.dto.request.OrderPageRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;

public interface OrderPageService {
    OrderPageResponse getOrderPageInfo(Long userId, OrderPageRequest request);
}
