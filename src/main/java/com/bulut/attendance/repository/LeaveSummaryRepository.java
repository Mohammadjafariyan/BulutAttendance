package com.bulut.attendance.repository;

import com.bulut.attendance.domain.LeaveSummary;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LeaveSummary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveSummaryRepository extends JpaRepository<LeaveSummary, UUID> {}
