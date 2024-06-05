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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDto userRequest) {
        return userService.save(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @GetMapping("/userID/{userID}")
    public ResponseEntity<?> checkUserID(@PathVariable("userID") String userID) {
        boolean exists = userService.checkUserID(userID);
        if (exists) {
            return ResponseEntity.status(200).build(); // 200 OK - 중복됨
        } else {
            return ResponseEntity.status(404).build(); // 404 Not Found - 중복되지 않음
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable("email") String email) {
        boolean exists = userService.checkEmail(email);
        if (exists) {
            return ResponseEntity.status(200).build(); // 200 OK - 중복됨
        } else {
            return ResponseEntity.status(404).build(); // 404 Not Found - 중복되지 않음
        }
    }

    @GetMapping("/{userID}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userID") String userID) {
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
