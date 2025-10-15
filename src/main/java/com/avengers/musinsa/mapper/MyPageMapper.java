package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.user.dto.MyPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPageMapper {
    MyPageDto findUserProfileId(@Param("userId") Long userId);

    void updateNickname(@Param("username") String username, @Param("nickname") String nickname);

    void updateProfileImage(@Param("username") String username, @Param("profileImage") String profileImage);

    MyPageDto findUserProfileByUserName(@Param("username") String username);


}
