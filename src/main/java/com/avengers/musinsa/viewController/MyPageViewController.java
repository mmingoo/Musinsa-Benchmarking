package com.avengers.musinsa.viewController;

import com.avengers.musinsa.domain.user.auth.oauth2.dto.CustomOAuth2User;
import com.avengers.musinsa.domain.user.dto.MyPageDto;
import com.avengers.musinsa.domain.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageViewController {

    private final MyPageService myPageService;

    @GetMapping
    public String myPage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomOAuth2User)) {
            return "redirect:/login";
        }

        CustomOAuth2User userDetails = (CustomOAuth2User) principal;
        MyPageDto profile = myPageService.findUserProfileByUserName(userDetails.getUsername());

        if (profile == null) { return "redirect:/login";}

        model.addAttribute("profile", profile);
        model.addAttribute("userMileage", profile.getUserMileage() != null ? profile.getUserMileage() : 0);

        return "mypage/mypage";
    }

    // 프로필 수정(설정) 메뉴

    @GetMapping("/settings")
    public String Setting(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomOAuth2User)) {
            return "redirect:/login";
        }

        CustomOAuth2User userDetails = (CustomOAuth2User) principal;

        MyPageDto user = myPageService.findUserProfileByUserName(userDetails.getUsername());
        if (user == null) return "redirect:/login";
        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("profileImage", user.getProfileImage());
        model.addAttribute("email", user.getEmail());
        return "mypage/settings";
    }


    @PostMapping("/nickname")
    public String updateNickname(@RequestParam("nickname") String nickname,
                                 Principal principal, RedirectAttributes ra) {
        MyPageDto updated = myPageService.updateNickname(principal.getName(), nickname);

        ra.addFlashAttribute("msg", "닉네임이 변경되었습니다.");

        return "redirect:/mypage";
    }

    @PostMapping("/profile-image")
    public String updateProfileImage(@RequestParam("file") MultipartFile file,
                                     Principal principal) throws IOException {
        if (!file.isEmpty()) {
            myPageService.updateProfileImage(principal.getName(), file);
        }
        return "redirect:/mypage";
    }



    private MyPageDto getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomOAuth2User)) {
            return null;
        }

        CustomOAuth2User userDetails = (CustomOAuth2User) principal;
        return myPageService.findUserProfileByUserName(userDetails.getUsername());
    }



}




