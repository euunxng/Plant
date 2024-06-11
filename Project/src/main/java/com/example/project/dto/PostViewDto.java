package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostViewDto {
    @JsonProperty("userID")
    private String userID;

    @JsonProperty("userName")  // 닉네임 필드 추가
    private String userName;

    @JsonProperty("photoPath")
    private String photoPath;

    @JsonProperty("pText")
    private String ptext;

    @JsonProperty("pDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate pdate;
}
