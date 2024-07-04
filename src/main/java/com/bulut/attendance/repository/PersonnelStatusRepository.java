package com.bulut.attendance.repository;

import com.bulut.attendance.domain.PersonnelStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PersonnelStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonnelStatusRepository extends JpaRepository<PersonnelStatus, UUID> {}
