package com.bulut.attendance.service;

import com.bulut.attendance.domain.LeaveSummary;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.LeaveSummary}.
 */
public interface LeaveSummaryService {
    /**
     * Save a leaveSummary.
     *
     * @param leaveSummary the entity to save.
     * @return the persisted entity.
     */
    LeaveSummary save(LeaveSummary leaveSummary);

    /**
     * Updates a leaveSummary.
     *
     * @param leaveSummary the entity to update.
     * @return the persisted entity.
     */
    LeaveSummary update(LeaveSummary leaveSummary);

    /**
     * Partially updates a leaveSummary.
     *
     * @param leaveSummary the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaveSummary> partialUpdate(LeaveSummary leaveSummary);

    /**
     * Get all the leaveSummaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveSummary> findAll(Pageable pageable);

    /**
     * Get the "id" leaveSummary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveSummary> findOne(UUID id);

    /**
     * Delete the "id" leaveSummary.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the leaveSummary corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveSummary> search(String query, Pageable pageable);
}
