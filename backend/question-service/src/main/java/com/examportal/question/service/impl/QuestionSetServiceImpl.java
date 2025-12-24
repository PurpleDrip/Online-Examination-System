package com.examportal.question.service.impl;

import com.examportal.question.entity.Question;
import com.examportal.question.entity.QuestionSet;
import com.examportal.question.repository.QuestionRepository;
import com.examportal.question.repository.QuestionSetRepository;
import com.examportal.question.service.QuestionSetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * QuestionSet Service Implementation
 * Implements business logic for question set management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionSetServiceImpl implements QuestionSetService {

    private final QuestionSetRepository questionSetRepository;
    private final QuestionRepository questionRepository;

    @Override
    public QuestionSet createQuestionSet(QuestionSet questionSet) {
        log.info("Creating new question set: {} for semester {} and department {}", 
                questionSet.getName(), questionSet.getSemester(), questionSet.getDepartment());
        return questionSetRepository.save(questionSet);
    }

    @Override
    public Optional<QuestionSet> getQuestionSetById(Long id) {
        log.debug("Fetching question set with ID: {}", id);
        return questionSetRepository.findById(id);
    }

    @Override
    public List<QuestionSet> getAllQuestionSets() {
        log.debug("Fetching all question sets");
        return questionSetRepository.findAll();
    }

    @Override
    public List<QuestionSet> getQuestionSetsBySemester(Integer semester) {
        log.debug("Fetching question sets for semester: {}", semester);
        return questionSetRepository.findBySemester(semester);
    }

    @Override
    public List<QuestionSet> getQuestionSetsByDepartment(String department) {
        log.debug("Fetching question sets for department: {}", department);
        return questionSetRepository.findByDepartment(department);
    }

    @Override
    public List<QuestionSet> getQuestionSetsBySemesterAndDepartment(Integer semester, String department) {
        log.debug("Fetching question sets for semester {} and department {}", semester, department);
        return questionSetRepository.findBySemesterAndDepartment(semester, department);
    }

    @Override
    public QuestionSet updateQuestionSet(Long id, QuestionSet questionSet) {
        log.info("Updating question set with ID: {}", id);
        return questionSetRepository.findById(id)
                .map(existing -> {
                    existing.setName(questionSet.getName());
                    existing.setDescription(questionSet.getDescription());
                    existing.setSemester(questionSet.getSemester());
                    existing.setDepartment(questionSet.getDepartment());
                    return questionSetRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Question set not found with ID: " + id));
    }

    @Override
    public QuestionSet addQuestionToSet(Long setId, Long questionId) {
        log.info("Adding question {} to question set {}", questionId, setId);
        QuestionSet questionSet = questionSetRepository.findById(setId)
                .orElseThrow(() -> new RuntimeException("Question set not found with ID: " + setId));
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));
        
        questionSet.getQuestions().add(question);
        return questionSetRepository.save(questionSet);
    }

    @Override
    public QuestionSet removeQuestionFromSet(Long setId, Long questionId) {
        log.info("Removing question {} from question set {}", questionId, setId);
        QuestionSet questionSet = questionSetRepository.findById(setId)
                .orElseThrow(() -> new RuntimeException("Question set not found with ID: " + setId));
        
        questionSet.getQuestions().removeIf(q -> q.getId().equals(questionId));
        return questionSetRepository.save(questionSet);
    }

    @Override
    public void deleteQuestionSet(Long id) {
        log.info("Deleting question set with ID: {}", id);
        questionSetRepository.deleteById(id);
    }
}
