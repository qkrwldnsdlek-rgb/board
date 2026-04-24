package com.example.board.controller;

import com.example.board.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://board-pjw.vercel.app"})
public class StatsController {

    private final StatsService statsService;

    // 방문 카운트 증가 + 통계 조회
    @GetMapping
    public Map<String, Object> getStats() {
        statsService.incrementVisit();
        Map<String, Object> stats = new HashMap<>();
        stats.put("todayVisit", statsService.getTodayVisit());
        stats.put("totalPosts", statsService.getTotalPosts());
        return stats;
    }

    @GetMapping("/weekly")
    public List<Map<String, Object>> getWeeklyStats() {
        List<Map<String, Object>> result = statsService.getWeeklyStats();
        return result;
    }

}