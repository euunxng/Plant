package com.example.project.dto;

import com.example.project.domain.CalenderId;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(CalenderId.class)
public class cCompleteDto {

    private boolean complete;

}