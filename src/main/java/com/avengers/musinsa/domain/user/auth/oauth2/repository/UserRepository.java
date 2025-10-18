package com.avengers.musinsa.domain.user.auth.oauth2.repository;

import com.avengers.musinsa.domain.user.entity.User;
import com.avengers.musinsa.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserMapper userMapper;

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void saveUser(User user) {
        System.out.println("유저 저장");
        System.out.println(user.toString());
        if (user.getUserId() == null) {
            userMapper.insertUser(user);

        } else {
            userMapper.updateUser(user);
        }
    }

    public User findByUserId(Long userId) {
        return userMapper.findByUserId(userId);
    }

    public User findByUsernameId(String username) {
        return userMapper.findByUsername(username);
    }
}
