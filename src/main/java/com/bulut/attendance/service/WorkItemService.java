package com.bulut.attendance.service;

import com.bulut.attendance.domain.WorkItem;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.WorkItem}.
 */
public interface WorkItemService {
    /**
     * Save a workItem.
     *
     * @param workItem the entity to save.
     * @return the persisted entity.
     */
    WorkItem save(WorkItem workItem);

    /**
     * Updates a workItem.
     *
     * @param workItem the entity to update.
     * @return the persisted entity.
     */
    WorkItem update(WorkItem workItem);

    /**
     * Partially updates a workItem.
     *
     * @param workItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkItem> partialUpdate(WorkItem workItem);

    /**
     * Get all the workItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkItem> findAll(Pageable pageable);

    /**
     * Get the "id" workItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkItem> findOne(UUID id);

    /**
     * Delete the "id" workItem.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the workItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkItem> search(String query, Pageable pageable);
}
