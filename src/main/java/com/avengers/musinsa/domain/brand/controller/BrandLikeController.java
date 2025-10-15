package com.avengers.musinsa.domain.brand.controller;
import com.avengers.musinsa.domain.brand.dto.response.BrandLikeResponse;
import com.avengers.musinsa.domain.brand.service.BrandService;
import com.avengers.musinsa.domain.brand.service.BrandServiceImpl;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BrandLikeController {
    private final BrandService brandService;
    private final TokenProviderService tokenProviderService;

    //브랜드 좋아요 토글 기능
    @PostMapping("/brands/{brandId}/liked")

    public BrandLikeResponse BrandLikeToggle(@PathVariable Long brandId, @CookieValue(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing");
        } else {
            Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            System.out.println("userId = " + userId);
            return brandService.BrandLikeToggle(userId, brandId);
        }
    }
}



