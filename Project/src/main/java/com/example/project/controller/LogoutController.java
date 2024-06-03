package com.example.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}