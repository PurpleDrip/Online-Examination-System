package com.examportal.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Start Test Request DTO
 * Request body for starting a new test session
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartTestRequest {

    @NotBlank(message = "USN is required")
    private String usn;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotNull(message = "Semester is required")
    private Integer semester;

    @NotNull(message = "Question set ID is required")
    private Long questionSetId;
}
