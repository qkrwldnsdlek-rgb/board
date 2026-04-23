package com.example.board.repository;

import com.example.board.entity.SiteStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StatsRepository extends JpaRepository<SiteStats, Long> {
    Optional<SiteStats> findByVisitDate(LocalDate visitDate);
}