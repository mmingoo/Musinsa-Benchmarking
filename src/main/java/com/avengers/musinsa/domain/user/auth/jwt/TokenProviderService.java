package com.avengers.musinsa.domain.user.auth.jwt;

import com.avengers.musinsa.domain.user.auth.oauth2.repository.UserRepository;
import com.avengers.musinsa.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TokenProviderService {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public TokenProviderService(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public Long getUserIdFromToken(String authorizationHeader) {
        // "Bearer " 제거
        String token = authorizationHeader.replace("Bearer ", "");

        // 토큰에서 username 추출
        String username = jwtUtil.getUsername(token);

        // username으로 사용자 ID 조회
        User user = userRepository.findByUsernameId(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found for username: " + username);
        }
        return user.getUserId();
    }
}