package com.example.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "group_table")
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "groupName", length = 24, nullable = false)
    private String groupName;
    @Column(name = "groupPassword", nullable = false)
    private int groupPassword;
    @Builder.Default
    @Column(name = "plantGauge", nullable = false)
    private int plantGauge = 0;
    @Builder.Default
    @Column(name = "plantType", nullable = false)
    private int plantType = 0;
    @Builder.Default
    @Column(name = "growStep", nullable = false)
    private int growStep = 0;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> member;
}
