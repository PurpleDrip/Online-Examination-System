package com.examportal.question.config;

import com.examportal.question.entity.Question;
import com.examportal.question.entity.QuestionSet;
import com.examportal.question.repository.QuestionRepository;
import com.examportal.question.repository.QuestionSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Data Initializer
 * Loads hardcoded questions and question sets on application startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final QuestionSetRepository questionSetRepository;

    @Override
    public void run(String... args) {
        // Check if data already exists
        if (questionRepository.count() > 0) {
            log.info("Questions already exist in database. Skipping initialization.");
            return;
        }

        log.info("Initializing database with hardcoded questions...");

        // Create hardcoded questions for CS Department, Semester 6
        Question q1 = new Question();
        q1.setQuestionText("What is the time complexity of binary search?");
        q1.setOptionA("O(n)");
        q1.setOptionB("O(log n)");
        q1.setOptionC("O(n^2)");
        q1.setOptionD("O(1)");
        q1.setCorrectOption("B");
        q1.setSemester(6);
        q1.setDepartment("CS");

        Question q2 = new Question();
        q2.setQuestionText("Which data structure uses LIFO (Last In First Out)?");
        q2.setOptionA("Queue");
        q2.setOptionB("Array");
        q2.setOptionC("Stack");
        q2.setOptionD("Tree");
        q2.setCorrectOption("C");
        q2.setSemester(6);
        q2.setDepartment("CS");

        Question q3 = new Question();
        q3.setQuestionText("What does SQL stand for?");
        q3.setOptionA("Structured Query Language");
        q3.setOptionB("Simple Question Language");
        q3.setOptionC("Standard Query Language");
        q3.setOptionD("Sequential Query Language");
        q3.setCorrectOption("A");
        q3.setSemester(6);
        q3.setDepartment("CS");

        // Save questions
        q1 = questionRepository.save(q1);
        q2 = questionRepository.save(q2);
        q3 = questionRepository.save(q3);

        log.info("Created {} hardcoded questions", 3);

        // Create a question set
        QuestionSet set1 = new QuestionSet();
        set1.setName("Data Structures & Algorithms - Set 1");
        set1.setDescription("Basic questions on DSA and Database concepts");
        set1.setSemester(6);
        set1.setDepartment("CS");
        
        Set<Question> questions = new HashSet<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        set1.setQuestions(questions);

        questionSetRepository.save(set1);

        log.info("Created 1 question set with 3 questions");
        log.info("Database initialization complete!");
    }
}
