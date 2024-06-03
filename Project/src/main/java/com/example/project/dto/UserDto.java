package com.example.project.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private String userID;
    private String userName;
    private String userPassword;
    private String email;
    private byte[] profilePhoto;
    private byte[] userFace;
    private boolean login;

}