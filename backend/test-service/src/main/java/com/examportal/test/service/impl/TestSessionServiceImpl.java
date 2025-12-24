package com.examportal.test.service.impl;

import com.examportal.test.dto.QuestionSetDTO;
import com.examportal.test.dto.StartTestRequest;
import com.examportal.test.dto.SubmitAnswerRequest;
import com.examportal.test.entity.TestSession;
import com.examportal.test.repository.TestSessionRepository;
import com.examportal.test.service.TestSessionService;
import com.examportal.test.util.USNParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TestSession Service Implementation
 * Implements business logic for test session management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TestSessionServiceImpl implements TestSessionService {

    private final TestSessionRepository testSessionRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${services.question-service.url:http://localhost:8085}")
    private String questionServiceUrl;

    @Value("${services.marks-service.url:http://localhost:8087}")
    private String marksServiceUrl;

    @Override
    public TestSession startTest(StartTestRequest request) {
        log.info("Starting test for USN: {}, Question Set: {}", request.getUsn(), request.getQuestionSetId());

        // Validate USN format
        if (!USNParser.isValidUSN(request.getUsn())) {
            throw new IllegalArgumentException("Invalid USN format: " + request.getUsn());
        }

        // Parse USN
        String year = USNParser.parseYear(request.getUsn());
        String department = USNParser.parseDepartment(request.getUsn());

        // Fetch question set from Question Service
        QuestionSetDTO questionSet = fetchQuestionSet(request.getQuestionSetId());

        // Validate eligibility (semester and department must match)
        if (!questionSet.getSemester().equals(request.getSemester())) {
            throw new IllegalArgumentException(
                String.format("Question set is for semester %d, but student is in semester %d",
                    questionSet.getSemester(), request.getSemester())
            );
        }

        if (!questionSet.getDepartment().equalsIgnoreCase(department)) {
            throw new IllegalArgumentException(
                String.format("Question set is for department %s, but student is from department %s",
                    questionSet.getDepartment(), department)
            );
        }

        // Create test session
        TestSession session = new TestSession();
        session.setUsn(request.getUsn());
        session.setStudentName(request.getStudentName());
        session.setSemester(request.getSemester());
        session.setDepartment(department);
        session.setYearOfAdmission(year);
        session.setQuestionSetId(request.getQuestionSetId());
        session.setAnswers("{}"); // Empty JSON object
        session.setStatus(TestSession.TestStatus.IN_PROGRESS);

        TestSession saved = testSessionRepository.save(session);
        log.info("Test session created with ID: {}", saved.getId());

        return saved;
    }

    @Override
    public Optional<TestSession> getTestSessionById(Long id) {
        log.debug("Fetching test session with ID: {}", id);
        return testSessionRepository.findById(id);
    }

    @Override
    public List<TestSession> getTestSessionsByUsn(String usn) {
        log.debug("Fetching test sessions for USN: {}", usn);
        return testSessionRepository.findByUsn(usn);
    }

    @Override
    public List<TestSession> getAllTestSessions() {
        log.debug("Fetching all test sessions");
        return testSessionRepository.findAll();
    }

    @Override
    public TestSession submitAnswer(Long sessionId, SubmitAnswerRequest request) {
        log.info("Submitting answer for session {}, question {}", sessionId, request.getQuestionId());

        TestSession session = testSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Test session not found with ID: " + sessionId));

        if (session.getStatus() == TestSession.TestStatus.COMPLETED) {
            throw new IllegalStateException("Test session is already completed");
        }

        // Parse existing answers
        Map<String, String> answers = parseAnswers(session.getAnswers());

        // Add/update answer
        answers.put(request.getQuestionId().toString(), request.getSelectedOption());

        // Save updated answers
        session.setAnswers(serializeAnswers(answers));
        TestSession updated = testSessionRepository.save(session);

        log.debug("Answer saved for question {}", request.getQuestionId());
        return updated;
    }

    @Override
    public TestSession submitTest(Long sessionId) {
        log.info("Submitting test for session: {}", sessionId);

        TestSession session = testSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Test session not found with ID: " + sessionId));

        if (session.getStatus() == TestSession.TestStatus.COMPLETED) {
            throw new IllegalStateException("Test session is already completed");
        }

        // Mark session as completed
        session.setStatus(TestSession.TestStatus.COMPLETED);
        session.setEndTime(LocalDateTime.now());
        session.setSubmittedAt(LocalDateTime.now());

        TestSession updated = testSessionRepository.save(session);

        // Call Marks Service to calculate results
        try {
            String url = marksServiceUrl + "/api/marks/calculate";
            Map<String, Object> request = new HashMap<>();
            request.put("testSessionId", sessionId);

            log.info("Calling Marks Service at: {}", url);
            restTemplate.postForObject(url, request, Object.class);
            log.info("Marks calculation initiated for session: {}", sessionId);
        } catch (Exception e) {
            log.error("Failed to call Marks Service: {}", e.getMessage());
            // Don't fail the submission if marks calculation fails
        }

        return updated;
    }

    @Override
    public QuestionSetDTO fetchQuestionSet(Long questionSetId) {
        String url = questionServiceUrl + "/api/question-sets/" + questionSetId;
        log.info("Fetching question set from: {}", url);

        try {
            QuestionSetDTO questionSet = restTemplate.getForObject(url, QuestionSetDTO.class);
            if (questionSet == null) {
                throw new RuntimeException("Question set not found with ID: " + questionSetId);
            }
            log.info("Fetched question set: {} with {} questions",
                questionSet.getName(), questionSet.getQuestions().size());
            return questionSet;
        } catch (Exception e) {
            log.error("Failed to fetch question set: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch question set from Question Service", e);
        }
    }

    /**
     * Parse JSON answers string to Map
     */
    private Map<String, String> parseAnswers(String answersJson) {
        try {
            return objectMapper.readValue(answersJson, HashMap.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse answers JSON: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Serialize Map to JSON string
     */
    private String serializeAnswers(Map<String, String> answers) {
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize answers: {}", e.getMessage());
            return "{}";
        }
    }
}
