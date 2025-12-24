package com.examportal.test.controller;

import com.examportal.test.dto.QuestionSetDTO;
import com.examportal.test.dto.StartTestRequest;
import com.examportal.test.dto.SubmitAnswerRequest;
import com.examportal.test.entity.TestSession;
import com.examportal.test.service.TestSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TestSession Controller
 * REST API endpoints for test session management
 */
@RestController
@RequestMapping("/api/test-sessions")
@RequiredArgsConstructor
@Slf4j
public class TestSessionController {

    private final TestSessionService testSessionService;

    /**
     * Start a new test session
     */
    @PostMapping("/start")
    public ResponseEntity<?> startTest(@Valid @RequestBody StartTestRequest request) {
        log.info("POST /api/test-sessions/start - Starting test for USN: {}", request.getUsn());
        try {
            TestSession session = testSessionService.startTest(request);
            
            // Also fetch the question set to return to the client
            QuestionSetDTO questionSet = testSessionService.fetchQuestionSet(request.getQuestionSetId());
            
            // Return both session and questions
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "session", session,
                "questionSet", questionSet
            ));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error starting test: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to start test: " + e.getMessage()));
        }
    }

    /**
     * Get test session by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestSession> getTestSessionById(@PathVariable Long id) {
        log.info("GET /api/test-sessions/{} - Fetching test session", id);
        return testSessionService.getTestSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all test sessions for a USN
     */
    @GetMapping("/usn/{usn}")
    public ResponseEntity<List<TestSession>> getTestSessionsByUsn(@PathVariable String usn) {
        log.info("GET /api/test-sessions/usn/{} - Fetching test sessions", usn);
        List<TestSession> sessions = testSessionService.getTestSessionsByUsn(usn);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Get all test sessions (for admin)
     */
    @GetMapping
    public ResponseEntity<List<TestSession>> getAllTestSessions() {
        log.info("GET /api/test-sessions - Fetching all test sessions");
        List<TestSession> sessions = testSessionService.getAllTestSessions();
        return ResponseEntity.ok(sessions);
    }

    /**
     * Submit an answer for a question
     */
    @PutMapping("/{id}/answer")
    public ResponseEntity<?> submitAnswer(
            @PathVariable Long id,
            @Valid @RequestBody SubmitAnswerRequest request) {
        log.info("PUT /api/test-sessions/{}/answer - Submitting answer for question {}", 
                id, request.getQuestionId());
        try {
            TestSession updated = testSessionService.submitAnswer(id, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Submit the entire test
     */
    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitTest(@PathVariable Long id) {
        log.info("POST /api/test-sessions/{}/submit - Submitting test", id);
        try {
            TestSession completed = testSessionService.submitTest(id);
            return ResponseEntity.ok(completed);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper class for Map.of() - Java 9+
    private static class Map {
        public static <K, V> java.util.Map<K, V> of(K k1, V v1) {
            return java.util.Collections.singletonMap(k1, v1);
        }
        
        public static <K, V> java.util.Map<K, V> of(K k1, V v1, K k2, V v2) {
            java.util.Map<K, V> map = new java.util.HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }
    }
}
