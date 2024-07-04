package com.bulut.attendance.repository;

import com.bulut.attendance.domain.HrLetterType;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HrLetterType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HrLetterTypeRepository extends JpaRepository<HrLetterType, UUID> {}
