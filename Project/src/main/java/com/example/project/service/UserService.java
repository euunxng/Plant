package com.example.project.service;

import com.example.project.domain.User;
import com.example.project.dto.UserDto;
import com.example.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.project.request.LoginRequest;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public ResponseEntity<?> save(UserDto userRequest) {
        try {
            logger.info("Attempting to save user: {}", userRequest);

            if (userRepository.existsByUserID(userRequest.getUserID())) {
                throw new IllegalArgumentException("UserID " + userRequest.getUserID() + "가 이미 있습니다.");
            }
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new IllegalArgumentException("email " + userRequest.getEmail() + " 가 이미 있습니다.");
            }
            // 카카오 로그인인 경우, userPassword를 userID와 동일하게 설정
            if (userRequest.isKakao()) {
                userRequest.setUserPassword(userRequest.getUserID()); // userID와 userPassword 동일하게 설정
            } else {
                // 일반 회원가입인 경우 비밀번호 유효성 검사
                String password = userRequest.getUserPassword();
                if (!isValidPassword(password)) {
                    throw new IllegalArgumentException("비밀번호는 숫자와 영어를 모두 포함하고, 5-10자여야합니다.");
                }
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
                    .profilePhotoPath(userRequest.getProfilePhotoUrl())
                    .userFacePath(userRequest.getUserFaceUrl())
                    .login(userRequest.isLogin())
                    .kakao(userRequest.isKakao())
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

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public ResponseEntity<?> login(LoginRequest loginRequestDto) {
        User user = userRepository.findByUserID(loginRequestDto.getUserID());
        if (user == null || !user.getUserPassword().equals(loginRequestDto.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } else {
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
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        return UserDto.builder()
                .userID(userInfo.getUserID())
                .userPassword(userInfo.getUserPassword())
                .userName(userInfo.getUserName())
                .email(userInfo.getEmail())
                .profilePhotoUrl(userInfo.getProfilePhotoPath())
                .userFaceUrl(userInfo.getUserFacePath())
                .login(userInfo.isLogin())
                .build();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidPassword(String password) {
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            return false;
        }
        return password.length() >= 5 && password.length() <= 10;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    public boolean checkUserID(String userID) {
        return userRepository.existsByUserID(userID);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public User findByUserIDAndEmail(String userID, String email) {
        // 로그 추가
        System.out.println("Searching for user with userID: " + userID + " and email: " + email);
        return userRepository.findByUserIDAndEmail(userID, email);
    }

    @Transactional
    public void deleteUser(String userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userID));
        userRepository.delete(user);
    }

    public UserDto getUserByToken(String token) {
// 토큰으로 사용자 찾기 로직 추가
        User user = userRepository.findByToken(token); // findByToken 메서드를 UserRepository에 추가해야 합니다.
        if (user != null) {
            return UserDto.builder()
                    .userID(user.getUserID())
                    .userName(user.getUserName())
                    .email(user.getEmail())
                    .profilePhotoUrl(user.getProfilePhotoPath())
                    .userFaceUrl(user.getUserFacePath())
                    .login(user.isLogin())
                    .kakao(user.isKakao())
                    .build();
        }
        return null;
    }


}
