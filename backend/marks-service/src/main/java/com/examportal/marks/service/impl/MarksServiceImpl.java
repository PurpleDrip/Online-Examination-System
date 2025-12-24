package com.examportal.marks.service.impl;

import com.examportal.marks.dto.DashboardStats;
import com.examportal.marks.dto.QuestionDTO;
import com.examportal.marks.dto.QuestionSetDTO;
import com.examportal.marks.dto.TestSessionDTO;
import com.examportal.marks.entity.Result;
import com.examportal.marks.repository.ResultRepository;
import com.examportal.marks.service.MarksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Marks Service Implementation
 * Implements business logic for marks calculation
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MarksServiceImpl implements MarksService {

    private final ResultRepository resultRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${services.test-service.url:http://localhost:8086}")
    private String testServiceUrl;

    @Value("${services.question-service.url:http://localhost:8085}")
    private String questionServiceUrl;

    @Override
    public Result calculateMarks(Long testSessionId) {
        log.info("Calculating marks for test session: {}", testSessionId);

        // Check if result already exists
        Optional<Result> existing = resultRepository.findByTestSessionId(testSessionId);
        if (existing.isPresent()) {
            log.warn("Result already exists for test session: {}", testSessionId);
            return existing.get();
        }

        // Fetch test session from Test Service
        TestSessionDTO testSession = fetchTestSession(testSessionId);

        // Fetch question set from Question Service
        QuestionSetDTO questionSet = fetchQuestionSet(testSession.getQuestionSetId());

        // Parse student answers
        Map<String, String> studentAnswers = parseAnswers(testSession.getAnswers());

        // Calculate marks
        int totalQuestions = questionSet.getQuestions().size();
        int correctAnswers = 0;
        int wrongAnswers = 0;

        for (QuestionDTO question : questionSet.getQuestions()) {
            String studentAnswer = studentAnswers.get(question.getId().toString());
            if (studentAnswer != null && studentAnswer.equalsIgnoreCase(question.getCorrectOption())) {
                correctAnswers++;
            } else if (studentAnswer != null) {
                wrongAnswers++;
            }
        }

        // Calculate percentage
        double percentage = totalQuestions > 0 ? (correctAnswers * 100.0) / totalQuestions : 0.0;

        // Create result
        Result result = new Result();
        result.setTestSessionId(testSessionId);
        result.setUsn(testSession.getUsn());
        result.setStudentName(testSession.getStudentName());
        result.setSemester(testSession.getSemester());
        result.setDepartment(testSession.getDepartment());
        result.setQuestionSetId(testSession.getQuestionSetId());
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correctAnswers);
        result.setWrongAnswers(wrongAnswers);
        result.setScore(correctAnswers);
        result.setPercentage(percentage);

        Result saved = resultRepository.save(result);
        log.info("Marks calculated for test session {}: {}/{} ({}%)", 
                testSessionId, correctAnswers, totalQuestions, String.format("%.2f", percentage));

        return saved;
    }

    @Override
    public Optional<Result> getResultByTestSessionId(Long testSessionId) {
        log.debug("Fetching result for test session: {}", testSessionId);
        return resultRepository.findByTestSessionId(testSessionId);
    }

    @Override
    public List<Result> getResultsByUsn(String usn) {
        log.debug("Fetching results for USN: {}", usn);
        return resultRepository.findByUsn(usn);
    }

    @Override
    public List<Result> getAllResults() {
        log.debug("Fetching all results");
        return resultRepository.findAll();
    }

    @Override
    public DashboardStats getDashboardStats() {
        log.debug("Calculating overall dashboard statistics");
        return calculateStats(resultRepository.findAll());
    }

    @Override
    public DashboardStats getDashboardStatsByUsn(String usn) {
        log.debug("Calculating dashboard statistics for USN: {}", usn);
        return calculateStats(resultRepository.findByUsn(usn));
    }

    @Override
    public DashboardStats getDashboardStatsByDepartment(String department) {
        log.debug("Calculating dashboard statistics for department: {}", department);
        return calculateStats(resultRepository.findByDepartment(department));
    }

    @Override
    public DashboardStats getDashboardStatsBySemester(Integer semester) {
        log.debug("Calculating dashboard statistics for semester: {}", semester);
        return calculateStats(resultRepository.findBySemester(semester));
    }

    /**
     * Fetch test session from Test Service
     */
    private TestSessionDTO fetchTestSession(Long testSessionId) {
        String url = testServiceUrl + "/api/test-sessions/" + testSessionId;
        log.info("Fetching test session from: {}", url);

        try {
            TestSessionDTO testSession = restTemplate.getForObject(url, TestSessionDTO.class);
            if (testSession == null) {
                throw new RuntimeException("Test session not found with ID: " + testSessionId);
            }
            log.info("Fetched test session for USN: {}", testSession.getUsn());
            return testSession;
        } catch (Exception e) {
            log.error("Failed to fetch test session: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch test session from Test Service", e);
        }
    }

    /**
     * Fetch question set from Question Service
     */
    private QuestionSetDTO fetchQuestionSet(Long questionSetId) {
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
     * Calculate statistics from a list of results
     */
    private DashboardStats calculateStats(List<Result> results) {
        if (results.isEmpty()) {
            return new DashboardStats(0L, 0.0, 0.0, 0, 0);
        }

        long totalTests = results.size();
        double avgScore = results.stream()
                .mapToInt(Result::getScore)
                .average()
                .orElse(0.0);
        double avgPercentage = results.stream()
                .mapToDouble(Result::getPercentage)
                .average()
                .orElse(0.0);
        int highestScore = results.stream()
                .mapToInt(Result::getScore)
                .max()
                .orElse(0);
        int lowestScore = results.stream()
                .mapToInt(Result::getScore)
                .min()
                .orElse(0);

        return new DashboardStats(totalTests, avgScore, avgPercentage, highestScore, lowestScore);
    }
}
