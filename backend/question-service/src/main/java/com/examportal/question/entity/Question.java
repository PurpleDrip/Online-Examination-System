package com.examportal.question.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Question Entity
 * Represents a single question with multiple choice options
 */
@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Question text is required")
    @Column(nullable = false, length = 1000)
    private String questionText;

    @NotBlank(message = "Option A is required")
    @Column(nullable = false)
    private String optionA;

    @NotBlank(message = "Option B is required")
    @Column(nullable = false)
    private String optionB;

    @NotBlank(message = "Option C is required")
    @Column(nullable = false)
    private String optionC;

    @NotBlank(message = "Option D is required")
    @Column(nullable = false)
    private String optionD;

    @NotBlank(message = "Correct option is required")
    @Column(nullable = false, length = 1)
    private String correctOption; // A, B, C, or D

    @NotNull(message = "Semester is required")
    @Column(nullable = false)
    private Integer semester; // 1-8

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 10)
    private String department; // CS, EC, ME, etc.

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
