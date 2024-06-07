package com.example.project.dto;

import com.example.project.domain.Post;
import com.example.project.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class cmtViewDto {

    private Long cmtID;
    private String userID;
    private String cmtText;
    private LocalDateTime cmtDate;

}
