package com.example.project.service;

import com.example.project.domain.Groups;
import com.example.project.dto.PlantDto;
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
}
