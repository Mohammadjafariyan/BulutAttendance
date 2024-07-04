package com.bulut.attendance.repository;

import com.bulut.attendance.domain.AccountTemplate;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountTemplateRepository extends JpaRepository<AccountTemplate, UUID> {}
