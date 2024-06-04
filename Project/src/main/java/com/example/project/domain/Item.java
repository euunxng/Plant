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
public class Item {

    @Id
    @Column(name = "groupId")
    private Long groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private Groups group;

    @Builder.Default
    @Column(name = "coin", nullable = false)
    private int coin = 0;

    @Builder.Default
    @Column(name = "water", nullable = false)
    private int water = 0;

    @Builder.Default
    @Column(name = "rabbit", nullable = false)
    private boolean rabbit = false;

    @Column(name = "seed")
    private int seed;

    @Builder.Default
    @Column(name = "stone", nullable = false)
    private boolean stone = false;

    @Builder.Default
    @Column(name = "energy1", nullable = false)
    private int energy1 = 0;

    @Builder.Default
    @Column(name = "energy2", nullable = false)
    private int energy2 = 0;

    @Builder.Default
    @Column(name = "energy3", nullable = false)
    private int energy3 = 0;

    @Builder.Default
    @Column(name = "cuteStone", nullable = false)
    private boolean cuteStone = false;

    @Builder.Default
    @Column(name = "ladybug", nullable = false)
    private boolean ladybug = false;

    @Builder.Default
    @Column(name = "title", nullable = false)
    private boolean title = false;

    @Builder.Default
    @Column(name = "bfly", nullable = false)
    private boolean bfly = false;

}
