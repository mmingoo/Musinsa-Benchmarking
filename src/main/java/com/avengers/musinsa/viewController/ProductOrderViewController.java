package com.avengers.musinsa.viewController;

import com.avengers.musinsa.domain.order.dto.request.OrderPageRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;
import com.avengers.musinsa.domain.order.service.OrderPageService;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductOrderViewController {

    private final TokenProviderService tokenProviderService;
    private final OrderPageService orderPageService;

    /**
     * 상품 상세 페이지에서 구매하기 버튼 클릭 시 처리
     * POST 요청으로 주문 데이터를 받아서 세션에 저장하고 주문 페이지로 리다이렉트
     */
    @PostMapping("/direct-purchase")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> directPurchase(
            @RequestBody OrderPageRequest request,
            @CookieValue(value = "Authorization") String authorization,
            HttpSession session) {

        try {
            // 1. 쿠키에서 userId 추출
            Long userId = tokenProviderService.getUserIdFromToken(authorization);

            // 2. 주문 페이지에 필요한 데이터 조회
            OrderPageResponse response = orderPageService.getOrderPageInfo(userId, request);
            System.out.println("주문에 필요한 데이터 조회 성공");

            // 3. 세션에 주문 데이터 저장
            session.setAttribute("orderData", response);

            // 4. 성공 응답 반환
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("redirectUrl", "/orders/order-page");

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            // 비즈니스 로직 에러 (예: 재고 부족)
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);

        } catch (Exception e) {
            // 기타 시스템 에러
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "주문 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
            return ResponseEntity.status(500).body(result);
        }
    }
}
