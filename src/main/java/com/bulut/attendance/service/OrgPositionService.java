package com.bulut.attendance.service;

import com.bulut.attendance.domain.OrgPosition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.OrgPosition}.
 */
public interface OrgPositionService {
    /**
     * Save a orgPosition.
     *
     * @param orgPosition the entity to save.
     * @return the persisted entity.
     */
    OrgPosition save(OrgPosition orgPosition);

    /**
     * Updates a orgPosition.
     *
     * @param orgPosition the entity to update.
     * @return the persisted entity.
     */
    OrgPosition update(OrgPosition orgPosition);

    /**
     * Partially updates a orgPosition.
     *
     * @param orgPosition the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrgPosition> partialUpdate(OrgPosition orgPosition);

    /**
     * Get all the orgPositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrgPosition> findAll(Pageable pageable);

    /**
     * Get the "id" orgPosition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrgPosition> findOne(UUID id);

    /**
     * Delete the "id" orgPosition.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the orgPosition corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrgPosition> search(String query, Pageable pageable);
}
