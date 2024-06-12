package com.example.project.repository;

import com.example.project.domain.Member;
import com.example.project.domain.MemberId;
import com.example.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
