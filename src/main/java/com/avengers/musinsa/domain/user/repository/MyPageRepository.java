package com.avengers.musinsa.domain.user.repository;

import com.avengers.musinsa.domain.user.dto.MyPageDto;
import com.avengers.musinsa.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyPageRepository {
    private final MyPageMapper myPageMapper;

    public MyPageDto findUserProfileId(Long userId){
        return myPageMapper.findUserProfileId(userId);
    }
    public MyPageDto findUserProfileByUserName(String username){
        return myPageMapper.findUserProfileByUserName(username);
    }

    public void updateNickname(String username, String nickname) {
        myPageMapper.updateNickname(username, nickname);
    }

    public void updateProfileImage(String username, String profileImage) {
        myPageMapper.updateProfileImage(username, profileImage);

    }
}
