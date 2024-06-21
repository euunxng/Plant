package com.example.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MemberId.class)
@Builder
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "groupId", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "userID", nullable = false)
    private String userID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private Groups group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", insertable = false, updatable = false)
    private User user;

    public String getUserFacePath() {
        return user.getUserFacePath(); // User 객체에서 userFacePath 값을 가져옴
    }
}
