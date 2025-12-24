package com.examportal.question.repository;

import com.examportal.question.entity.QuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QuestionSet Repository
 * Data access layer for QuestionSet entity
 */
@Repository
public interface QuestionSetRepository extends JpaRepository<QuestionSet, Long> {
    
    /**
     * Find question sets by semester
     */
    List<QuestionSet> findBySemester(Integer semester);
    
    /**
     * Find question sets by department
     */
    List<QuestionSet> findByDepartment(String department);
    
    /**
     * Find question sets by semester and department
     */
    List<QuestionSet> findBySemesterAndDepartment(Integer semester, String department);
}
