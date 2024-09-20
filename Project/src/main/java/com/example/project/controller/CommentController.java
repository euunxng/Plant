package com.example.project.controller;

import com.example.project.domain.Comment;
import com.example.project.domain.User;
import com.example.project.dto.cmtViewDto;
import com.example.project.repository.CommentRepository;
import com.example.project.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/api/postComments")
    public ResponseEntity<Comment> addComment(HttpSession session, @RequestParam("postID") Long postID, @RequestParam("cmtText") String cmtText) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.error("User is not logged in.");
            return ResponseEntity.status(401).body(null);
        }
        logger.info("User {} is adding a comment.", user.getUserID());
        String userID = user.getUserID();
        Comment savedComment = commentService.saveComment(userID, postID, cmtText);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/api/deleteComments")
    public ResponseEntity<String> deleteComment(HttpSession session, @RequestParam("cmtID") Long cmtID) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.error("User is not logged in.");
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        logger.info("User {} is deleting a comment with ID {}", user.getUserID(), cmtID);
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
    public ResponseEntity<List<cmtViewDto>> getCommentsByPostId(@RequestParam("postId") Long postId) {  // 여기서 postId로 통일
        List<cmtViewDto> comments = commentService.getCommentsByPostId(postId);  // postId로 통일
        return ResponseEntity.ok(comments);
    }

}
