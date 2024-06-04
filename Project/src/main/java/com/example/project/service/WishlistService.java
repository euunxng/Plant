package com.example.project.service;

import com.example.project.domain.Wishlist;
import com.example.project.dto.WishlistDto;
import com.example.project.repository.GroupsRepository;
import com.example.project.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final GroupsRepository groupsRepository;

    public List<WishlistDto> getWishlistByGroupId(Long groupId) {
        // 그룹 존재 여부 확인
        if (!groupsRepository.existsById(groupId)) {
            throw new IllegalArgumentException("Group ID " + groupId + " not found");
        }

        // 위시리스트 조회
        List<Wishlist> wishlist = wishlistRepository.findByGroupId(groupId);
        return wishlist.stream()
                .map(wish -> new WishlistDto(
                        wish.getContents(),
                        wish.isComplete()
                ))
                .collect(Collectors.toList());
    }

    public Wishlist createWishlist(Long groupId, WishlistDto wishlistDto) {
        if (isValidContents(wishlistDto.getContents())) {
            Wishlist wishlist = Wishlist.builder()
                    .groupId(groupId)
                    .contents(wishlistDto.getContents())
                    .complete(false)
                    .build();
            return wishlistRepository.save(wishlist);
        } else {
            return null; // or handle appropriately
        }
    }

    private boolean isValidContents(String contents) {
        return contents != null && contents.length() <= 20;
    }

    public void deleteWishlist(Long wishID) {
        wishlistRepository.deleteById(wishID);
    }
}