package com.example.project.service;

import com.example.project.domain.Groups;
import com.example.project.domain.Member;
import com.example.project.domain.Post;
import com.example.project.domain.User;
import com.example.project.dto.GroupsInfoDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.UserDto;
import com.example.project.repository.*;
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
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CalenderRepository calenderRepository;
    private final WishlistRepository wishlistRepository;

    private static final int MAX_GROUP_MEMBERS = 6; // 최대 그룹 인원
    private static final int MAX_USER_GROUPS = 5; // 사용자가 참여할 수 있는 최대 그룹 수

    @Transactional
    public void addMemberToGroup(Long groupId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }
        String userId = user.getUserID();

        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        boolean isMemberExist = memberRepository.existsByGroupIdAndUserID(groupId, userId);
        if (isMemberExist) {
            throw new RuntimeException("이미 가입된 그룹입니다.");
        }

        long groupMemberCount = memberRepository.countByGroupId(groupId);
        if (groupMemberCount >= MAX_GROUP_MEMBERS) {
            throw new RuntimeException("최대 수용 인원을 초과했습니다.");
        }

        long userGroupCount = memberRepository.countByUserID(userId);
        if (userGroupCount >= MAX_USER_GROUPS) {
            throw new RuntimeException("더이상 그룹을 생성할 수 없습니다.");
        }

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

        try {
            // 사용자가 탈퇴 시 해당 사용자의 게시물 및 댓글 삭제
            List<Post> posts = postRepository.findByGroup_GroupIdAndUserID(groupId, userId);
            for (Post post : posts) {
                commentRepository.deleteByPost(post);
                postRepository.delete(post);
            }

            // 멤버 삭제
            memberRepository.deleteByGroupIdAndUserID(groupId, userId);

            // 그룹에 남은 멤버가 있는지 확인
            boolean hasMembers = memberRepository.existsByGroupId(groupId);
            if (!hasMembers) {

                // 그룹에 더 이상 멤버가 없으면 그룹과 관련된 모든 엔티티 삭제
                wishlistRepository.deleteByGroupId(groupId);
                calenderRepository.deleteByGroupId(groupId);
                commentRepository.deleteByGroupId(groupId);
                postRepository.deleteByGroupId(groupId);
                itemRepository.deleteByGroupId(groupId);
                groupsRepository.deleteById(groupId);
            }
        } catch (Exception e) {
            // 예외 발생 시 롤백하고 로그 출력
            System.err.println("그룹에서 멤버를 삭제하는 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("그룹 탈퇴 중 오류가 발생했습니다.");
        }
    }

    // 그룹에 속한 멤버들의 UserDto를 가져오는 메서드
    public List<UserDto> getUserFacePathsByGroupId(Long groupId) {
        List<Member> members = memberRepository.findByGroupId(groupId);
        return members.stream()
                .map(member -> UserDto.builder()
                        .userID(member.getUser().getUserID())
                        .userName(member.getUser().getUserName())
                        .userPassword(member.getUser().getUserPassword())
                        .email(member.getUser().getEmail())
                        .profilePhotoUrl(member.getUser().getProfilePhotoPath())
                        .userFaceUrl(member.getUserFacePath()) // Member 객체에서 userFacePath 값을 가져옴
                        .login(member.getUser().isLogin())
                        .build())
                .collect(Collectors.toList());
    }
}
