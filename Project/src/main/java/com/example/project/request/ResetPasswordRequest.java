package com.example.project.request;


public class ResetPasswordRequest {
    private String email;
    private String code;
    private String newPassword;

    // 기본 생성자
    public ResetPasswordRequest() {
    }

    // 매개변수가 있는 생성자
    public ResetPasswordRequest(String email, String code, String newPassword) {
        this.email = email;
        this.code = code;
        this.newPassword = newPassword;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for code
    public String getCode() {
        return code;
    }

    // Setter for code
    public void setCode(String code) {
        this.code = code;
    }

    // Getter for newPassword
    public String getNewPassword() {
        return newPassword;
    }

    // Setter for newPassword
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

