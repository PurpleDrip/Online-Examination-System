package com.examportal.marks.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Calculate Marks Request DTO
 * Request body for calculating marks
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculateMarksRequest {

    @NotNull(message = "Test session ID is required")
    private Long testSessionId;
}
