package com.avengers.musinsa.viewController;

import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import com.avengers.musinsa.domain.user.dto.ProductsInCartInfoResponse;
import com.avengers.musinsa.domain.user.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CartViewController {

    private final CartService cartService;

    private final TokenProviderService tokenProviderService;

    @GetMapping("/cart")
    public String cartTestPage(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader,
            Model model) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);

        List<ProductsInCartInfoResponse> responses = cartService.getProductsInCart(userId);
        model.addAttribute("cartProducts", responses);
        return "user/cart";
    }

}
