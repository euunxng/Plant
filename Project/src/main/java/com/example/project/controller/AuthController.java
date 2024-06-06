package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.dto.ResetPasswordRequest;
import com.example.project.service.EmailService;
import com.example.project.service.UserService;
import com.example.project.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Autowired
    public AuthController(UserService userService, EmailService emailService, VerificationService verificationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    // 아이디 찾기: 인증 코드 전송
    @PostMapping("/sendCode")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("유저를 찾을 수 없습니다.");
        }
        String code = verificationService.generateVerificationCode(email);
        emailService.sendEmail(email, "[우정플랜트] 이메일 인증을 위한 인증번호입니다.", "인증번호 :  " + code);
        return ResponseEntity.ok("인증번호를 전송했습니다.");
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        if (verificationService.verifyCode(email, code)) {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok("유저의 ID: " + user.getUserID());
        } else {
            return ResponseEntity.badRequest().body("인증번호가 잘못되었습니다.");
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String email = request.getEmail();
        String code = request.getCode();
        String newPassword = request.getNewPassword();

        if (verificationService.verifyCode(email, code)) {
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("유저를 찾을 수 없습니다.");
            }
            user.setUserPassword(newPassword); // 기존 userPassword 값을 업데이트
            userService.saveUser(user);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 잘못되었습니다.");
        }
    }
}
