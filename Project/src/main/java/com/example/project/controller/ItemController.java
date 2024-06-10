package com.example.project.controller;

import com.example.project.domain.Item;
import com.example.project.dto.ItemDto;
import com.example.project.dto.waterDto;
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

    @GetMapping("/getWaterCountByGroupId")
    public List<waterDto> getWaterCountByGroupId(@RequestParam("groupId") Long groupId) {
        return itemService.getWaterCountByGroupId(groupId);
    }

    @PutMapping("/updateWaterCountByGroupId")
    public waterDto updateWaterCountByGroupId(@RequestParam("groupId") Long groupId,
                                              @RequestBody waterDto newWaterDto) {
        return itemService.updateWaterCountByGroupId(groupId, newWaterDto);
    }

    @PostMapping("/putItem")
    public ResponseEntity<Item> createItem(@RequestParam("groupId") Long groupId) {
        Item newItem = itemService.createItem(groupId);
        return ResponseEntity.ok(newItem);
    }
}