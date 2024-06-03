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
@IdClass(MemberId.class)
@Builder
@Table(name = "member_table")
public class Member {

    @Id
    @Column(name = "groupId", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "userID", nullable = false)
    private String userID;

    @ManyToOne
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private Groups group;

    @ManyToOne
    @JoinColumn(name = "userID", insertable = false, updatable = false)
    private User user;

}
