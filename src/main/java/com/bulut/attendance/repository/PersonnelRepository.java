package com.bulut.attendance.repository;

import com.bulut.attendance.domain.Personnel;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Personnel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, UUID> {}
