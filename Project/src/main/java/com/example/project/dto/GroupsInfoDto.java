package com.example.project.dto;

import java.util.List;

public class GroupsInfoDto {
    private Long groupId;
    private String groupName;
    private int groupPassword;
    private List<MemberDto> members;

    public GroupsInfoDto(Long groupId, String groupName, int groupPassword, List<MemberDto> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupPassword = groupPassword;
        this.members = members;
    }

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

    public List<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDto> members) {
        this.members = members;
    }
}