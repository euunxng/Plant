package com.example.project.repository;

import com.example.project.domain.Comment;
import com.example.project.domain.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId); // 수정된 메서드 이름

    Optional<Comment> findById(Long cmtID);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.cmtID = :cmtID AND c.userID = :userID")
    void deleteByCmtIDAndUserID(@Param("cmtID") Long cmtID, @Param("userID") String userID);

    void deleteByPost(Post post);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.post.group.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);
}