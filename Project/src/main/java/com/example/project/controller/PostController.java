package com.example.project.controller;

import com.example.project.domain.Post;

import com.example.project.domain.User;
import com.example.project.dto.PhotoList;
import com.example.project.dto.PostDto;
import com.example.project.dto.PostViewDto;
import com.example.project.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/addPost")
    public ResponseEntity<PostDto> createPost(HttpSession session, @RequestBody PostDto postDto) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        PostDto createdPost = postService.createPost(postDto, user.getUserID());
        return ResponseEntity.ok(createdPost);
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
