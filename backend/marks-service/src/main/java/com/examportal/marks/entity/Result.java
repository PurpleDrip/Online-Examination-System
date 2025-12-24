package com.examportal.marks.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Result Entity
 * Represents the result of a completed test session
 */
@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Test session ID is required")
    @Column(nullable = false, unique = true)
    private Long testSessionId;

    @NotBlank(message = "USN is required")
    @Column(nullable = false)
    private String usn;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false)
    private String studentName;

    @NotNull(message = "Semester is required")
    @Column(nullable = false)
    private Integer semester;

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 10)
    private String department;

    @NotNull(message = "Question set ID is required")
    @Column(nullable = false)
    private Long questionSetId;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer correctAnswers;

    @Column(nullable = false)
    private Integer wrongAnswers;

    @Column(nullable = false)
    private Integer score; // Same as correctAnswers, but kept for clarity

    @Column(nullable = false)
    private Double percentage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
