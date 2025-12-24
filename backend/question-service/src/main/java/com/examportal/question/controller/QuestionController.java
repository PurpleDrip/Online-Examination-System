package com.examportal.question.controller;

import com.examportal.question.entity.Question;
import com.examportal.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Question Controller
 * REST API endpoints for question management
 */
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    /**
     * Create a new question
     */
    @PostMapping
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question) {
        log.info("POST /api/questions - Creating new question");
        Question created = questionService.createQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get question by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        log.info("GET /api/questions/{} - Fetching question", id);
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all questions with optional filters
     */
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions(
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String department) {
        
        log.info("GET /api/questions - Fetching questions with filters: semester={}, department={}", 
                semester, department);
        
        List<Question> questions;
        
        if (semester != null && department != null) {
            questions = questionService.getQuestionsBySemesterAndDepartment(semester, department);
        } else if (semester != null) {
            questions = questionService.getQuestionsBySemester(semester);
        } else if (department != null) {
            questions = questionService.getQuestionsByDepartment(department);
        } else {
            questions = questionService.getAllQuestions();
        }
        
        return ResponseEntity.ok(questions);
    }

    /**
     * Update question
     */
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody Question question) {
        log.info("PUT /api/questions/{} - Updating question", id);
        try {
            Question updated = questionService.updateQuestion(id, question);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete question
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.info("DELETE /api/questions/{} - Deleting question", id);
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
