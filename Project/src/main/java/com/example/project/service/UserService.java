package com.example.project.service;

import com.example.project.domain.User;
import com.example.project.dto.UserDto;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.project.dto.LoginRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public ResponseEntity<?> save(UserDto userRequest) {
        try {
            logger.info("Attempting to save user: {}", userRequest);

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

            // 이미지를 파일로 저장하고 경로를 얻음
            String profilePhotoPath = saveImageToFile(userRequest.getProfilePhoto(), userRequest.getUserID() + "_profile.jpg");
            String userFacePath = saveImageToFile(userRequest.getUserFace(), userRequest.getUserID() + "_face.jpg");

            User newUser = User.builder()
                    .userID(userRequest.getUserID())
                    .userName(userRequest.getUserName())
                    .userPassword(userRequest.getUserPassword())
                    .email(userRequest.getEmail())
                    .profilePhotoPath(profilePhotoPath)
                    .userFacePath(userFacePath)
                    .login(userRequest.isLogin())
                    .build();

            logger.info("Saving user to database: {}", newUser);
            User savedUser = userRepository.save(newUser);
            logger.info("User saved successfully: {}", savedUser);

            return ResponseEntity.ok().body(savedUser);
        } catch (Exception e) {
            logger.error("Error saving user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the user: " + e.getMessage());
        }
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
                .profilePhotoPath(userInfo.getProfilePhotoPath())
                .userFacePath(userInfo.getUserFacePath())
                .email(userInfo.getEmail())
                .login(userInfo.isLogin())
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
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0.-]+\\.[A-Z|a-z]{2,6}$";
        return email.matches(emailPattern);
    }

    public boolean checkUserID(String userID) {
        return userRepository.existsById(userID);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private String saveImageToFile(MultipartFile file, String fileName) throws IOException {
        File storageDir = new File("/path/to/images/directory/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();  // 디렉토리가 없으면 생성
        }
        File imageFile = new File(storageDir, fileName);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(file.getBytes());
        }
        return imageFile.getAbsolutePath();
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
