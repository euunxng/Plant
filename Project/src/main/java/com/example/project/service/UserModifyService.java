package com.example.project.service;

import com.example.project.domain.User;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.example.project.service.UserService.logger;

@Service
@RequiredArgsConstructor
public class UserModifyService {

    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public void updateUserName(User user, String newUsername) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 닉네임 업데이트
        userInfo.setUserName(newUsername);
        userRepository.save(userInfo);
    }

    @Transactional
    public String updateProfilePicture(User user, MultipartFile profilePicture) {
        // 사용자 정보 가져오기
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

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
            Path uploadDirPath = Paths.get(uploadDir);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            Path filePath = uploadDirPath.resolve(uniqueFileName);
            Files.write(filePath, profilePicture.getBytes());

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/")
                    .path(uniqueFileName)
                    .toUriString();
            logger.info("File uploaded to: " + filePath);
            logger.info("File accessible at: " + fileDownloadUri);

            userInfo.setProfilePhotoPath(fileDownloadUri);
            userRepository.save(userInfo);

            return fileDownloadUri;

        } catch (IOException e) {
            System.err.println("Could not upload the file: " + e.getMessage());
            throw new RuntimeException("프로필 사진 업로드에 실패했습니다.", e);
        }
    }

    @Transactional
    public void updateUserFace(User user, MultipartFile userFace) {
        User userInfo = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 얼굴 이미지 업로드 및 경로 저장
        try {
            String directory = uploadDir + "/face";
            String fileName = UUID.randomUUID().toString() + ".jpg";
            Path path = Paths.get(directory, fileName);

            // 디렉토리가 존재하지 않으면 생성
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
                System.out.println("디렉토리 생성됨: " + path.getParent());
            }

            byte[] bytes = userFace.getBytes();
            Files.write(path, bytes);
            userInfo.setUserFacePath(path.toString());
            userRepository.save(userInfo);
            System.out.println("User face image updated successfully: " + path.toString());
        } catch (IOException e) {
            System.err.println("Failed to upload user face image");
            e.printStackTrace();
            throw new RuntimeException("얼굴 이미지 업로드에 실패했습니다.", e);
        }
    }
}
