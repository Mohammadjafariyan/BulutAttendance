package com.bulut.attendance.repository;

import com.bulut.attendance.domain.AccountHesab;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountHesab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountHesabRepository extends JpaRepository<AccountHesab, UUID> { }
