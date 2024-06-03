package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.service.WithdrawService;
import com.example.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class WithdrawController {

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawService withdrawService;

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(HttpSession session) {
        // 세션에서 현재 로그인된 사용자 정보를 가져옵니다.
        User user = (User) session.getAttribute("user");

        if (user != null) {
            boolean success = withdrawService.deleteUser(user.getUserID());

            if (success) {
                session.invalidate();
                return ResponseEntity.ok("Account deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to delete user account");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not logged in");
        }
    }
}
