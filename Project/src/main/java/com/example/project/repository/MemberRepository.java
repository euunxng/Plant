package com.example.project.repository;

import com.example.project.domain.Member;
import com.example.project.domain.MemberId;
import com.example.project.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
    boolean existsByGroupIdAndUserID(Long groupId, String userId);
    long countByGroupId(Long groupId);
    long countByUserID(String userID);
    List<Member> findByUserID(String userID);
    List<Member> findByUser(User user);
    List<Member> findByGroupId(Long groupId);
    void deleteByGroupIdAndUserID(Long groupId, String userID);
    boolean existsByGroupId(Long groupId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Member m WHERE m.userID = :userID")
    default void deleteByUserID(@Param("userID") String userID) {

    }
    @Modifying
    @Transactional
    @Query("DELETE FROM Member m WHERE m.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);
}
