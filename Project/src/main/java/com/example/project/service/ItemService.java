package com.example.project.service;

import com.example.project.domain.Item;
import com.example.project.dto.ItemDto;
import com.example.project.dto.SeedDto;
import com.example.project.dto.coinDto;
import com.example.project.dto.waterDto;
import com.example.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Transactional
    public coinDto updateCoinByGroupId(Long groupId) {
        Optional<Item> itemOptional = itemRepository.findByGroupId(groupId).stream().findFirst();

        if (itemOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 그룹 ID에 해당하는 아이템이 없습니다: " + groupId);
        }

        Item item = itemOptional.get();

        // 기존 코인 양에 3 추가
        int updatedCoin = item.getCoin() + 3;
        item.setCoin(updatedCoin);

        itemRepository.save(item);

        return new coinDto(item.getCoin());
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

    @Transactional
    public List<waterDto> getWaterCountByGroupId(Long groupId) {
        List<Item> items = itemRepository.findByGroupId(groupId);

        if (items.isEmpty()) {
            throw new IllegalArgumentException("해당 그룹 ID에 해당하는 아이템이 없습니다: " + groupId);
        }

        return items.stream()
                .map(item -> new waterDto(item.getWater()))
                .collect(Collectors.toList());
    }

    public waterDto updateWaterCountByGroupId(Long groupId, waterDto newWaterDto) {
        Optional<Item> itemOptional = itemRepository.findByGroupId(groupId).stream().findFirst();

        if (itemOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 그룹 ID에 해당하는 아이템이 없습니다: " + groupId);
        }

        Item item = itemOptional.get();
        item.setWater(newWaterDto.getWater());

        itemRepository.save(item);

        return new waterDto(item.getWater());
    }

    public Item createItem(Long groupId) {
        Item item = Item.builder().groupId(groupId).build();
        return itemRepository.save(item);
    }

    @Transactional
    public void resetSeedByGroupId(Long groupId) {
        List<Item> items = itemRepository.findByGroupId(groupId);
        if (items == null || items.isEmpty()) {
            System.out.println("No items found for groupId: " + groupId);
            return;
        }

        System.out.println("Found items: " + items.size());
        for (Item item : items) {
            System.out.println("Item before reset: " + item);
            item.setSeed(0);
            System.out.println("Item after reset: " + item);
        }

        List<Item> savedItems = itemRepository.saveAll(items);
        System.out.println("Items saved: " + savedItems);
    }

    public SeedDto getSeedByGroupId(Long groupId) {
        int seedCount = itemRepository.findSeedCountByGroupId(groupId);
        return new SeedDto(seedCount);
    }
}
