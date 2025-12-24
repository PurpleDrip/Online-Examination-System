package com.examportal.marks.service;

import com.examportal.marks.dto.DashboardStats;
import com.examportal.marks.entity.Result;

import java.util.List;
import java.util.Optional;

/**
 * Marks Service Interface
 * Business logic for marks calculation and result management
 */
public interface MarksService {
    
    /**
     * Calculate marks for a test session
     */
    Result calculateMarks(Long testSessionId);
    
    /**
     * Get result by test session ID
     */
    Optional<Result> getResultByTestSessionId(Long testSessionId);
    
    /**
     * Get all results for a USN
     */
    List<Result> getResultsByUsn(String usn);
    
    /**
     * Get all results (for admin)
     */
    List<Result> getAllResults();
    
    /**
     * Get dashboard statistics (overall)
     */
    DashboardStats getDashboardStats();
    
    /**
     * Get dashboard statistics for a specific USN
     */
    DashboardStats getDashboardStatsByUsn(String usn);
    
    /**
     * Get dashboard statistics for a department
     */
    DashboardStats getDashboardStatsByDepartment(String department);
    
    /**
     * Get dashboard statistics for a semester
     */
    DashboardStats getDashboardStatsBySemester(Integer semester);
}
