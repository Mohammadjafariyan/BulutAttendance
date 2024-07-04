package com.bulut.attendance.repository;

import com.bulut.attendance.domain.HrLetter;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HrLetter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HrLetterRepository extends JpaRepository<HrLetter, UUID> {}
