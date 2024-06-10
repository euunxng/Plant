package com.example.project.service;

import com.example.project.domain.User;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserModifyService {

    private final UserRepository userRepository;

    @Value("${file.upload.profile}")
    private String uploadProfileDir;

    @Value("${file.upload.face}")
    private String uploadFaceDir;

    @Transactional
    public void updateUserName(User user, String newUsername) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다. 정보 업데이트를 위해 다시 로그인 해주세요."));

        // 닉네임 업데이트
        userInfo.setUserName(newUsername);
        userRepository.save(userInfo);
    }

    @Transactional
    public String updateProfilePicture(User user, MultipartFile profilePicture) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다. 정보 업데이트를 위해 다시 로그인 해주세요."));

        // 프로필 사진 업로드 및 경로 저장
        try {
            String originalFileName = profilePicture.getOriginalFilename();
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

            // 정적 자원 디렉토리로 파일 저장 경로 설정
            Path uploadDirPath = Paths.get(uploadProfileDir);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            Path filePath = uploadDirPath.resolve(uniqueFileName);
            Files.write(filePath, profilePicture.getBytes());

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/profile/")
                    .path(uniqueFileName)
                    .toUriString();
            System.out.println("File uploaded to: " + filePath);
            System.out.println("File accessible at: " + fileDownloadUri);

            userInfo.setProfilePhotoPath(fileDownloadUri);
            userRepository.save(userInfo);

            return fileDownloadUri;

        } catch (IOException e) {
            System.err.println("Could not upload the file: " + e.getMessage());
            throw new RuntimeException("프로필 사진 업로드에 실패했습니다.", e);
        }
    }

    @Transactional
    public String updateUserFace(User user, MultipartFile userFace) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다. 정보 업데이트를 위해 다시 로그인 해주세요."));

        // 얼굴 이미지 업로드 및 경로 저장
        try {
            String originalFileName = userFace.getOriginalFilename();
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

            // 얼굴 이미지 저장 경로 설정
            Path uploadDirPath = Paths.get(uploadFaceDir);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            Path filePath = uploadDirPath.resolve(uniqueFileName);
            Files.write(filePath, userFace.getBytes());

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads//face/")
                    .path(uniqueFileName)
                    .toUriString();
            System.out.println("User face image uploaded to: " + filePath);
            System.out.println("User face image accessible at: " + fileDownloadUri);

            userInfo.setUserFacePath(fileDownloadUri);
            userRepository.save(userInfo);

            return fileDownloadUri;

        } catch (IOException e) {
            System.err.println("Failed to upload user face image: " + e.getMessage());
            throw new RuntimeException("얼굴 이미지 업로드에 실패했습니다.", e);
        }
    }
}
