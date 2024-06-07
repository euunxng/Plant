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
    public Comment saveComment(String userID, Long postID, String cmtText) {
        Comment comment = Comment.builder()
                .userID(userID)
                .postID(postID)
                .cmtText(cmtText)
                .cmtDate(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long cmtID, String userID) {
        Optional<Comment> comment = commentRepository.findByCmtID(cmtID);
        if (comment.isPresent() && !comment.get().getUserID().equals(userID)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        commentRepository.deleteByCmtIDAndUserID(cmtID, userID);
    }

    public List<cmtViewDto> getCommentsByPostID(Long postID) {
        List<Comment> comments = commentRepository.findByPostID(postID);
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
