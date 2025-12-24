package com.examportal.question.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * QuestionSet Entity
 * Represents a collection of questions grouped together for a test
 */
@Entity
@Table(name = "question_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Question set name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @NotNull(message = "Semester is required")
    @Column(nullable = false)
    private Integer semester; // 1-8

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 10)
    private String department; // CS, EC, ME, etc.

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "question_set_questions",
        joinColumns = @JoinColumn(name = "question_set_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
