package com.bulut.attendance.repository;

import com.bulut.attendance.domain.AccountingProcedure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountingProcedure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingProcedureRepository extends JpaRepository<AccountingProcedure, Long> {}
