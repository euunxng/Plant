package com.example.project.dto;

import jakarta.validation.constraints.NotBlank;


public class LoginRequest {
    @NotBlank
    private String userID; // 변경된 부분

    @NotBlank
    private String userPassword; // 변경된 부분

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
