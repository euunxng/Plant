package com.example.project.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupsDto {

    private Long groupId;
    private String groupName;
    private int groupPassword;
    private int plantGauge;
    private int plantType;
    private int growStep;
}