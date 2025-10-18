package com.avengers.musinsa.domain.user.repository;

import com.avengers.musinsa.domain.user.dto.UserResponseDto;
import com.avengers.musinsa.domain.user.entity.User;

public interface UserRepository {
    UserResponseDto.UserNameAndEmailAndMobileDto findUserNameAndEmailAndMobileById(Long userId);
}
