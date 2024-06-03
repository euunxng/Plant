package com.example.project.service;

import com.example.project.domain.User;
import com.example.project.dto.UserDto;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.project.dto.LoginRequest;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> save(UserDto userRequest) {
        if (userRepository.existsByUserID(userRequest.getUserID())) {
            throw new IllegalArgumentException("UserID " + userRequest.getUserID() + "가 이미 있습니다. ");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("email " + userRequest.getEmail() + " 가 이미 있습니다. ");
        }
        String password = userRequest.getUserPassword();
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 숫자와 영어를 모두 포함하고, 5-10자여야합니다.");
        }
        String email = userRequest.getEmail();
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        User newUser = User.builder()
                .userID(userRequest.getUserID())
                .userName(userRequest.getUserName())
                .userPassword(userRequest.getUserPassword())
                .email(userRequest.getEmail())
                .profilePhoto(userRequest.getProfilePhoto())
                .userFace(userRequest.getUserFace())
                .login(userRequest.isLogin())
                .build();

        return ResponseEntity.ok().body(userRepository.save(newUser));
    }

    public ResponseEntity<?> login(LoginRequest loginRequestDto) {
        User user = userRepository.findByUserID(loginRequestDto.getUserID());
        if (user == null || !user.getUserPassword().equals(loginRequestDto.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } else {
            // 로그인 성공 시 유저 정보 반환
            return ResponseEntity.ok().body(user);
        }
    }

    public ResponseEntity<?> getUserInfo(String userID) {
        User user = userRepository.findByUserID(userID);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            return ResponseEntity.ok().body(user);
        }
    }

    public UserDto getUserInfo(User user) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 필요한 정보만 DTO로 변환하여 반환
        return UserDto.builder()
                .userID(userInfo.getUserID())
                .userPassword(userInfo.getUserPassword())
                .userName(userInfo.getUserName())
                .profilePhoto(userInfo.getProfilePhoto())
                .userFace(userInfo.getUserFace())
                .email(userInfo.getEmail())
                .build();
    }


    private boolean isValidPassword(String password) {
        // 비밀번호는 영문자와 숫자를 반드시 포함해야 함
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            return false;
        }
        // 비밀번호는 5자 이상 10자 이하이어야 함
        return password.length() >= 5 && password.length() <= 10;
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        return email.matches(emailPattern);
    }
}