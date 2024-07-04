package com.bulut.attendance.repository;

import com.bulut.attendance.domain.TaxTemplate;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaxTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxTemplateRepository extends JpaRepository<TaxTemplate, UUID> {}
