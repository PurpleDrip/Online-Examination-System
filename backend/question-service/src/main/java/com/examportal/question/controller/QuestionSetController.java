package com.examportal.question.controller;

import com.examportal.question.entity.QuestionSet;
import com.examportal.question.service.QuestionSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * QuestionSet Controller
 * REST API endpoints for question set management
 */
@RestController
@RequestMapping("/api/question-sets")
@RequiredArgsConstructor
@Slf4j
public class QuestionSetController {

    private final QuestionSetService questionSetService;

    /**
     * Create a new question set
     */
    @PostMapping
    public ResponseEntity<QuestionSet> createQuestionSet(@Valid @RequestBody QuestionSet questionSet) {
        log.info("POST /api/question-sets - Creating new question set");
        QuestionSet created = questionSetService.createQuestionSet(questionSet);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get question set by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionSet> getQuestionSetById(@PathVariable Long id) {
        log.info("GET /api/question-sets/{} - Fetching question set", id);
        return questionSetService.getQuestionSetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all question sets with optional filters
     */
    @GetMapping
    public ResponseEntity<List<QuestionSet>> getAllQuestionSets(
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String department) {
        
        log.info("GET /api/question-sets - Fetching question sets with filters: semester={}, department={}", 
                semester, department);
        
        List<QuestionSet> questionSets;
        
        if (semester != null && department != null) {
            questionSets = questionSetService.getQuestionSetsBySemesterAndDepartment(semester, department);
        } else if (semester != null) {
            questionSets = questionSetService.getQuestionSetsBySemester(semester);
        } else if (department != null) {
            questionSets = questionSetService.getQuestionSetsByDepartment(department);
        } else {
            questionSets = questionSetService.getAllQuestionSets();
        }
        
        return ResponseEntity.ok(questionSets);
    }

    /**
     * Update question set
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionSet> updateQuestionSet(
            @PathVariable Long id,
            @Valid @RequestBody QuestionSet questionSet) {
        log.info("PUT /api/question-sets/{} - Updating question set", id);
        try {
            QuestionSet updated = questionSetService.updateQuestionSet(id, questionSet);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add question to question set
     */
    @PostMapping("/{setId}/questions/{questionId}")
    public ResponseEntity<QuestionSet> addQuestionToSet(
            @PathVariable Long setId,
            @PathVariable Long questionId) {
        log.info("POST /api/question-sets/{}/questions/{} - Adding question to set", setId, questionId);
        try {
            QuestionSet updated = questionSetService.addQuestionToSet(setId, questionId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove question from question set
     */
    @DeleteMapping("/{setId}/questions/{questionId}")
    public ResponseEntity<QuestionSet> removeQuestionFromSet(
            @PathVariable Long setId,
            @PathVariable Long questionId) {
        log.info("DELETE /api/question-sets/{}/questions/{} - Removing question from set", setId, questionId);
        try {
            QuestionSet updated = questionSetService.removeQuestionFromSet(setId, questionId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete question set
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionSet(@PathVariable Long id) {
        log.info("DELETE /api/question-sets/{} - Deleting question set", id);
        questionSetService.deleteQuestionSet(id);
        return ResponseEntity.noContent().build();
    }
}
