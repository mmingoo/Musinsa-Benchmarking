package com.avengers.musinsa.domain.user.service;

import com.avengers.musinsa.domain.user.dto.MyPageDto;
import com.avengers.musinsa.domain.user.repository.MyPageRepository;
import com.avengers.musinsa.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageRepository myPageRepository;


    public MyPageDto findUserProfileId(Long userId){
        return myPageRepository.findUserProfileId(userId);
    }




    public MyPageDto updateNickname(String username, String nickname) {
        myPageRepository.updateNickname(username, nickname);
        return myPageRepository.findUserProfileByUserName(username);
    }



    public MyPageDto updateProfileImage(String username, MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("file is empty");

        try {
            // 실제 저장 경로 (예: 프로젝트 루트/uploads)
            Path uploadDir = Path.of("uploads");
            Files.createDirectories(uploadDir);

            String ext = getExtension(file.getOriginalFilename());
            String filename = "pf_" + username + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ext;

            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // 정적 리소스 매핑(/uploads/**)을 통해 접근할 URL
            String publicUrl = "/uploads/" + filename;

            myPageRepository.updateProfileImage(username, publicUrl);
            return myPageRepository.findUserProfileByUserName(username);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
    private String getExtension(String name){
        if (name == null || !name.contains(".")) return "";
        return name.substring(name.lastIndexOf("."));
    }


    public MyPageDto findUserProfileByUserName(String username){
        return myPageRepository.findUserProfileByUserName(username);
    }


}
