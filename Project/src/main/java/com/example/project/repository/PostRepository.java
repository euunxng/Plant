package com.example.project.repository;

import com.example.project.domain.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List; // List 클래스를 임포트
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.postId = :postId")
    Optional<Post> findPostWithUserById(@Param("postId") Long postId);

    List<Post> findByGroup_GroupId(Long groupId);
    List<Post> findByGroup_GroupIdAndUserID(Long groupId, String userID);
    @Modifying
    @Transactional
    @Query("DELETE FROM Post p WHERE p.group.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);
}