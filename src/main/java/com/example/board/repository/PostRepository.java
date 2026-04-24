package com.example.board.repository;

import com.example.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Post> findByCategoryAndTitleContainingIgnoreCase(String category, String title, Pageable pageable);
    Page<Post> findByCategory(String category, Pageable pageable);
}