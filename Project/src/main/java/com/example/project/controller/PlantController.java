package com.example.project.controller;

import com.example.project.dto.PlantDto;
import com.example.project.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/{groupId}/Plant")
    public PlantDto getPlantDetails(@PathVariable("groupId") Long groupId) {
        return plantService.getPlantDetails(groupId);
    }
}