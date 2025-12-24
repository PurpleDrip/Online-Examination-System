package com.examportal.question.service.impl;

import com.examportal.question.entity.Question;
import com.examportal.question.repository.QuestionRepository;
import com.examportal.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Question Service Implementation
 * Implements business logic for question management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public Question createQuestion(Question question) {
        log.info("Creating new question for semester {} and department {}", 
                question.getSemester(), question.getDepartment());
        return questionRepository.save(question);
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        log.debug("Fetching question with ID: {}", id);
        return questionRepository.findById(id);
    }

    @Override
    public List<Question> getAllQuestions() {
        log.debug("Fetching all questions");
        return questionRepository.findAll();
    }

    @Override
    public List<Question> getQuestionsBySemester(Integer semester) {
        log.debug("Fetching questions for semester: {}", semester);
        return questionRepository.findBySemester(semester);
    }

    @Override
    public List<Question> getQuestionsByDepartment(String department) {
        log.debug("Fetching questions for department: {}", department);
        return questionRepository.findByDepartment(department);
    }

    @Override
    public List<Question> getQuestionsBySemesterAndDepartment(Integer semester, String department) {
        log.debug("Fetching questions for semester {} and department {}", semester, department);
        return questionRepository.findBySemesterAndDepartment(semester, department);
    }

    @Override
    public Question updateQuestion(Long id, Question question) {
        log.info("Updating question with ID: {}", id);
        return questionRepository.findById(id)
                .map(existing -> {
                    existing.setQuestionText(question.getQuestionText());
                    existing.setOptionA(question.getOptionA());
                    existing.setOptionB(question.getOptionB());
                    existing.setOptionC(question.getOptionC());
                    existing.setOptionD(question.getOptionD());
                    existing.setCorrectOption(question.getCorrectOption());
                    existing.setSemester(question.getSemester());
                    existing.setDepartment(question.getDepartment());
                    return questionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + id));
    }

    @Override
    public void deleteQuestion(Long id) {
        log.info("Deleting question with ID: {}", id);
        questionRepository.deleteById(id);
    }
}
