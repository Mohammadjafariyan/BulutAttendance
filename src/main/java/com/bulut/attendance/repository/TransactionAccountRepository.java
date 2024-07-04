package com.bulut.attendance.repository;

import com.bulut.attendance.domain.TransactionAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransactionAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionAccountRepository extends JpaRepository<TransactionAccount, Long> {}
