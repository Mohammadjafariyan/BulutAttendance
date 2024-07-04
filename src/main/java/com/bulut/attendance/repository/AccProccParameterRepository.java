package com.bulut.attendance.repository;

import com.bulut.attendance.domain.AccProccParameter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccProccParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccProccParameterRepository extends JpaRepository<AccProccParameter, Long> {}
