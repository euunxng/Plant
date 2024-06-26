package com.example.project.repository;

import com.example.project.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    void deleteByGroupId(Long groupId);
    Optional<Item> findByGroup_GroupId(Long groupId);
    List<Item> findByGroupId(Long groupId);
    @Query("SELECT s.seed FROM Item s WHERE s.groupId = :groupId")
    int findSeedCountByGroupId(@Param("groupId") Long groupId);
}