package com.bulut.attendance.repository;

import com.bulut.attendance.domain.RecordStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecordStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordStatusRepository extends JpaRepository<RecordStatus, UUID> {}
