//
//package com.avengers.musinsa.test;
//
//import java.util.List;
//
//import com.avengers.musinsa.global.base.BaseResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@Controller
//@RequiredArgsConstructor
//public class TestUserController {
//    private final MybatisService mybatisService;
//
//    @GetMapping("/")
//    public String home(Model model) {
//        try {
//            List<TestUser> user = this.mybatisService.getAllUser();
//            model.addAttribute("user", user);
//            return "test/testindex";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "error";
//        }
//    }
//
//    @GetMapping("/user")
//    public String getUserById(@RequestParam Long id, Model model) {
//        try {
//            TestUser user = this.mybatisService.getUserById(id);
//            model.addAttribute("user", user);
//            return "test/user";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "error";
//        }
//    }
//
//    @PostMapping("/user")
//    public String addUser(@RequestParam String name, @RequestParam String email, Model model) {
//        try {
//            this.mybatisService.createUser(name, email);
//            return "redirect:/";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            model.addAttribute("user", this.mybatisService.getAllUser());
//            return "test/testindex";
//        }
//    }
//
//    @GetMapping("/testUser")
//    public BaseResponse<TestDto.TestPrintDto> testPrintDtoBaseResponse() {
//
//        TestUser testUser = new TestUser();
//        testUser.setId(1L);
//        testUser.setName("어벤져스");
//        testUser.setEmail("aldern23@naver.com");
//        return BaseResponse.onSuccess(TestUserDtoConverter.toTestPrintDto(testUser));
//    }
//
//
//    @GetMapping("/testUserFail")
//    public BaseResponse<TestDto.TestPrintDto> testPrintDtoBaseFailResponse() {
//
//        TestUser testUser = new TestUser();
//        testUser.setId(1L);
//        testUser.setName("어벤져스");
//        testUser.setEmail("aldern23@naver.com");
//        return BaseResponse.onSuccess(TestUserDtoConverter.toTestPrintDto(testUser));
//    }
//
//    @GetMapping("/main")
//    public String getMain(Model model, @AuthenticationPrincipal UserDetails userDetails) {
//        // 로그인 상태 확인
//        if (userDetails != null) {
//            model.addAttribute("isLoggedIn", true);
//            model.addAttribute("username", userDetails.getUsername());
//        } else {
//            model.addAttribute("isLoggedIn", false);
//        }
//        return "test/main";
//    }
//
//    @GetMapping("/product")
//    public String sidebar() {
//        return "test/product"; // sidebar.jsp로 이동
//    }
//
//}
//
//
