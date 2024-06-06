package com.example.project.service;

import com.example.project.domain.Groups;
import com.example.project.domain.Member;
import com.example.project.domain.User;
import com.example.project.dto.GroupPageDto;
import com.example.project.dto.GroupsDto;
import com.example.project.dto.GroupsInfoDto;
import com.example.project.dto.SearchDto;
import com.example.project.repository.GroupsRepository;
import com.example.project.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupsService {

    private final GroupsRepository groupsRepository;
    private final MemberRepository memberRepository;


    private static final int MAX_USER_GROUPS = 6; // 사용자가 참여할 수 있는 최대 그룹 수

    @Transactional
    public void createGroup(String name, int password, HttpSession session) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("그룹명은 특수문자 미포함 최대 8자 입니다.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 숫자 4자리 입니다.");
        }

        // 세션에서 로그인한 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        // 사용자가 속한 그룹 수 확인
        long userGroupCount = memberRepository.countByUserID(user.getUserID());
        if (userGroupCount >= MAX_USER_GROUPS) {
            throw new RuntimeException("더이상 그룹을 생성할 수 없습니다.");
        }

        Groups groups = Groups.builder()
                .groupName(name)
                .groupPassword(password)
                .build();

        groupsRepository.save(groups);

        Long groupId = groups.getGroupId();

        // Member 엔티티 생성 및 저장
        Member member = Member.builder()
                .groupId(groupId)
                .userID(user.getUserID())
                .group(groups)
                .user(user)
                .build();

        memberRepository.save(member);
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z가-힣0-9]{1,8}");
    }

    private boolean isValidPassword(int password) {
        return String.valueOf(password).matches("\\d{4}");
    }

    @Transactional
    public List<GroupsInfoDto> getUserGroups(HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        // 사용자의 userID를 통해 User 테이블에서 그룹 ID들을 가져오기
        List<Long> groupIds = memberRepository.findByUserID(user.getUserID())
                .stream()
                .map(Member::getGroupId)
                .collect(Collectors.toList());

        // 그룹 ID들을 통해 Groups 테이블에서 그룹 정보 가져오기
        List<Groups> groupsList = groupsRepository.findAllById(groupIds);

        // 필요한 정보만 DTO로 변환하여 반환
        return groupsList.stream()
                .map(group -> new GroupsInfoDto(group.getGroupName(), group.getPlantType()))
                .collect(Collectors.toList());
    }

    public List<SearchDto> searchGroups(String keyword) {
        List<Groups> groups = groupsRepository.findByGroupNameContaining(keyword);
        return groups.stream()
                .map(group -> new SearchDto(
                        group.getGroupId(),
                        group.getGroupName(),
                        group.getGroupPassword()
                ))
                .collect(Collectors.toList());
    }


    public GroupPageDto getGroupById(Long groupId) {
        Groups group = groupsRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        return GroupPageDto.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .groupPassword(group.getGroupPassword())
                .build();
    }
}
