package com.example.project.service;

import com.example.project.domain.Groups;
import com.example.project.dto.PlantDto;
import com.example.project.dto.PlantGaugeDto;
import com.example.project.dto.PlantTypeDto;
import com.example.project.repository.GroupsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final GroupsRepository groupsRepository;

    public PlantDto getPlantDetails(Long groupId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));
        return new PlantDto(group.getGrowStep(), group.getPlantGauge(), group.getPlantType());
    }

    public PlantDto updatePlantDetails(Long groupId, PlantDto plantDto) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));

        group.setGrowStep(plantDto.getGrowStep());
        group.setPlantGauge(plantDto.getPlantGauge());
        group.setPlantType(plantDto.getPlantType());

        Groups updatedGroup = groupsRepository.save(group);

        return new PlantDto(updatedGroup.getGrowStep(), updatedGroup.getPlantGauge(), updatedGroup.getPlantType());
    }


    public PlantTypeDto getPlantType(Long groupId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));
        return PlantTypeDto.builder()
                .plantType(group.getPlantType())
                .build();
    }

    public PlantTypeDto updatePlantType(Long groupId, PlantTypeDto plantTypeDto) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));

        group.setPlantType(plantTypeDto.getPlantType());

        Groups updatedGroup = groupsRepository.save(group);

        return new PlantTypeDto(updatedGroup.getPlantType());
    }


    public PlantGaugeDto getPlantGauge(Long groupId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));
        return PlantGaugeDto.builder()
                .plantGauge(group.getPlantGauge())
                .build();
    }

    public PlantGaugeDto updatePlantGauge(Long groupId, PlantGaugeDto plantGaugeDto) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹이 존재하지 않습니다."));

        group.setPlantGauge(plantGaugeDto.getPlantGauge());

        Groups updatedGroup = groupsRepository.save(group);

        return new PlantGaugeDto(updatedGroup.getPlantGauge());
    }
}
