package com.example.project.controller;

import com.example.project.dto.GroupPageDto;
import com.example.project.dto.GroupsInfoDto;
import com.example.project.dto.JoinView;
import com.example.project.dto.SearchDto;
import com.example.project.request.GroupRequest;
import com.example.project.request.SearchRequest;
import com.example.project.service.GroupsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupsController {

    private final GroupsService groupsService;

    @PostMapping("/CreateGroups")
    public ResponseEntity<String> createGroup(@RequestBody GroupRequest groupRequest, HttpSession session) {
        try {
            Long groupId = groupsService.createGroup(groupRequest.getName(), groupRequest.getPassword(), session);
            return ResponseEntity.ok("그룹이 성공적으로 생성되었습니다. ID: " + groupId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/userGroups")
    public List<GroupsInfoDto> getUserGroups(HttpSession session) {
        return groupsService.getUserGroups(session);
    }

    @PostMapping("/api/groups/search")
    public ResponseEntity<List<SearchDto>> searchGroups(@RequestBody SearchRequest searchRequest) {
        List<SearchDto> groups = groupsService.searchGroups(searchRequest);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/api/group/join")
    public ResponseEntity<String> joinGroup(@RequestBody JoinView joinView, HttpSession session) {
        try {
            groupsService.joinGroup(joinView.getGroupId(), joinView.getName(), joinView.getPassword(), session);
            return ResponseEntity.ok("그룹에 성공적으로 가입되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/GroupPage/{groupId}")
    public GroupPageDto getGroupById(@PathVariable("groupId") Long groupId) {
        return groupsService.getGroupById(groupId);
    }

    @GetMapping("/api/group/info")
    public ResponseEntity<GroupsInfoDto> getGroupInfo(@RequestParam("groupId") Long groupId) {
        try {
            GroupsInfoDto groupInfo = groupsService.getGroupsInfo(groupId);
            return ResponseEntity.ok(groupInfo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
