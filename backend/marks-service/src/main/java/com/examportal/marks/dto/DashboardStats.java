package com.examportal.marks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dashboard Statistics DTO
 * Contains aggregated statistics for dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long totalTests;
    private Double averageScore;
    private Double averagePercentage;
    private Integer highestScore;
    private Integer lowestScore;
}
