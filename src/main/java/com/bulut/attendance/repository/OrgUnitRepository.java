package com.bulut.attendance.repository;

import com.bulut.attendance.domain.OrgUnit;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrgUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnit, UUID> {}
