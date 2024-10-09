package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.request.EmailRequest;
import com.example.project.request.LoginRequest;
import com.example.project.request.VerifyRequest;
import com.example.project.dto.UserDto;
import com.example.project.service.EmailService;
import com.example.project.service.UserService;
import com.example.project.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.example.project.service.UserService.logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Value("${file.upload.profile}")
    private String uploadProfileDir;

    @Value("${file.upload.face}")
    private String uploadFaceDir;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> save(
            @RequestParam("userID") String userID,
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword,
            @RequestParam("email") String email,
            @RequestParam("profilePhotoUrl") String profilePhotoUrl,
            @RequestParam("userFaceUrl") String userFaceUrl,
            @RequestParam(value = "kakao", required = false) Boolean kakao
    ) {
        // kakao가 null일 경우 false로 설정
        if (kakao == null) {
            kakao = false;
        }
        logger.info("userID: {}", userID != null ? userID : "null");
        logger.info("userName: {}", userName != null ? userName : "null");
        logger.info("email: {}", email != null ? email : "null");
        logger.info("userPassword: {}", userPassword != null ? userPassword : "null");

        UserDto userRequest = UserDto.builder()
                .userID(userID)
                .userName(userName)
                .userPassword(userPassword)
                .email(email)
                .profilePhotoUrl(profilePhotoUrl)
                .userFaceUrl(userFaceUrl)
                .login(true)
                .kakao(kakao)  // 명시된 kakao 값 사용
                .build();

        logger.info("Received data - userID: {}, userName: {}, email: {}, userPassword: {}, kakao: {}", userID, userName, email, userPassword, kakao);
        return userService.save(userRequest);
    }


    @GetMapping("/userID/{userID}")
    public ResponseEntity<?> checkUserID(@PathVariable("userID") String userID) {
        boolean exists = userService.checkUserID(userID);
        if (exists) {
            return ResponseEntity.status(200).build(); // 200 OK - 중복됨
        } else {
            return ResponseEntity.status(404).build(); // 404 Not Found - 중복되지 않음
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable("email") String email) {
        boolean exists = userService.checkEmail(email);
        if (exists) {
            return ResponseEntity.status(200).build(); // 200 OK - 중복됨
        } else {
            return ResponseEntity.status(404).build(); // 404 Not Found - 중복되지 않음
        }
    }

    @GetMapping("/{userID}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userID") String userID) {
        return userService.getUserInfo(userID);
    }

    @GetMapping("/myPage")
    public UserDto getMyPageInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }
        return userService.getUserInfo(user);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<UserDto> getUserInfoByToken(@RequestParam("token") String token) {
        UserDto user = userService.getUserByToken(token); // 서비스에서 토큰을 통해 사용자 조회
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(value = "/uploadProfileImage", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, uploadProfileDir, "/uploads/profile/");
    }

    @PostMapping(value = "/uploadFaceImage", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFaceImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, uploadFaceDir, "/uploads/face/");
    }

    private ResponseEntity<String> uploadImage(MultipartFile file, String uploadDir, String uriPath) {
        try {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IOException("File name is null");
            }

            String fileExtension = "";
            int lastDotIndex = originalFileName.lastIndexOf(".");
            if (lastDotIndex != -1) {
                fileExtension = originalFileName.substring(lastDotIndex);
            } else {
                fileExtension = ".jpg";
            }

            String uniqueFileName = UUID.randomUUID().toString() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + fileExtension;

            File uploadDirPath = new File(uploadDir);
            if (!uploadDirPath.exists()) {
                if (!uploadDirPath.mkdirs()) {
                    throw new IOException("Failed to create directory: " + uploadDirPath.getAbsolutePath());
                }
            }

            File uploadedFile = new File(uploadDirPath, uniqueFileName);
            try (FileOutputStream fos = new FileOutputStream(uploadedFile)) {
                fos.write(file.getBytes());
            }

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(uriPath)
                    .path(uniqueFileName)
                    .toUriString();

            logger.info("File uploaded to: " + uploadedFile.getAbsolutePath());
            logger.info("File accessible at: " + fileDownloadUri);

            return ResponseEntity.ok(fileDownloadUri);

        } catch (IOException e) {
            logger.error("Could not upload the file: ", e);
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        }
    }

    @PostMapping("/sendSignupCode")
    public ResponseEntity<String> sendSignupCode(@RequestBody EmailRequest emailRequest) {
        String email = emailRequest.getEmail();
        if (userService.checkEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        }
        String code = verificationService.generateVerificationCode(email);
        emailService.sendEmail(email, "[우정플랜트] 회원가입 이메일 인증 코드", "인증코드는 " + code);
        return ResponseEntity.ok("Verification code sent.");
    }

    @PostMapping("/verifySignupCode")
    public ResponseEntity<String> verifySignupCode(@RequestBody VerifyRequest verifyRequest) {
        boolean isVerified = verificationService.verifyCode(verifyRequest.getEmail(), verifyRequest.getCode());
        if (isVerified) {
            return ResponseEntity.ok("Verification successful.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed.");
        }
    }


}
