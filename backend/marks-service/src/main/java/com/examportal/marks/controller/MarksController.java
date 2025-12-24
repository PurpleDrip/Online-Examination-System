package com.examportal.marks.controller;

import com.examportal.marks.dto.CalculateMarksRequest;
import com.examportal.marks.dto.DashboardStats;
import com.examportal.marks.entity.Result;
import com.examportal.marks.service.MarksService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Marks Controller
 * REST API endpoints for marks calculation and result management
 */
@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
@Slf4j
public class MarksController {

    private final MarksService marksService;

    /**
     * Calculate marks for a test session
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMarks(@Valid @RequestBody CalculateMarksRequest request) {
        log.info("POST /api/marks/calculate - Calculating marks for test session: {}", 
                request.getTestSessionId());
        try {
            Result result = marksService.calculateMarks(request.getTestSessionId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("Error calculating marks: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(java.util.Map.of("error", "Failed to calculate marks: " + e.getMessage()));
        }
    }

    /**
     * Get result by test session ID
     */
    @GetMapping("/test-session/{testSessionId}")
    public ResponseEntity<Result> getResultByTestSessionId(@PathVariable Long testSessionId) {
        log.info("GET /api/marks/test-session/{} - Fetching result", testSessionId);
        return marksService.getResultByTestSessionId(testSessionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all results for a USN
     */
    @GetMapping("/usn/{usn}")
    public ResponseEntity<List<Result>> getResultsByUsn(@PathVariable String usn) {
        log.info("GET /api/marks/usn/{} - Fetching results", usn);
        List<Result> results = marksService.getResultsByUsn(usn);
        return ResponseEntity.ok(results);
    }

    /**
     * Get all results (for admin)
     */
    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        log.info("GET /api/marks - Fetching all results");
        List<Result> results = marksService.getAllResults();
        return ResponseEntity.ok(results);
    }

    /**
     * Get overall dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        log.info("GET /api/marks/dashboard - Fetching overall statistics");
        DashboardStats stats = marksService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get dashboard statistics for a specific USN
     */
    @GetMapping("/dashboard/usn/{usn}")
    public ResponseEntity<DashboardStats> getDashboardStatsByUsn(@PathVariable String usn) {
        log.info("GET /api/marks/dashboard/usn/{} - Fetching statistics", usn);
        DashboardStats stats = marksService.getDashboardStatsByUsn(usn);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get dashboard statistics for a department
     */
    @GetMapping("/dashboard/department/{dept}")
    public ResponseEntity<DashboardStats> getDashboardStatsByDepartment(@PathVariable String dept) {
        log.info("GET /api/marks/dashboard/department/{} - Fetching statistics", dept);
        DashboardStats stats = marksService.getDashboardStatsByDepartment(dept);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get dashboard statistics for a semester
     */
    @GetMapping("/dashboard/semester/{semester}")
    public ResponseEntity<DashboardStats> getDashboardStatsBySemester(@PathVariable Integer semester) {
        log.info("GET /api/marks/dashboard/semester/{} - Fetching statistics", semester);
        DashboardStats stats = marksService.getDashboardStatsBySemester(semester);
        return ResponseEntity.ok(stats);
    }
}
