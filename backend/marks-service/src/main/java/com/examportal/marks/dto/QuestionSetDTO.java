package com.examportal.marks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * QuestionSet DTO
 * Mirrors the QuestionSet entity from Question Service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSetDTO {
    private Long id;
    private String name;
    private String description;
    private Integer semester;
    private String department;
    private Set<QuestionDTO> questions;
}
