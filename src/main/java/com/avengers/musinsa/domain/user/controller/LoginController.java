package com.avengers.musinsa.domain.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {

        return "login/login";
    }

    @PostMapping("/custom-logout")
    public String logout(HttpServletResponse response) {
        try {
            System.out.println("로그아웃 실행됨");
            // Authorization 쿠키 삭제
            Cookie authCookie = new Cookie("Authorization", null);
            authCookie.setMaxAge(0); // 즉시 만료
            authCookie.setPath("/");
            authCookie.setHttpOnly(true);
            authCookie.setSecure(false); // 개발환경에서는 false, 운영에서는 true
            response.addCookie(authCookie);

            // JSESSIONID 쿠키도 삭제 (세션 무효화)
            Cookie sessionCookie = new Cookie("JSESSIONID", null);
            sessionCookie.setMaxAge(0);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);

            // 메인 페이지로 리다이렉트
            return "redirect:/main";

        } catch (Exception e) {
            // 에러가 발생해도 메인 페이지로 리다이렉트
            return "redirect:/main";
        }
    }
}