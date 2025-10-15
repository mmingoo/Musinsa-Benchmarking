package com.avengers.musinsa.domain.user.auth.oauth2;

import com.avengers.musinsa.domain.user.auth.jwt.JWTUtil;
import com.avengers.musinsa.domain.user.auth.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 토큰 생성 (24시간 유효)
        String token = jwtUtil.createJwt(username, role, 24 * 60 * 60 * 1000L);

        response.addCookie(createCookie("Authorization", token));

        // 로그인 성공 후 메인 페이지로 리다이렉트
        response.sendRedirect("http://localhost:8080/main");
        System.out.println("Response status: " + response.getStatus());
        System.out.println("Response committed: " + response.isCommitted());
        System.out.println("왜 리다이렉트가 안돼?");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 24시간
        //cookie.setSecure(true); // HTTPS에서만 사용
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}