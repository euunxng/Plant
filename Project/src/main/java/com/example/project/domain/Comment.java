package com.example.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmtID;

    @Column(name = "userID", nullable = false)
    private String userID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", referencedColumnName = "userID", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    @Column(name = "postId", nullable = false)  // 수정됨
    private Long postId;  // 수정됨

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId", insertable = false, updatable = false)  // 수정됨
    @JsonIgnore
    private Post post;

    @Column(name="cmtText", length=60)
    private String cmtText;

    @Column(name="cmtDate", nullable = false)
    private LocalDateTime cmtDate;
}