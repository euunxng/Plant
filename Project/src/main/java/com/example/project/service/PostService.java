package com.example.project.service;

import com.example.project.domain.*;
import com.example.project.dto.PhotoList;
import com.example.project.dto.PostDto;
import com.example.project.dto.PostViewDto;
import com.example.project.repository.GroupsRepository;
import com.example.project.repository.PostRepository;
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

    public PostDto createPost(PostDto postDto, String userID) {
        Groups group = groupsRepository.findById(Long.parseLong(postDto.getGroupId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid groupId"));

        Post post = Post.builder()
                .group(group)
                .groupId(Long.parseLong(postDto.getGroupId()))
                .userID(userID)
                .pText(postDto.getPText())
                .pDate(postDto.getPDate() != null ? postDto.getPDate() : LocalDate.now())
                .photoPath(postDto.getPhotoPath())
                .build();

        post = postRepository.save(post);

        return PostDto.builder()
                .groupId(postDto.getGroupId())
                .pText(post.getPText())
                .pDate(post.getPDate())
                .photoPath(post.getPhotoPath())
                .build();
    }

    public PostViewDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId"));

        return PostViewDto.builder()
                .userID(post.getUserID())
                .pText(post.getPText())
                .photoPath(post.getPhotoPath())
                .pDate(post.getPDate())
                .build();
    }

    public List<PhotoList> getPhotosByGroupId(Long groupId) {
        List<Post> posts = postRepository.findByGroup_GroupId(groupId);
        return posts.stream()
                .map(post -> PhotoList.builder()
                        .postID(post.getPostID())
                        .photoPath(post.getPhotoPath())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long postID, String userID) {
        // Post 엔티티를 조회하여 존재 여부 확인
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postID));

        // 작성자인지 확인
        if (!post.getUserID().equals(userID)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.deleteById(postID);
    }
}
