package com.example.project.service;

import com.example.project.domain.User;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserModifyService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUserName(User user, String newUsername) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 닉네임 업데이트
        userInfo.setUserName(newUsername);
        userRepository.save(userInfo);
    }

    @Transactional
    public void updateProfilePicture(User user, MultipartFile profilePicture) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 프로필 사진 업로드 및 경로 저장
        try {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            byte[] bytes = profilePicture.getBytes();
            Path path = Paths.get("uploads/profile/" + fileName);
            Files.write(path, bytes);
            userInfo.setProfilePhotoPath(path.toString());
            userRepository.save(userInfo);
        } catch (IOException e) {
            throw new RuntimeException("프로필 사진 업로드에 실패했습니다.", e);
        }
    }

    @Transactional
    public void updateUserFace(User user, MultipartFile userFace) {
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 얼굴 이미지 업로드 및 경로 저장
        try {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            byte[] bytes = userFace.getBytes();
            Path path = Paths.get("uploads/face/" + fileName);
            Files.write(path, bytes);
            userInfo.setUserFacePath(path.toString());
            userRepository.save(userInfo);
        } catch (IOException e) {
            throw new RuntimeException("얼굴 이미지 업로드에 실패했습니다.", e);
        }
    }
}
