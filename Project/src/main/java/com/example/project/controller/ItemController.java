package com.example.project.controller;

import com.example.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/{groupId}/coin")
    public int getCoinByGroupId(@PathVariable("groupId") Long groupId) {
        return itemService.getCoinByGroupId(groupId);
    }
}