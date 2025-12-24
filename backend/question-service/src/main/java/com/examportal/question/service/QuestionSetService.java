package com.examportal.question.service;

import com.examportal.question.entity.QuestionSet;

import java.util.List;
import java.util.Optional;

/**
 * QuestionSet Service Interface
 * Business logic for question set management
 */
public interface QuestionSetService {
    
    /**
     * Create a new question set
     */
    QuestionSet createQuestionSet(QuestionSet questionSet);
    
    /**
     * Get question set by ID
     */
    Optional<QuestionSet> getQuestionSetById(Long id);
    
    /**
     * Get all question sets
     */
    List<QuestionSet> getAllQuestionSets();
    
    /**
     * Get question sets by semester
     */
    List<QuestionSet> getQuestionSetsBySemester(Integer semester);
    
    /**
     * Get question sets by department
     */
    List<QuestionSet> getQuestionSetsByDepartment(String department);
    
    /**
     * Get question sets by semester and department
     */
    List<QuestionSet> getQuestionSetsBySemesterAndDepartment(Integer semester, String department);
    
    /**
     * Update question set
     */
    QuestionSet updateQuestionSet(Long id, QuestionSet questionSet);
    
    /**
     * Add question to question set
     */
    QuestionSet addQuestionToSet(Long setId, Long questionId);
    
    /**
     * Remove question from question set
     */
    QuestionSet removeQuestionFromSet(Long setId, Long questionId);
    
    /**
     * Delete question set
     */
    void deleteQuestionSet(Long id);
}
