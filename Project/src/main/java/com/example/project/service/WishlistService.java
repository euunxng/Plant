package com.example.project.service;

import com.example.project.domain.Calender;
import com.example.project.domain.CalenderId;
import com.example.project.domain.Wishlist;
import com.example.project.dto.WishlistDto;
import com.example.project.dto.cCompleteDto;
import com.example.project.repository.GroupsRepository;
import com.example.project.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
                        wish.getWishID(),
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
            return null;
        }
    }

    private boolean isValidContents(String contents) {
        return contents != null && contents.length() <= 20;
    }

    public void deleteWishlist(Long wishID) {
        wishlistRepository.deleteById(wishID);
    }

    @Transactional
    public WishlistDto updateCompleteByWishID(Long wishID) {
        Optional<Wishlist> wishlistOptional = wishlistRepository.findById(wishID);

        if (wishlistOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 wishID에 해당하는 위시리스트가 없습니다: " + wishID);
        }

        Wishlist wishlist = wishlistOptional.get();
        wishlist.setComplete(!wishlist.isComplete()); // complete 값을 반대로 설정

        wishlistRepository.save(wishlist);

        return WishlistDto.builder()
                .wishID(wishlist.getWishID())
                .contents(wishlist.getContents())
                .complete(wishlist.isComplete())
                .build();
    }

}