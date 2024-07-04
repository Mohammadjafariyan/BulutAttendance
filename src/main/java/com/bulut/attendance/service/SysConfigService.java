package com.bulut.attendance.service;

import com.bulut.attendance.domain.SysConfig;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.SysConfig}.
 */
public interface SysConfigService {
    /**
     * Save a sysConfig.
     *
     * @param sysConfig the entity to save.
     * @return the persisted entity.
     */
    SysConfig save(SysConfig sysConfig);

    /**
     * Updates a sysConfig.
     *
     * @param sysConfig the entity to update.
     * @return the persisted entity.
     */
    SysConfig update(SysConfig sysConfig);

    /**
     * Partially updates a sysConfig.
     *
     * @param sysConfig the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SysConfig> partialUpdate(SysConfig sysConfig);

    /**
     * Get all the sysConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SysConfig> findAll(Pageable pageable);

    /**
     * Get the "id" sysConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SysConfig> findOne(UUID id);

    /**
     * Delete the "id" sysConfig.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the sysConfig corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SysConfig> search(String query, Pageable pageable);
}
