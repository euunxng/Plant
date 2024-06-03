package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.dto.LoginRequest;
import com.example.project.dto.UserDto;
import com.example.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<?> Save(@RequestBody UserDto UserRequest) {
        return userService.save(UserRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userID) {
        return userService.getUserInfo(userID);
    }

    @GetMapping("/myPage")
    public UserDto getMyPageInfo(HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        // 사용자 정보 가져오기
        return userService.getUserInfo(user);
    }

}