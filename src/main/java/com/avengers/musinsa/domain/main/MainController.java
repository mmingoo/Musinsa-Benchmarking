package com.avengers.musinsa.domain.main;

import com.avengers.musinsa.domain.product.dto.search.SearchResponse;
import com.avengers.musinsa.domain.product.service.ProductService;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/product/productDetail")
    public String getProductDetail() {

        return "product/productDetail";
    }

    @GetMapping("/product/likeProducts")
    public String getlikeProducts() {

        return "product/likeProducts";
    }

    @GetMapping("/product/product")
    public String getProduct() {

        return "product/product";
    }

    @GetMapping("/user/cart")
    public String getCart() {

        return "user/cart";
    }


}

