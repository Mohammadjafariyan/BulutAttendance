package com.bulut.attendance.repository;

import com.bulut.attendance.domain.AccProcStepExecution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccProcStepExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccProcStepExecutionRepository extends JpaRepository<AccProcStepExecution, Long> {}
