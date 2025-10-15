package com.avengers.musinsa.domain.user.controller;

import com.avengers.musinsa.domain.user.auth.oauth2.dto.CustomOAuth2User;
import com.avengers.musinsa.domain.user.dto.MyPageDto;
import com.avengers.musinsa.domain.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/profile")
    public ResponseEntity<MyPageDto> getMyPage(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        // 소셜 로그인 사용자 정보 가져오기
        CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
        String userName = userDetails.getUsername();

        // DB 조회
        MyPageDto userProfile = myPageService.findUserProfileByUserName(userName);

        if (userProfile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userProfile);

    }

    @PostMapping("/nickname")
    public ResponseEntity< MyPageDto> updateNickname(@RequestParam("nickname") String nickname, Principal principal) {
        MyPageDto updateUser = myPageService.updateNickname(principal.getName(), nickname);
        return ResponseEntity.ok(updateUser);
    }

    @PostMapping("/profile-image")
    public ResponseEntity<MyPageDto> updateProfileImage(@RequestParam("file") MultipartFile file, Principal principal) {
        MyPageDto updatedUser = myPageService.updateProfileImage(principal.getName(), file);
        return ResponseEntity.ok(updatedUser);
    }


}
