package com.example.project.repository;

import com.example.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUserID(String userID);

    boolean existsByEmail(String email);

    User findByUserID(String userID);

    User findByUserIDAndUserPassword(String userID, String userPassword);

    User findByEmail(String email);

    User findByUserIDAndEmail(String userID, String email);

    void deleteByUserID(String userID); // 사용자 ID로 사용자 삭제
}
