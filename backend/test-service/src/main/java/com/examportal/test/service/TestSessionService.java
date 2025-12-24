package com.examportal.test.service;

import com.examportal.test.dto.QuestionSetDTO;
import com.examportal.test.dto.StartTestRequest;
import com.examportal.test.dto.SubmitAnswerRequest;
import com.examportal.test.entity.TestSession;

import java.util.List;
import java.util.Optional;

/**
 * TestSession Service Interface
 * Business logic for test session management
 */
public interface TestSessionService {
    
    /**
     * Start a new test session
     * Validates USN, fetches question set, and checks eligibility
     */
    TestSession startTest(StartTestRequest request);
    
    /**
     * Get test session by ID
     */
    Optional<TestSession> getTestSessionById(Long id);
    
    /**
     * Get all test sessions for a USN
     */
    List<TestSession> getTestSessionsByUsn(String usn);
    
    /**
     * Get all test sessions (for admin)
     */
    List<TestSession> getAllTestSessions();
    
    /**
     * Submit an answer for a question
     */
    TestSession submitAnswer(Long sessionId, SubmitAnswerRequest request);
    
    /**
     * Submit the entire test
     * Calls Marks Service to calculate results
     */
    TestSession submitTest(Long sessionId);
    
    /**
     * Fetch question set from Question Service
     */
    QuestionSetDTO fetchQuestionSet(Long questionSetId);
}
