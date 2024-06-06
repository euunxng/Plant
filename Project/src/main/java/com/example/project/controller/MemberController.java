package com.example.project.controller;

import com.example.project.domain.Member;
import com.example.project.dto.MemberDto;
import com.example.project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/addMember")
    public ResponseEntity<String> addMemberToGroup(@RequestParam Long groupId, HttpSession session) {
        try {
            memberService.addMemberToGroup(groupId, session);
            return ResponseEntity.ok("그룹에 성공적으로 가입되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/MemberList/{groupId}")
    public List<MemberDto> getMembersByGroupId(@PathVariable("groupId") Long groupId) {
        return memberService.getMembersByGroupId(groupId);
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<String> deleteMemberFromGroup(@RequestParam Long groupId, HttpSession session) {
        try {
            memberService.deleteMemberFromGroup(groupId, session);
            return ResponseEntity.ok("그룹에서 성공적으로 탈퇴되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
