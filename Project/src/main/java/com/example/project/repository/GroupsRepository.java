package com.example.project.repository;

import com.example.project.domain.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GroupsRepository extends JpaRepository<Groups, Long> {
    List<Groups> findByGroupNameContaining(String keyword);
}
