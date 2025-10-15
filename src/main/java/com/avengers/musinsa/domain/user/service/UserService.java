package com.avengers.musinsa.domain.user.service;


import com.avengers.musinsa.domain.user.auth.oauth2.repository.UserRepository;
import com.avengers.musinsa.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user){
        userRepository.saveUser(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

}