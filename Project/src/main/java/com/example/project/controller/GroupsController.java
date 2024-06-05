package com.example.project.controller;

import com.example.project.dto.GroupsDto;
import com.example.project.dto.GroupsInfoDto;
import com.example.project.dto.SearchDto;
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
    public ResponseEntity<String> createGroup(@RequestParam String name, @RequestParam int password, HttpSession session) {
        try {
            groupsService.createGroup(name, password, session);
            return ResponseEntity.ok("그룹이 성공적으로 생성되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/userGroups")
    public List<GroupsInfoDto> getUserGroups(HttpSession session) {
        // 사용자가 가입한 그룹 정보 가져오기
        return groupsService.getUserGroups(session);
    }

    @GetMapping("/api/groups/search")
    public ResponseEntity<List<SearchDto>> searchGroups(@RequestParam("keyword") String keyword) {
        List<SearchDto> groups = groupsService.searchGroups(keyword);
        return ResponseEntity.ok(groups);
    }
}
