package com.bulut.attendance.repository;

import com.bulut.attendance.domain.AccProcStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccProcStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccProcStepRepository extends JpaRepository<AccProcStep, Long> {}
