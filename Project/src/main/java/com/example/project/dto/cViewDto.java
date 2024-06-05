package com.example.project.dto;

import com.example.project.domain.CalenderId;
import com.example.project.domain.Groups;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(CalenderId.class)
public class cViewDto {

    private LocalDate cDate;
    private String cName;
    private String time;
    private String place;

    @Builder.Default
    private boolean complete=false;

}