package com.avengers.musinsa.viewController;

import com.avengers.musinsa.domain.product.dto.response.ProductByCategoryResponse;
import com.avengers.musinsa.domain.product.dto.search.SearchResponse;
import com.avengers.musinsa.domain.product.service.ProductService;
import com.avengers.musinsa.domain.product.service.ProductServiceImpl;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductViewController {
    private final TokenProviderService tokenProviderService;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public String getProductDetail(@PathVariable Long productId, Model model) {

        return "product/productDetailPage";
    }

    @GetMapping()
    public String searchPage(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "LIKE") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "12") int size,
            @CookieValue(value = "Authorization", required = false) String authorization,
            Model model
    ) {
        Long userId = null;
        if (authorization != null && !authorization.isBlank()) {
            try {
                userId = tokenProviderService.getUserIdFromToken(authorization);
            } catch (Exception ignore) { /* 비로그인/만료 등은 null 로 */ }
        }

        SearchResponse result = productService.searchProducts(keyword, userId, sortBy, page, size);

        model.addAttribute("keyword", keyword);
        model.addAttribute("result", result);
        // JSP에서 ${result} 사용
        return "product/searchProducts"; // JSP 뷰 경로
    }

    @GetMapping("/category/{categoryId}")
    public String getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "sortBy", required = false, defaultValue = "LIKE") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "12") int size,
            @CookieValue(value = "Authorization", required = false) String authorization,
            Model model) {
        Long userId = null;
        if (authorization != null && !authorization.isBlank()) {
            try {
                userId = tokenProviderService.getUserIdFromToken(authorization);
            } catch (Exception ignore) { /* 비로그인/만료 등은 null 로 */ }
        }

        List<ProductByCategoryResponse> products = productService.getProductsByCategory(categoryId, userId, sortBy, page, size);
        model.addAttribute("products", products);
        return "product/categoryProductsPage";
    }
}
