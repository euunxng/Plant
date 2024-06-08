package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.service.UserModifyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserModifyController {

    private final UserModifyService userModifyService;

    @PutMapping("/user/userName")
    public String updateNickname(@RequestParam("userName") String newUsername, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        // 닉네임 업데이트
        userModifyService.updateUserName(user, newUsername);

        return "닉네임이 성공적으로 업데이트되었습니다.";
    }

    @PutMapping("/user/profilePicture")
    public String updateProfilePhoto(@RequestParam("profilePicture") MultipartFile profilePicture, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        System.out.println("Received request to update profile picture");
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.err.println("User not logged in");
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        String newProfilePhotoPath = userModifyService.updateProfilePicture(user, profilePicture);
        return newProfilePhotoPath; // 새 프로필 사진 URL 반환
    }

    @PutMapping("/user/userFace")
    public String updateUserFace(@RequestParam("userFace") MultipartFile userFace, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        // 사용자 얼굴 업데이트
        userModifyService.updateUserFace(user, userFace);

        return "사용자 얼굴이 성공적으로 업데이트되었습니다.";
    }
}
