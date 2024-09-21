package com.example.project.service;

import com.example.project.domain.Comment;
import com.example.project.dto.cmtViewDto;
import com.example.project.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment saveComment(String userID, Long postId, String cmtText) { // postID -> postId
        Comment comment = Comment.builder()
                .userID(userID)
                .postId(postId) // postID -> postId
                .cmtText(cmtText)
                .cmtDate(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long cmtID, String userID) {
        Optional<Comment> comment = commentRepository.findById(cmtID);
        if (comment.isPresent() && comment.get().getUserID().equals(userID)) {
            commentRepository.deleteByCmtIDAndUserID(cmtID, userID);
        } else {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
    }

    public List<cmtViewDto> getCommentsByPostId(Long postId) { // 수정
        List<Comment> comments = commentRepository.findByPostId(postId); // 수정
        return comments.stream().map(comment -> {
            LocalDateTime cmtDate = comment.getCmtDate().withNano(0);
            return cmtViewDto.builder()
                    .cmtID(comment.getCmtID())
                    .userID(comment.getUserID())
                    .cmtText(comment.getCmtText())
                    .cmtDate(cmtDate)
                    .build();
        }).collect(Collectors.toList());
    }
}