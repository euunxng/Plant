package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.dto.LoginRequest;
import com.example.project.dto.LoginResponse;
import com.example.project.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        String userID = request.getUserID();
        String userPassword = request.getUserPassword();

        User user = loginService.login(userID, userPassword);

        if (user != null) {
            session.setAttribute("user", user);
            return ResponseEntity.ok(new LoginResponse(true, "Login successful", user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Invalid username or password", null));
        }
    }
}
