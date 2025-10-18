package com.avengers.musinsa.viewController;

import com.avengers.musinsa.domain.product.dto.response.RecommendationResponse;
import com.avengers.musinsa.domain.product.service.ProductService;
import com.avengers.musinsa.domain.product.entity.Gender;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainPageViewController {

    private final ProductService productService;
    private final TokenProviderService tokenProviderService;

    @GetMapping("/main")
    public String main(@CookieValue(value = "Authorization", required = false) String authorization, Model model) {
        try {
            // 사용자 ID 가져오기 (비로그인일 경우 null)
            Long userId = null;
            if (authorization != null && !authorization.isBlank()) {
                try {
                    userId = tokenProviderService.getUserIdFromToken(authorization);
                } catch (Exception ignore) { /* 비로그인/만료 등은 null 로 */ }
            }

            // 남성 상품 추천 API 호출
            List<RecommendationResponse> maleProducts = productService.getRecommendationProductList(Gender.MALE, userId);
            List<ProductAdapter> menProductAdapters = maleProducts.stream()
                    .map(ProductAdapter::new)
                    .collect(Collectors.toList());

            // 여성 상품 추천 API 호출
            List<RecommendationResponse> femaleProducts = productService.getRecommendationProductList(Gender.FEMALE, userId);
            List<ProductAdapter> womenProductAdapters = femaleProducts.stream()
                    .map(ProductAdapter::new)
                    .collect(Collectors.toList());

            model.addAttribute("menProducts", menProductAdapters);
            model.addAttribute("womenProducts", womenProductAdapters);

            System.out.println("남성 상품 수: " + menProductAdapters.size());
            System.out.println("여성 상품 수: " + womenProductAdapters.size());

        } catch (Exception e) {
            // API 호출 실패시 빈 리스트 전달
            model.addAttribute("menProducts", List.of());
            model.addAttribute("womenProducts", List.of());
            e.printStackTrace();
        }

        return "main/main";
    }

    // RecommendationResponse DTO를 JSP에서 사용하기 위한 어댑터
    public static class ProductAdapter {
        private final RecommendationResponse product;

        public ProductAdapter(RecommendationResponse product) {
            this.product = product;
        }

        // JSP에서 사용할 getter들
        public Long getId() { return product.getProductId(); }
        public String getName() { return product.getProductName(); }
        public String getProductName() { return product.getProductName(); } // JSP에서 productName으로 접근할 때 사용
        public String getBrandName() { return product.getProductBrand(); }
        public Integer getCurrentPrice() { return product.getProductPrice(); }
        public String getImageUrl() { return product.getProductImage(); }
        public Integer getOriginalPrice() { return product.getProductPrice(); } // 할인이 없으므로 동일
        public Integer getDiscountRate() { return product.getDiscountRate(); } // 할인율
        public Boolean getIsLiked() { return product.getIsLiked(); } // 좋아요 상태

        // 원본 데이터 접근
        public RecommendationResponse getOriginal() { return product; }
    }
}