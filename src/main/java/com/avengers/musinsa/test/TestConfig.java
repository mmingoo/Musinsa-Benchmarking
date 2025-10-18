//
//
//package com.avengers.musinsa.test;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class TestConfig {
//    private final MusinsaMapper musinsaMapper;
//
//    @Bean
//    public MybatisService mybatisService() {
//        return new MybatisService(this.mybatisTestRepository());
//    }
//
//    @Bean
//    public MybatisTestRepository mybatisTestRepository() {
//        return new MybatisTestRepository(this.musinsaMapper);
//    }
//
//}
