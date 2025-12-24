package com.examportal.marks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TestSession DTO
 * Mirrors the TestSession entity from Test Service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSessionDTO {
    private Long id;
    private String usn;
    private String studentName;
    private Integer semester;
    private String department;
    private String yearOfAdmission;
    private Long questionSetId;
    private String answers; // JSON string
    private String status;
}
