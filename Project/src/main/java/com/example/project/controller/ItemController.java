package com.example.project.controller;

import com.example.project.domain.Item;
import com.example.project.dto.ItemDto;
import com.example.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/{groupId}/getcoin")
    public int getCoinByGroupId(@PathVariable("groupId") Long groupId) {
        return itemService.getCoinByGroupId(groupId);
    }

    @GetMapping("/{groupId}/getItem")
    public ResponseEntity<List<ItemDto>> getItemsByGroupId(@PathVariable("groupId") Long groupId) {
        List<ItemDto> items = itemService.getItemsByGroupId(groupId);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/{groupId}/putItem")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("groupId") Long groupId, @RequestBody ItemDto itemDto) {
        try {
            ItemDto updatedItem = itemService.updateItem(groupId, itemDto);
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}