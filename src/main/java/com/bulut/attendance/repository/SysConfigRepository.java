package com.bulut.attendance.repository;

import com.bulut.attendance.domain.SysConfig;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SysConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysConfigRepository extends JpaRepository<SysConfig, UUID> {}
