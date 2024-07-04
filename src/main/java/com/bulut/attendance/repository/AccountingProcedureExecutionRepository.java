package com.bulut.attendance.repository;

import com.bulut.attendance.domain.AccountingProcedureExecution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountingProcedureExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingProcedureExecutionRepository extends JpaRepository<AccountingProcedureExecution, Long> {}
