//
//package com.avengers.musinsa.test;
//
//import java.util.List;
//import lombok.Generated;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class MybatisService {
//    private final MybatisTestRepository mybatisTestRepository;
//
//    public List<TestUser> getAllUser() {
//        return this.mybatisTestRepository.findAllUser();
//    }
//
//    public TestUser getUserById(Long id) {
//        TestUser user = this.mybatisTestRepository.findUserById(id);
//        if (user == null) {
//            throw new RuntimeException("User not found with id: " + id);
//        } else {
//            return user;
//        }
//    }
//
//    public void createUser(String name, String email) {
//        TestUser user = new TestUser();
//        user.setName(name);
//        user.setEmail(email);
//        this.mybatisTestRepository.insertUser(user);
//    }
//
//}
