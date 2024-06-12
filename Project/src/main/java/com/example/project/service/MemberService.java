package com.example.project.service;

import com.example.project.domain.Groups;
import com.example.project.domain.Member;
import com.example.project.domain.User;
import com.example.project.dto.GroupsInfoDto;
import com.example.project.dto.MemberDto;
import com.example.project.repository.GroupsRepository;
import com.example.project.repository.ItemRepository;
import com.example.project.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final GroupsRepository groupsRepository;
    private final ItemRepository itemRepository;

    private static final int MAX_GROUP_MEMBERS = 6; // 최대 그룹 인원
    private static final int MAX_USER_GROUPS = 5; // 사용자가 참여할 수 있는 최대 그룹 수

    @Transactional
    public void addMemberToGroup(Long groupId, HttpSession session) {

        // 세션에서 로그인된 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }
        String userId = user.getUserID();

        // groupId를 사용하여 그룹 엔티티 가져오기
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        // 사용자가 이미 해당 그룹에 가입되어 있는지 확인
        boolean isMemberExist = memberRepository.existsByGroupIdAndUserID(groupId, userId);
        if (isMemberExist) {
            throw new RuntimeException("이미 가입된 그룹입니다.");
        }
        // 그룹의 현재 멤버 수 확인
        long groupMemberCount = memberRepository.countByGroupId(groupId);
        if (groupMemberCount >= MAX_GROUP_MEMBERS) {
            throw new RuntimeException("최대 수용 인원을 초과했습니다.");
        }

        // 사용자가 속한 그룹 수 확인
        long userGroupCount = memberRepository.countByUserID(userId);
        if (userGroupCount >= MAX_USER_GROUPS) {
            throw new RuntimeException("더이상 그룹을 생성할 수 없습니다.");
        }

        // Member 엔티티 생성 및 저장
        Member member = Member.builder()
                .groupId(groupId)
                .userID(userId)
                .group(group)
                .user(user)
                .build();
        memberRepository.save(member);
    }

    public GroupsInfoDto getGroupInfo(Long groupId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        List<Member> members = memberRepository.findByGroupId(groupId);
        List<MemberDto> memberDtos = members.stream()
                .map(member -> new MemberDto(member.getGroupId(), member.getUserID()))
                .collect(Collectors.toList());

        return new GroupsInfoDto(group.getGroupId(), group.getGroupName(), group.getGroupPassword(), memberDtos);
    }

    @Transactional
    public void deleteMemberFromGroup(Long groupId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }
        String userId = user.getUserID();

        boolean isMemberExist = memberRepository.existsByGroupIdAndUserID(groupId, userId);
        if (!isMemberExist) {
            throw new RuntimeException("사용자가 해당 그룹의 멤버가 아닙니다.");
        }

        memberRepository.deleteByGroupIdAndUserID(groupId, userId);

        // 그룹에 남은 멤버가 있는지 확인
        boolean hasMembers = memberRepository.existsByGroupId(groupId);
        if (!hasMembers) {
            // 그룹에 더 이상 멤버가 없으면 그룹 삭제
            itemRepository.deleteByGroupId(groupId);
            groupsRepository.deleteByGroupId(groupId);
        }
    }
}
