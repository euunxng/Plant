package com.example.project.service;

import com.example.project.domain.Item;
import com.example.project.dto.ItemDto;
import com.example.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public int getCoinByGroupId(Long groupId) {
        Item item = itemRepository.findByGroup_GroupId(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));
        return item.getCoin();
    }

    // item리스트 가져오기
    public List<ItemDto> getItemsByGroupId(Long groupId) {
        List<Item> items = itemRepository.findByGroupId(groupId);
        return items.stream()
                .map(item -> ItemDto.builder()
                        .coin(item.getCoin())
                        .water(item.getWater())
                        .rabbit(item.isRabbit())
                        .seed(item.getSeed())
                        .stone(item.isStone())
                        .energy1(item.getEnergy1())
                        .energy2(item.getEnergy2())
                        .energy3(item.getEnergy3())
                        .cuteStone(item.isCuteStone())
                        .ladybug(item.isLadybug())
                        .title(item.isTitle())
                        .bfly(item.isBfly())
                        .build())
                .collect(Collectors.toList());
    }

    public ItemDto updateItem(Long groupId, ItemDto itemDto) {
        Item item = itemRepository.findByGroup_GroupId(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));

        item.setCoin(itemDto.getCoin());
        item.setWater(itemDto.getWater());
        item.setRabbit(itemDto.isRabbit());
        item.setSeed(itemDto.getSeed());
        item.setStone(itemDto.isStone());
        item.setEnergy1(itemDto.getEnergy1());
        item.setEnergy2(itemDto.getEnergy2());
        item.setEnergy3(itemDto.getEnergy3());
        item.setCuteStone(itemDto.isCuteStone());
        item.setLadybug(itemDto.isLadybug());
        item.setTitle(itemDto.isTitle());
        item.setBfly(itemDto.isBfly());

        Item updatedItem = itemRepository.save(item);

        return ItemDto.builder()
                .coin(updatedItem.getCoin())
                .water(updatedItem.getWater())
                .rabbit(updatedItem.isRabbit())
                .seed(updatedItem.getSeed())
                .stone(updatedItem.isStone())
                .energy1(updatedItem.getEnergy1())
                .energy2(updatedItem.getEnergy2())
                .energy3(updatedItem.getEnergy3())
                .cuteStone(updatedItem.isCuteStone())
                .ladybug(updatedItem.isLadybug())
                .title(updatedItem.isTitle())
                .bfly(updatedItem.isBfly())
                .build();
    }
}
