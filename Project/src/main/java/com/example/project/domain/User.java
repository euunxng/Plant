package com.example.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @NotBlank
    @Column(name = "userID", length = 8, nullable = false, unique = true)
    private String userID;
    public void setUserID(String userID) {
        if (userID.length() > 8) {
            throw new IllegalArgumentException("8자보다 깁니다.");
        }
        this.userID = userID;
    }

    @NotBlank
    @Column(name = "userName", length = 8, nullable = false)
    private String userName;
    public void setUserName(String userName) {
        if (userName.length() > 8) {
            throw new IllegalArgumentException("8자보다 깁니다.");
        }
        this.userName = userName;
    }

    @NotBlank
    @Column(name = "userPassword", nullable = false)
    private String userPassword;

    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // 프로필사진 nullable = false 해야함 나중에
    @NotBlank
    @Lob
    @Column(name = "profilePhoto")
    private byte[] profilePhoto;

    // 얼굴인식 nullable = false 해야함 나중에
    @NotBlank
    @Lob
    @Column(name = "userFace")
    private byte[] userFace;

    @Builder.Default
    @Column(name = "login", columnDefinition = "TINYINT default 0")
    private boolean login= false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> members;

}
