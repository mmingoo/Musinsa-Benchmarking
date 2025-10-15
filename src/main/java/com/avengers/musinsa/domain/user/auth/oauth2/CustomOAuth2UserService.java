package com.avengers.musinsa.domain.user.auth.oauth2;

import com.avengers.musinsa.domain.enums.UserActiveStatus;
import com.avengers.musinsa.domain.user.dto.UserDTO;
import com.avengers.musinsa.domain.user.auth.oauth2.dto.CustomOAuth2User;
import com.avengers.musinsa.domain.user.auth.oauth2.dto.NaverResponse;

import com.avengers.musinsa.domain.user.entity.User;
import com.avengers.musinsa.domain.user.service.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        NaverResponse naverResponse = null;

        if (registrationId.equals("naver")) {
            naverResponse = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = naverResponse.getProvider() + " " + naverResponse.getProviderId();
        User existData = userService.findByUsername(username);

        if (existData == null) {
            // 새 사용자 생성 - 추가 정보 포함
            User newUser = User.builder()
                    .username(username)
                    .name(naverResponse.getName())
                    .email(naverResponse.getEmail())
                    .gender(naverResponse.getGender())
                    .mobile(naverResponse.getMobile())
                    .birthyear(naverResponse.getBirthyear())
                    .birthday(naverResponse.getBirthday())
                    .ageGroup(naverResponse.getAgeGroup())
                    .socialId(naverResponse.getProviderId())
                    .socialType(naverResponse.getProvider())
                    .isMember("Y")
                    .role("ROLE_USER")
                    .nickname("유저")
                    .userMileage(0)
                    .activeStatus("ACTIVE")
                    .build();
            System.out.println(newUser.toString());
            userService.saveUser(newUser);

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .name(naverResponse.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDTO);
        } else {
            // 기존 사용자 업데이트 - 추가 정보 포함
            User updatedUser = User.builder()
                    .userId(existData.getUserId())
                    .name(naverResponse.getName())
                    .email(naverResponse.getEmail())
                    .mobile(naverResponse.getMobile())
                    .ageGroup(naverResponse.getAgeGroup())
                    .build();

            userService.saveUser(updatedUser);

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .name(naverResponse.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDTO);
        }
    }



}