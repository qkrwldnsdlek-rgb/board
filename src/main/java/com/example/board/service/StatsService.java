package com.example.board.service;

import com.example.board.entity.SiteStats;
import com.example.board.repository.PostRepository;
import com.example.board.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final PostRepository postRepository;

    // 오늘 방문 카운트 증가
    public void incrementVisit() {
        LocalDate today = LocalDate.now();
        SiteStats stats = statsRepository.findByVisitDate(today)
                .orElseGet(() -> {
                    SiteStats newStats = new SiteStats();
                    newStats.setVisitDate(today);
                    newStats.setVisitCount(0);
                    return newStats;
                });
        stats.setVisitCount(stats.getVisitCount() + 1);
        statsRepository.save(stats);
    }

    // 오늘 방문자 수 조회
    public int getTodayVisit() {
        return statsRepository.findByVisitDate(LocalDate.now())
                .map(SiteStats::getVisitCount)
                .orElse(0);
    }

    // 전체 게시글 수 조회
    public long getTotalPosts() {
        return postRepository.count();
    }
}