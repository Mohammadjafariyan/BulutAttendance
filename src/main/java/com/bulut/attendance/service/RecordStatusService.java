package com.bulut.attendance.service;

import com.bulut.attendance.domain.RecordStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.RecordStatus}.
 */
public interface RecordStatusService {
    /**
     * Save a recordStatus.
     *
     * @param recordStatus the entity to save.
     * @return the persisted entity.
     */
    RecordStatus save(RecordStatus recordStatus);

    /**
     * Updates a recordStatus.
     *
     * @param recordStatus the entity to update.
     * @return the persisted entity.
     */
    RecordStatus update(RecordStatus recordStatus);

    /**
     * Partially updates a recordStatus.
     *
     * @param recordStatus the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecordStatus> partialUpdate(RecordStatus recordStatus);

    /**
     * Get all the recordStatuses.
     *
     * @return the list of entities.
     */
    List<RecordStatus> findAll();

    /**
     * Get the "id" recordStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecordStatus> findOne(UUID id);

    /**
     * Delete the "id" recordStatus.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the recordStatus corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<RecordStatus> search(String query);
}
