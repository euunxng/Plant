package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userID;
    private String userName;
    private String userPassword;
    private String email;
    private MultipartFile profilePhoto; // 이미지 파일 저장
    private MultipartFile userFace; // 이미지 파일 저장
    private boolean login; // 로그인 여부
    private String profilePhotoPath; // 프로필 사진 경로
    private String userFacePath; // 얼굴 사진 경로
}
