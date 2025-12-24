package com.examportal.question.repository;

import com.examportal.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Question Repository
 * Data access layer for Question entity
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * Find questions by semester
     */
    List<Question> findBySemester(Integer semester);
    
    /**
     * Find questions by department
     */
    List<Question> findByDepartment(String department);
    
    /**
     * Find questions by semester and department
     */
    List<Question> findBySemesterAndDepartment(Integer semester, String department);
}
