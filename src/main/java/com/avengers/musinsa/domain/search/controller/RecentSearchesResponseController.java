package com.avengers.musinsa.domain.search.controller;

import com.avengers.musinsa.domain.search.response.SearchKeywordResponseDTO;
import com.avengers.musinsa.domain.search.service.RecentSearchServiceImpl;
import com.avengers.musinsa.domain.user.auth.jwt.TokenProviderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class RecentSearchesResponseController {
    private final RecentSearchServiceImpl recentSearchService;
    private final TokenProviderService tokenProviderService;

    @GetMapping("/recent")
    public List<SearchKeywordResponseDTO> recentSearches(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing");
        }

        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return recentSearchService.getRecentSearches(userId);
    }

    @DeleteMapping("/recent/all")
    public ResponseEntity<Void> deleteAllRecentSearches(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        recentSearchService.deleteAllRecentSearches(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/recent")
    public ResponseEntity<Void> deleteRecentSearchKeyword(
            @CookieValue(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam("keyword") String keyword
    ) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        recentSearchService.deleteRecentSearchKeyword(userId, keyword);
        return ResponseEntity.noContent().build();
    }
}
