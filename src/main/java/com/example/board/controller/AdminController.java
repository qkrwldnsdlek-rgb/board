package com.example.board.controller;

import com.example.board.entity.Post;
import com.example.board.service.PostService;
import com.example.board.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://board-pjw.vercel.app"})
public class AdminController {

    private final PostService postService;
    private final StatsService statsService;

    @Value("${app.admin.email}")
    private String adminEmail;

    private boolean isAdmin(String email) {
        return adminEmail.equals(email);
    }

    // 대시보드 통계
    @GetMapping("/stats")
    public ResponseEntity<?> getAdminStats(@RequestHeader("X-User-Email") String email) {
        if (!isAdmin(email)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPosts", statsService.getTotalPosts());
        stats.put("todayVisit", statsService.getTodayVisit());
        stats.put("weeklyStats", statsService.getWeeklyStats());
        return ResponseEntity.ok(stats);
    }

    // 전체 게시글 목록
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(
            @RequestHeader("X-User-Email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String category) {
        if (!isAdmin(email)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");

        String titleKeyword = null;
        String contentKeyword = null;

        if (keyword != null && !keyword.isEmpty()) {
            if ("제목".equals(searchType)) titleKeyword = keyword;
            else if ("내용".equals(searchType)) contentKeyword = keyword;
            else { titleKeyword = keyword; contentKeyword = keyword; }
        }
        return ResponseEntity.ok(postService.getAllPosts(page, size, titleKeyword != null ? titleKeyword : contentKeyword, category));
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(
            @RequestHeader("X-User-Email") String email,
            @PathVariable Long id) {
        if (!isAdmin(email)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}