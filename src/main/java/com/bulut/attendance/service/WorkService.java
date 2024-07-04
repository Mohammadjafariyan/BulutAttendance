package com.bulut.attendance.service;

import com.bulut.attendance.domain.Work;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.Work}.
 */
public interface WorkService {
    /**
     * Save a work.
     *
     * @param work the entity to save.
     * @return the persisted entity.
     */
    Work save(Work work);

    /**
     * Updates a work.
     *
     * @param work the entity to update.
     * @return the persisted entity.
     */
    Work update(Work work);

    /**
     * Partially updates a work.
     *
     * @param work the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Work> partialUpdate(Work work);

    /**
     * Get all the works.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Work> findAll(Pageable pageable);

    /**
     * Get the "id" work.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Work> findOne(UUID id);

    /**
     * Delete the "id" work.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the work corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Work> search(String query, Pageable pageable);
}
