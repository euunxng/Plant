package com.example.project.response;

import com.example.project.domain.User;
import lombok.Data;

@Data
public class LoginResponse {
    // 성공 여부를 가져오는 메서드
    private boolean success;
    // 메시지를 가져오는 메서드
    private String message;
    // 사용자 정보를 가져오는 메서드
    private User user;

    // 기본 생성자
    public LoginResponse() {}

    // 생성자
    public LoginResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // 성공 여부를 설정하는 메서드
    public void setSuccess(boolean success) {
        this.success = success;
    }

    // 메시지를 설정하는 메서드
    public void setMessage(String message) {
        this.message = message;
    }

    // 사용자 정보를 설정하는 메서드
    public void setUser(User user) {
        this.user = user;
    }

    // toString 메서드 (디버깅을 위해)
    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}
