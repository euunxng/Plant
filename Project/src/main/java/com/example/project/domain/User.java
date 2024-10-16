package com.example.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
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

    public String getUserName() {
        return userName;
    }

    @NotBlank
    @Column(name = "userPassword", nullable = false)
    private String userPassword;

    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profilePhotoPath", nullable = false)
    private String profilePhotoPath;

    @Column(name = "userFacePath", nullable = false)
    private String userFacePath;

    @Builder.Default
    @Column(name = "login", columnDefinition = "TINYINT(1) default 0")
    private boolean login = false;

    @Builder.Default
    @Column(name = "kakao", columnDefinition = "TINYINT(1) default 0")
    private boolean kakao = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> members;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "token",nullable = true)
    private String token;

}
