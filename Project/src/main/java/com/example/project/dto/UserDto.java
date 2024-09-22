package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userID;
    private String userName;
    private String userPassword;
    private String email;
    private String profilePhotoUrl; // 프로필 사진 URL
    private String userFaceUrl; // 얼굴 사진 URL
    private boolean login; // 로그인 여부
    private boolean kakao; // 로그인 여부
}
