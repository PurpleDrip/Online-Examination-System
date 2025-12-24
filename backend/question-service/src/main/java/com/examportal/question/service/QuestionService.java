package com.examportal.question.service;

import com.examportal.question.entity.Question;

import java.util.List;
import java.util.Optional;

/**
 * Question Service Interface
 * Business logic for question management
 */
public interface QuestionService {
    
    /**
     * Create a new question
     */
    Question createQuestion(Question question);
    
    /**
     * Get question by ID
     */
    Optional<Question> getQuestionById(Long id);
    
    /**
     * Get all questions
     */
    List<Question> getAllQuestions();
    
    /**
     * Get questions by semester
     */
    List<Question> getQuestionsBySemester(Integer semester);
    
    /**
     * Get questions by department
     */
    List<Question> getQuestionsByDepartment(String department);
    
    /**
     * Get questions by semester and department
     */
    List<Question> getQuestionsBySemesterAndDepartment(Integer semester, String department);
    
    /**
     * Update question
     */
    Question updateQuestion(Long id, Question question);
    
    /**
     * Delete question
     */
    void deleteQuestion(Long id);
}
