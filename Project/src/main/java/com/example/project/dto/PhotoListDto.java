package com.example.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoListDto {

    @JsonProperty("postId")
    private long postId;

    @JsonProperty("groupId")
    private long groupId;


    @JsonProperty("photoPath")
    private String photoPath;

    @JsonProperty("pdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate pdate;

    @JsonProperty("ptext")
    private String ptext;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("profilePhotoPath")  // 프로필 사진 경로 추가
    private String profilePhotoPath;

}
