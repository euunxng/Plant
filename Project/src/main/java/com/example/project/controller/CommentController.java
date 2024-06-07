package com.example.project.controller;

import com.example.project.domain.Comment;
import com.example.project.domain.User;
import com.example.project.dto.cmtViewDto;
import com.example.project.repository.CommentRepository;
import com.example.project.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @PostMapping("/api/postComments")
    public ResponseEntity<Comment> addComment(HttpSession session, @RequestParam Long postID, @RequestParam String cmtText) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        String userID = user.getUserID();
        Comment savedComment = commentService.saveComment(userID, postID, cmtText);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/api/deleteComments")
    public ResponseEntity<String> deleteComment(HttpSession session, @RequestParam Long cmtID) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        try {
            commentService.deleteComment(cmtID, user.getUserID());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }


    @GetMapping("/api/getComments")
    public ResponseEntity<List<cmtViewDto>> getCommentsByPostID(@RequestParam Long postID) {
        List<cmtViewDto> comments = commentService.getCommentsByPostID(postID);
        return ResponseEntity.ok(comments);
    }
}
