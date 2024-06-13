package com.example.project.service;

import com.example.project.domain.Groups;
import com.example.project.domain.Member;
import com.example.project.domain.Post;
import com.example.project.domain.User;
import com.example.project.dto.PhotoListDto;
import com.example.project.dto.PostDto;
import com.example.project.dto.PostViewDto;
import com.example.project.repository.GroupsRepository;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import com.example.project.repository.CommentRepository;
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
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public PostDto createPost(PostDto postDto, String userID) {
        Groups group = groupsRepository.findById(postDto.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupId"));

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userID"));

        Post post = Post.builder()
                .group(group)
                .groupId(postDto.getGroupId())
                .userID(userID)
                .user(user)
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
                .userName(post.getUser().getUserName())
                .profilePhotoPath(post.getUser().getProfilePhotoPath())
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
                        .groupId(post.getGroup().getGroupId())
                        .userName(post.getUser().getUserName())
                        .ptext(post.getPtext())
                        .profilePhotoPath(post.getUser().getProfilePhotoPath())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long postId, String userID) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        if (!post.getUserID().equals(userID)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        // 댓글 삭제
        commentRepository.deleteByPost(post);

        // 게시물 삭제
        postRepository.deleteById(postId);
    }
}
