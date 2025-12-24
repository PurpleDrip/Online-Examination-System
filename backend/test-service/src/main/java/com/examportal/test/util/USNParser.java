package com.examportal.test.util;

import lombok.extern.slf4j.Slf4j;

/**
 * USN Parser Utility
 * Parses USN to extract year of admission and department
 * USN Format: 1MS22CS023
 * - Positions 3-4 (index 3-4): Year of Admission (e.g., "22")
 * - Positions 5-6 (index 5-6): Department Code (e.g., "CS")
 */
@Slf4j
public class USNParser {

    /**
     * Parse year of admission from USN
     * Example: 1MS22CS023 -> "22"
     */
    public static String parseYear(String usn) {
        if (usn == null || usn.length() < 5) {
            throw new IllegalArgumentException("Invalid USN format. USN must be at least 5 characters long.");
        }
        String year = usn.substring(3, 5);
        log.debug("Parsed year from USN {}: {}", usn, year);
        return year;
    }

    /**
     * Parse department code from USN
     * Example: 1MS22CS023 -> "CS"
     */
    public static String parseDepartment(String usn) {
        if (usn == null || usn.length() < 7) {
            throw new IllegalArgumentException("Invalid USN format. USN must be at least 7 characters long.");
        }
        String department = usn.substring(5, 7);
        log.debug("Parsed department from USN {}: {}", usn, department);
        return department;
    }

    /**
     * Validate USN format
     */
    public static boolean isValidUSN(String usn) {
        if (usn == null || usn.length() < 10) {
            return false;
        }
        // Basic validation: check if year and department positions contain expected characters
        try {
            String year = usn.substring(3, 5);
            String dept = usn.substring(5, 7);
            // Year should be numeric
            Integer.parseInt(year);
            // Department should be alphabetic
            return dept.matches("[A-Z]{2}");
        } catch (Exception e) {
            return false;
        }
    }
}
