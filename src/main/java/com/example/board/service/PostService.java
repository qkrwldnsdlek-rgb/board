package com.example.board.service;

import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 전체 조회 / 검색 (페이징)
    public Page<Post> getAllPosts(int page, int size, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (keyword != null && !keyword.isEmpty()) {
            return postRepository.findByTitleContainingIgnoreCase(keyword, pageRequest);
        }
        return postRepository.findAll(pageRequest);
    }

    // 단건 조회 (조회수 증가)
    public Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        post.setViewCount(post.getViewCount() + 1);
        return postRepository.save(post);
    }

    // 생성
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // 수정
    public Post updatePost(Long id, Post updatedPost) {
        Post post = getPost(id);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setAuthor(updatedPost.getAuthor());
        post.setImageUrl(updatedPost.getImageUrl());
        return postRepository.save(post);
    }

    // 삭제
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}