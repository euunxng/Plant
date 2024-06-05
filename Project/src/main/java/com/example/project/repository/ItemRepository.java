package com.example.project.repository;

import com.example.project.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByGroup_GroupId(Long groupId);
    List<Item> findByGroupId(Long groupId);
}