package com.example.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", referencedColumnName = "groupId", insertable = false, updatable = false)
    private Groups group;

    @Column(name = "groupId", nullable = false)
    private Long groupId;

    @Column(name = "userID", nullable = false)
    private String userID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", referencedColumnName = "userID", insertable = false, updatable = false)
    private User user;

    @Column(name="ptext", length=300, nullable = false)
    private String ptext;

    @Column(name="photoPath", length=10485760) // 10MB 크기의 Base64 인코딩된 문자열을 허용
    private String photoPath;

    @Column(name="pdate", nullable = false)
    private LocalDate pdate;
}