package com.avengers.musinsa.domain.order.controller;


import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.request.DirectPurchaseRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderCreateResponse;
import com.avengers.musinsa.domain.order.dto.response.OrderSummaryResponse;
import com.avengers.musinsa.domain.order.dto.response.UserInfoDTO;
import com.avengers.musinsa.domain.order.service.OrderService;
import com.avengers.musinsa.domain.shipments.dto.ShippingAddressOrderDTO;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final TokenProviderService tokenProviderService;

    //주문화면 접속??
    @GetMapping("/")
    public String orders() {
        return "order";
    }

    @GetMapping("/user-info/{userId}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable Long userId) {
        UserInfoDTO userInfo = orderService.getUserInfo(userId);

        return ResponseEntity.ok(userInfo);
    }

    //주문하기
    @PostMapping("/{userId}/order")
    public ResponseEntity<OrderCreateResponse> createOrder(@PathVariable Long userId,
                                                           @RequestBody OrderCreateRequest orderCreateRequest) {
        OrderCreateResponse orderCreateResponse = orderService.createOrder(userId, orderCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreateResponse);
    }

      // 주문 완료 화면 조회
    @GetMapping("/{orderId}/completion/summary")
    public ResponseEntity<OrderSummaryResponse.OrderSummaryDto> getCompletionOrderSummary(@PathVariable Long orderId, @CookieValue(value = "Authorization") String authorization) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        OrderSummaryResponse.OrderSummaryDto orderSummaryInfo = orderService.getCompletionOrderSummary(orderId, userId);

        return ResponseEntity.ok(orderSummaryInfo);
    }

    @GetMapping("/address-list")
    public ResponseEntity<List<ShippingAddressOrderDTO>> getShippingAddressesUserId(
            @CookieValue(value = "Authorization", required = false) String authorization
            ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        List<ShippingAddressOrderDTO> shippingAddresses = orderService.getShippingAddressesUserId(userId);

        return ResponseEntity.ok(shippingAddresses);

    }

    // 직접 구매
    @PostMapping("/products")
    public ResponseEntity<?> directPurchase(@RequestBody DirectPurchaseRequest request) {
        try {
            // 요청 데이터 로깅
            System.out.println("Direct Purchase Request: " + request);

            // 일단 요청 받았다는 응답 반환
            return ResponseEntity.ok().body("구매 요청이 접수되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("구매 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
