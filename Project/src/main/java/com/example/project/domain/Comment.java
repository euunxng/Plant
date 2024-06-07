package com.example.project.domain;

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
    private User user;

    @Column(name = "postID", nullable = false)
    private Long postID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postID", referencedColumnName = "postID", insertable = false, updatable = false)
    private Post post;

    @Column(name="cmtText", length=60)
    private String cmtText;

    @Column(name="cmtDate", nullable = false)
    private LocalDateTime cmtDate;
}
