package com.bulut.attendance.service;

import com.bulut.attendance.domain.Leave;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.Leave}.
 */
public interface LeaveService {
    /**
     * Save a leave.
     *
     * @param leave the entity to save.
     * @return the persisted entity.
     */
    Leave save(Leave leave);

    /**
     * Updates a leave.
     *
     * @param leave the entity to update.
     * @return the persisted entity.
     */
    Leave update(Leave leave);

    /**
     * Partially updates a leave.
     *
     * @param leave the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Leave> partialUpdate(Leave leave);

    /**
     * Get all the leaves.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Leave> findAll(Pageable pageable);

    /**
     * Get the "id" leave.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Leave> findOne(UUID id);

    /**
     * Delete the "id" leave.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the leave corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Leave> search(String query, Pageable pageable);
}
