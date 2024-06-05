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
@IdClass(CalenderId.class)
public class Calender {

    @Id
    @Column(name = "groupId", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "cDate", nullable = false)
    private LocalDate cDate;

    @Column(name = "cName", length = 30, nullable = false)
    private String cName;

    @Column(name = "time", length = 30, nullable = false)
    private String time;

    @Column(name = "place", length = 30, nullable = false)
    private String place;

    @Builder.Default
    @Column(name = "complete", nullable = false)
    private boolean complete=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private Groups group;
}
