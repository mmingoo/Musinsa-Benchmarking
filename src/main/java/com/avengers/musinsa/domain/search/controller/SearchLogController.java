package com.avengers.musinsa.domain.search.controller;

import com.avengers.musinsa.domain.search.service.SearchLogService;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchLogController {

    //서비스 계층에 일을 시킨다, 주입
    private final SearchLogService searchLogService;
    private final TokenProviderService tokenProviderService;

    // /api/v1/logs/search-keyword url 요청을 받아서,
    //logid로 응답을 넘겨준다
    @PostMapping("/search-logs")
    public ResponseEntity<Void> saveSearchLog(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam("keyword") String keyword
    ) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            // 비회원은 검색 로그를 남기지 않는다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        searchLogService.saveSearchKeywordLog(keyword, userId);
        return ResponseEntity.ok().build();
    }






}
