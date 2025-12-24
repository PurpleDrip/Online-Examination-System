package com.examportal.marks.repository;

import com.examportal.marks.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Result Repository
 * Data access layer for Result entity
 */
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    
    /**
     * Find result by test session ID
     */
    Optional<Result> findByTestSessionId(Long testSessionId);
    
    /**
     * Find results by USN
     */
    List<Result> findByUsn(String usn);
    
    /**
     * Find results by semester
     */
    List<Result> findBySemester(Integer semester);
    
    /**
     * Find results by department
     */
    List<Result> findByDepartment(String department);
    
    /**
     * Find results by semester and department
     */
    List<Result> findBySemesterAndDepartment(Integer semester, String department);
    
    /**
     * Get average score
     */
    @Query("SELECT AVG(r.score) FROM Result r")
    Double getAverageScore();
    
    /**
     * Get average percentage
     */
    @Query("SELECT AVG(r.percentage) FROM Result r")
    Double getAveragePercentage();
    
    /**
     * Get highest score
     */
    @Query("SELECT MAX(r.score) FROM Result r")
    Integer getHighestScore();
    
    /**
     * Get lowest score
     */
    @Query("SELECT MIN(r.score) FROM Result r")
    Integer getLowestScore();
}
