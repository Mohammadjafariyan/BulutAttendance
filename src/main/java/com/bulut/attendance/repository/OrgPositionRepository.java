package com.bulut.attendance.repository;

import com.bulut.attendance.domain.OrgPosition;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrgPosition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgPositionRepository extends JpaRepository<OrgPosition, UUID> {}
