package com.bulut.attendance.repository;

import com.bulut.attendance.domain.HrLetterParameter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HrLetterParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HrLetterParameterRepository extends JpaRepository<HrLetterParameter, Long> {}
