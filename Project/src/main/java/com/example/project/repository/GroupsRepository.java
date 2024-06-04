package com.example.project.repository;

import com.example.project.domain.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {
    List<Groups> findByGroupNameContaining(String keyword);
    boolean existsById(Long groupId);
}
