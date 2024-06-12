package com.example.project.service;

import com.example.project.domain.Groups;
import com.example.project.domain.Post;
import com.example.project.domain.User;
import com.example.project.dto.PhotoListDto;
import com.example.project.dto.PostDto;
import com.example.project.dto.PostViewDto;
import com.example.project.repository.GroupsRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository; // UserRepository 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final GroupsRepository groupsRepository;
    private final UserRepository userRepository; // UserRepository 추가

    public PostDto createPost(PostDto postDto, String userID) {
        Groups group = groupsRepository.findById(postDto.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupId"));

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userID"));

        Post post = Post.builder()
                .group(group)
                .groupId(postDto.getGroupId())
                .userID(userID)
                .user(user) // 작성자의 UserName 설정
                .ptext(postDto.getPtext())
                .pdate(postDto.getPdate() != null ? postDto.getPdate() : LocalDate.now())
                .photoPath(postDto.getPhotoPath())
                .build();

        post = postRepository.save(post);

        return PostDto.builder()
                .groupId(postDto.getGroupId())
                .ptext(post.getPtext())
                .pdate(post.getPdate())
                .photoPath(post.getPhotoPath())
                .build();
    }

    public PostViewDto getPostById(Long postId) {
        Post post = postRepository.findPostWithUserById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId"));

        return PostViewDto.builder()
                .userID(post.getUserID())
                .userName(post.getUser().getUserName())  // 닉네임 추가
                .ptext(post.getPtext())
                .photoPath(post.getPhotoPath())
                .pdate(post.getPdate())
                .build();
    }

    public List<PhotoListDto> getPhotosByGroupId(Long groupId) {
        List<Post> posts = postRepository.findByGroup_GroupId(groupId);
        return posts.stream()
                .map(post -> PhotoListDto.builder()
                        .postId(post.getPostId())
                        .pdate(post.getPdate())
                        .photoPath(post.getPhotoPath())
                        .groupId(post.getGroup().getGroupId()) // groupId 설정
                        .userName(post.getUser().getUserName()) // userName 설정
                        .ptext(post.getPtext()) // ptext 설정
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long postId, String userID) {
        // Post 엔티티를 조회하여 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        // 작성자인지 확인
        if (!post.getUserID().equals(userID)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.deleteById(postId);

        boolean hasPosts = postRepository.existsByUserID(userID);
        if (!hasPosts) {
            userRepository.deleteByUserID(userID); // 수정된 메서드 호출
        }
    }
}
