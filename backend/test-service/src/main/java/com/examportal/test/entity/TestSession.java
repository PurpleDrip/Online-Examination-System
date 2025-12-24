package com.examportal.test.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * TestSession Entity
 * Represents a test session with student information embedded
 */
@Entity
@Table(name = "test_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "USN is required")
    @Column(nullable = false)
    private String usn;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false)
    private String studentName;

    @NotNull(message = "Semester is required")
    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false, length = 10)
    private String department; // Parsed from USN

    @Column(length = 4)
    private String yearOfAdmission; // Parsed from USN

    @NotNull(message = "Question set ID is required")
    @Column(nullable = false)
    private Long questionSetId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String answers; // JSON string: {"1": "A", "2": "B", ...}

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status = TestStatus.IN_PROGRESS;

    private LocalDateTime submittedAt;

    public enum TestStatus {
        IN_PROGRESS,
        COMPLETED
    }
}
