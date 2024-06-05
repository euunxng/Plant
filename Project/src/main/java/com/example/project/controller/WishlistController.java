package com.example.project.controller;

import com.example.project.domain.Wishlist;
import com.example.project.dto.WishlistDto;
import com.example.project.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;


    @PostMapping("/Wishlist")
    public List<WishlistDto> getWishlistByGroupId(@RequestParam("groupId") Long groupId) {
        return wishlistService.getWishlistByGroupId(groupId);
    }

    @PostMapping("/CreateWish")
    public ResponseEntity<Wishlist> createWishlist(@RequestParam("groupId") Long groupId, @RequestParam("contents") String contents) {
        WishlistDto wishlistDto = WishlistDto.builder()
                .contents(contents)
                .complete(false)
                .build();
        Wishlist createdWishlist = wishlistService.createWishlist(groupId, wishlistDto);
        if (createdWishlist != null) {
            return ResponseEntity.ok(createdWishlist);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/DELETE/{wishID}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable("wishID") Long wishID) {
        wishlistService.deleteWishlist(wishID);
        return ResponseEntity.noContent().build();
    }

}
