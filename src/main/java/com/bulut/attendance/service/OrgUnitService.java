package com.bulut.attendance.service;

import com.bulut.attendance.domain.OrgUnit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.OrgUnit}.
 */
public interface OrgUnitService {
    /**
     * Save a orgUnit.
     *
     * @param orgUnit the entity to save.
     * @return the persisted entity.
     */
    OrgUnit save(OrgUnit orgUnit);

    /**
     * Updates a orgUnit.
     *
     * @param orgUnit the entity to update.
     * @return the persisted entity.
     */
    OrgUnit update(OrgUnit orgUnit);

    /**
     * Partially updates a orgUnit.
     *
     * @param orgUnit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrgUnit> partialUpdate(OrgUnit orgUnit);

    /**
     * Get all the orgUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrgUnit> findAll(Pageable pageable);

    /**
     * Get the "id" orgUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrgUnit> findOne(UUID id);

    /**
     * Delete the "id" orgUnit.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the orgUnit corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrgUnit> search(String query, Pageable pageable);
}
