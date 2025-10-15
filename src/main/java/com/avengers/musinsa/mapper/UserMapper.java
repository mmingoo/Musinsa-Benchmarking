package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.user.dto.UserResponseDto;
import com.avengers.musinsa.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    User findByUsername(@Param("username") String username);
    
    void insertUser(User user);
    
    void updateUser(User user);
    
    User findByUserId(@Param("userId") Long userId);

    UserResponseDto.UserNameAndEmailAndMobileDto findUserNameAndEmailAndMobileById(@Param("userId") Long userId);
}