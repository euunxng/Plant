package com.example.project.repository;

import com.example.project.domain.Item;
import com.example.project.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByGroupId(Long groupId);
    void deleteByGroupId(Long groupId);
}
