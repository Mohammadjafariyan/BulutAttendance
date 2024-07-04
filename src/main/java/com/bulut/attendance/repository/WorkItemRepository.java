package com.bulut.attendance.repository;

import com.bulut.attendance.domain.WorkItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkItemRepository extends JpaRepository<WorkItem, UUID> {}
