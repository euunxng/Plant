package com.example.project.repository;

import com.example.project.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List; // List 클래스를 임포트
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.postId = :postId")
    Optional<Post> findPostWithUserById(@Param("postId") Long postId);

    List<Post> findByGroup_GroupId(Long groupId);
    boolean existsByUserID(String userID);
}