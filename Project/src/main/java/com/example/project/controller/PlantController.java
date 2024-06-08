package com.example.project.controller;

import com.example.project.dto.PlantDto;
import com.example.project.dto.PlantGaugeDto;
import com.example.project.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/{groupId}/getPlant")
    public PlantDto getPlantDetails(@PathVariable("groupId") Long groupId) {
        return plantService.getPlantDetails(groupId);
    }

    @PutMapping("/{groupId}/putPlant")
    public PlantDto updatePlantDetails(@PathVariable("groupId") Long groupId, @RequestBody PlantDto plantDto) {
        return plantService.updatePlantDetails(groupId, plantDto);
    }

    @PutMapping("/{groupId}/IncPlantGauge")
    public PlantGaugeDto updatePlantGauge(@PathVariable("groupId") Long groupId, @RequestBody PlantGaugeDto plantGaugeDto) {
        return plantService.updatePlantGauge(groupId, plantGaugeDto);
    }
}
