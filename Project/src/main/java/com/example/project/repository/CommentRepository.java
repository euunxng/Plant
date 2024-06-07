package com.example.project.repository;

import com.example.project.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostID(Long postID);
    void deleteByCmtIDAndUserID(Long cmtID, String userID);
    Optional<Comment> findByCmtID(Long cmtID);
}