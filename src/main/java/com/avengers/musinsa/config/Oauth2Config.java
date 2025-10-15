package com.avengers.musinsa.config;

import com.avengers.musinsa.domain.user.auth.oauth2.CustomOAuth2UserService;
import com.avengers.musinsa.domain.user.auth.oauth2.repository.UserRepository;
import com.avengers.musinsa.domain.user.service.UserService;
import com.avengers.musinsa.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Oauth2Config {
    private final UserMapper userMapper;

    @Bean
    public CustomOAuth2UserService customOAuth2UserService(){
        return new CustomOAuth2UserService(this.userService());
    }

    @Bean
    public UserService userService(){
        return new UserService(this.userRepository());
    }

    @Bean
    public UserRepository userRepository(){
        return new UserRepository(userMapper);
    }
}
