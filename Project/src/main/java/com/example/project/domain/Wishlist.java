package com.example.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishID;

    @Column(name = "groupId")
    private Long groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private Groups group;

    @Column(name = "contents", length = 60, nullable = false)
    private String contents;

    @Builder.Default
    @Column(name = "complete", nullable = false)
    private boolean complete=false;
}
