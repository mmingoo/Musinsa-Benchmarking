package com.avengers.musinsa.domain.order.controller;

import com.avengers.musinsa.domain.order.dto.request.OrderPageRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;
import com.avengers.musinsa.domain.order.service.OrderPageService;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderPageController {

    private final OrderPageService orderPageService;
    private final TokenProviderService tokenProviderService;

    /**
     * 주문 페이지 정보 조회
     */
    @GetMapping("/order-page")
    public ResponseEntity<OrderPageResponse> getOrderPageInfo(
            @RequestBody OrderPageRequest request,
            @CookieValue(value = "Authorization") String authorization) {

        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        OrderPageResponse response = orderPageService.getOrderPageInfo(userId, request);

        return ResponseEntity.ok(response);
    }
}




