package com.example.project.request;

public class SearchRequest {
    private Long groupId;
    private String groupName;
    private int groupPassword;

    // Getters and Setters
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(int groupPassword) {
        this.groupPassword = groupPassword;
    }
}
