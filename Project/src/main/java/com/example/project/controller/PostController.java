package com.example.project.controller;

import com.example.project.domain.User;
import com.example.project.dto.PhotoList;
import com.example.project.dto.PostDto;
import com.example.project.dto.PostViewDto;
import com.example.project.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.UUID;

import static com.example.project.service.UserService.logger;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;
    @Value("${file.upload.post}")
    private String uploadPostDir;

    @PostMapping(value = "/api/addPostWithImage", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDto> createPostWithImage(HttpSession session,
                                                       @RequestPart("postDto") String postDtoString,
                                                       @RequestPart("file") MultipartFile file) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            PostDto postDto = objectMapper.readValue(postDtoString, PostDto.class);
            String fileDownloadUri = uploadImage(file, uploadPostDir, "/uploads/post/");
            if (fileDownloadUri == null) {
                return ResponseEntity.status(500).body(null);
            }
            postDto.setPhotoPath(fileDownloadUri);
            PostDto createdPost = postService.createPost(postDto, user.getUserID());
            return ResponseEntity.ok(createdPost);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private String uploadImage(MultipartFile file, String uploadPostDir, String uriPath) {
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
            File uploadDirPath = new File(uploadPostDir);
            if (!uploadDirPath.exists()) {
                if (!uploadDirPath.mkdirs()) {
                    throw new IOException("Failed to create directory: " + uploadDirPath.getAbsolutePath());
                }
            }
            File uploadedFile = new File(uploadDirPath, uniqueFileName);
            try (FileOutputStream fos = new FileOutputStream(uploadedFile)) {
                fos.write(file.getBytes());
            }
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(uriPath)
                    .path(uniqueFileName)
                    .toUriString();
        } catch (IOException e) {
            logger.error("Could not upload the file: ", e);
            return null;
        }
    }

    @GetMapping("/View/{postId}")
    public ResponseEntity<PostViewDto> getPostById(@PathVariable Long postID) {
        PostViewDto postViewDto = postService.getPostById(postID);
        return ResponseEntity.ok(postViewDto);
    }

    @GetMapping("/List/{groupId}")
    public ResponseEntity<List<PhotoList>> getPhotosByGroupId(@RequestParam("groupId") Long groupId) {
        List<PhotoList> photos = postService.getPhotosByGroupId(groupId);
        return ResponseEntity.ok(photos);
    }

    @DeleteMapping("/delete/{postID}")
    public ResponseEntity<String> deletePost(HttpSession session, @PathVariable("postID") Long postID) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            postService.deletePost(postID, user.getUserID());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }
}
