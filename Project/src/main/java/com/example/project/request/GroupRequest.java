package com.example.project.request;

public class GroupRequest {
    private String name;
    private int password;

    // 기본 생성자 추가
    public GroupRequest() {}

    public GroupRequest(String name, int password) {
        this.name = name;
        this.password = password;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}