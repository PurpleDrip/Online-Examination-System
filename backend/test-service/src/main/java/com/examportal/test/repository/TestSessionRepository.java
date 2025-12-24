package com.examportal.test.repository;

import com.examportal.test.entity.TestSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TestSession Repository
 * Data access layer for TestSession entity
 */
@Repository
public interface TestSessionRepository extends JpaRepository<TestSession, Long> {
    
    /**
     * Find test sessions by USN
     */
    List<TestSession> findByUsn(String usn);
    
    /**
     * Find test sessions by semester
     */
    List<TestSession> findBySemester(Integer semester);
    
    /**
     * Find test sessions by department
     */
    List<TestSession> findByDepartment(String department);
    
    /**
     * Find test sessions by status
     */
    List<TestSession> findByStatus(TestSession.TestStatus status);
}
