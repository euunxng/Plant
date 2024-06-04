package com.example.project.service;

import com.example.project.domain.Item;
import com.example.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public int getCoinByGroupId(Long groupId) {
        Item item = itemRepository.findByGroup_GroupId(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));
        return item.getCoin();
    }
}