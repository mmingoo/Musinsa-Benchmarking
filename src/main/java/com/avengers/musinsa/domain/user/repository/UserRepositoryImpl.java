package com.avengers.musinsa.domain.user.repository;

import com.avengers.musinsa.domain.user.dto.UserResponseDto;
import com.avengers.musinsa.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{
    private final UserMapper userMapper;

    @Override
    public UserResponseDto.UserNameAndEmailAndMobileDto findUserNameAndEmailAndMobileById(Long userId) {
        return userMapper.findUserNameAndEmailAndMobileById(userId);
    }
}
