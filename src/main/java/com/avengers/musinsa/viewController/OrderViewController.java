package com.avengers.musinsa.viewController;

import com.avengers.musinsa.domain.order.dto.request.OrderCreateRequest;
import com.avengers.musinsa.domain.order.dto.request.OrderPageRequest;
import com.avengers.musinsa.domain.order.dto.response.OrderCreateResponse;
import com.avengers.musinsa.domain.order.dto.response.OrderPageResponse;
import com.avengers.musinsa.domain.order.dto.response.OrderSummaryResponse;
import com.avengers.musinsa.domain.order.service.OrderPageService;
import com.avengers.musinsa.domain.order.service.OrderService;
import com.avengers.musinsa.domain.product.dto.response.ProductVariantDto;
import com.avengers.musinsa.domain.product.service.ProductVariantService;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderViewController {

    private final TokenProviderService tokenProviderService;
    private final OrderService orderService;
    private final OrderPageService orderPageService;
    private final ProductVariantService productVariantService;

    @PostMapping("/completion-order")
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request,
                                                           @CookieValue(value = "Authorization", required = false) String authorization,
                                                           Model model) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);

        //관련된 정보들 세팅
        request.setUserId(userId);

        // variantId 와 옵션 세팅
        for (OrderCreateRequest.ProductLine productLine : request.getProduct()) {
            System.out.println(
                    "productId = " + productLine.getProductId() + " optionName = " + productLine.getOptionName());

            ProductVariantDto variant = productVariantService.findProductVariantByOptionName(
                    productLine.getProductId(),
                    productLine.getOptionName()
            );

            if (variant == null) {
                throw new IllegalArgumentException(
                        "상품 옵션을 찾을 수 없습니다. productId: " + productLine.getProductId() +
                                ", optionName: " + productLine.getOptionName()
                );
            }

            Long variantId = variant.getProductVariantId();
            productLine.setVariantId(variantId);

            // HashMap 생성 후 설정
            HashMap<String, String> options = new HashMap<>();
            options.put("size", variant.getSizeValue());
            productLine.setOrderItemSize(variant.getSizeValue());
            options.put("color", variant.getColorValue());
            productLine.setColor(variant.getColorValue());
            productLine.setOptions(options);  // ← 생성한 Map을 설정
        }

        OrderCreateResponse orderCreateResponse = orderService.createOrder(userId, request);
        System.out.println("생성된 주문 ID: " + orderCreateResponse.getOrderId());
        System.out.println("응답 객체: " + orderCreateResponse);

        return ResponseEntity.ok(orderCreateResponse);

    }


    @GetMapping("/order-complete/{orderId}")
    public String complete(@PathVariable Long orderId,
                           @CookieValue(value = "Authorization", required = false) String auth,
                           Model model) {
        System.out.println("리다이랙트가 됨");
        Long userId = tokenProviderService.getUserIdFromToken(auth);
        OrderSummaryResponse.OrderSummaryDto response = orderService.getCompletionOrderSummary(orderId, userId);
        model.addAttribute("orderData", response);
        return "order/orderCompletePage";
    }


    @PostMapping("/orders-page")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processOrder(
            @RequestBody OrderPageRequest request,
            @CookieValue(value = "Authorization") String authorization,
            HttpSession session) {

        try {
            Long userId = tokenProviderService.getUserIdFromToken(authorization);
            OrderPageResponse response = orderPageService.getOrderPageInfo(userId, request);

            // 세션에 데이터 저장
            session.setAttribute("orderData", response);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("redirectUrl", "/orders/order-page");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "주문 처리 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/order-page")
    public String showOrderPage(HttpSession session, Model model) {

        OrderPageResponse response = (OrderPageResponse) session.getAttribute("orderData");

        if (response == null) {
            return "redirect:/cart";
        }

        BuyerRecipientInfoAdapter buyerRecipientInfoAdapter = new BuyerRecipientInfoAdapter(response);
        List<ProductAdapter> orderProductAdapters = response.getProducts().stream()
                .map(ProductAdapter::new)
                .toList();

        System.out.println(response.getMileageInfo().getTotalMileage());

        model.addAttribute("buyerRecipientInfoAdapter", buyerRecipientInfoAdapter);
        model.addAttribute("orderProductAdapters", orderProductAdapters);

        return "order/orderPage";
    }


    public static class BuyerRecipientInfoAdapter {
        private final OrderPageResponse product;

        public BuyerRecipientInfoAdapter(OrderPageResponse product) {
            this.product = product;
        }

        //구매자 정보 가져오기
        public String getBuyerName() {
            return product.getBuyerInfo().getName();
        }

        public String getBuyerPhone() {
            return product.getBuyerInfo().getPhone();
        }

        public String getBuyerEmail() {
            return product.getBuyerInfo().getEmail();
        }

        // 수령자 정보 가져오기
        public String getRecipientName() {
            return product.getBuyerInfo().getDefaultAddress().getRecipientName();
        }

        public String getRecipientPhone() {
            return product.getBuyerInfo().getDefaultAddress().getRecipientPhone();
        }

        public String getRecipientPostCode() {
            return product.getBuyerInfo().getDefaultAddress().getPostCode();
        }

        public String getRecipientAddress() {
            return product.getBuyerInfo().getDefaultAddress().getAddress();
        }

        public String getRecipientDetailAddress() {
            return product.getBuyerInfo().getDefaultAddress().getDetailAddress();
        }

        //적립금 가져오기
        public Integer getMileage() {
            return product.getMileageInfo().getTotalMileage();
        }
    }


    public static class ProductAdapter {
        private final OrderPageResponse.OrderProductInfo productInfo;

        public ProductAdapter(OrderPageResponse.OrderProductInfo productInfo) {
            this.productInfo = productInfo;
        }

        //구매 상품 가져오기
        public Long getBrandId() {
            return productInfo.getBrandId();
        }

        public String getBrandName() {
            return productInfo.getBrandName();
        }

        public String getBrandImage() {
            return productInfo.getBrandImage();
        }

        public Long getProductId() {
            return productInfo.getProductId();
        }

        public String getProductName() {
            return productInfo.getProductName();
        }

        public String getProductImage() {
            return productInfo.getProductImage();
        }

        public Integer getFinalPrice() {
            return productInfo.getFinalPrice();
        }

        public String getOptionName() {
            return productInfo.getOptionName();
        }

        public Integer getQuantity() {
            return productInfo.getQuantity();
        }

        public Integer getTotalPrice() {
            return productInfo.getTotalPrice();
        }

        public Integer getDiscountRate() {
            return productInfo.getDiscountRate();
        }

        public Integer getOriginalPrice() {
            return productInfo.getOriginalPrice();
        }
    }


}
