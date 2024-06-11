package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.request.LoginRequest;
import com.example.project.dto.UserDto;
import com.example.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
            @RequestParam("userFaceUrl") String userFaceUrl) {
        UserDto userRequest = UserDto.builder()
                .userID(userID)
                .userName(userName)
                .userPassword(userPassword)
                .email(email)
                .profilePhotoUrl(profilePhotoUrl)
                .userFaceUrl(userFaceUrl)
                .login(true)
                .build();
        return userService.save(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequestDto) {
        return userService.login(loginRequestDto);
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
}
