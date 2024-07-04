package com.bulut.attendance.service;

import com.bulut.attendance.domain.AccProcStepExecution;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.AccProcStepExecution}.
 */
public interface AccProcStepExecutionService {
    /**
     * Save a accProcStepExecution.
     *
     * @param accProcStepExecution the entity to save.
     * @return the persisted entity.
     */
    AccProcStepExecution save(AccProcStepExecution accProcStepExecution);

    /**
     * Updates a accProcStepExecution.
     *
     * @param accProcStepExecution the entity to update.
     * @return the persisted entity.
     */
    AccProcStepExecution update(AccProcStepExecution accProcStepExecution);

    /**
     * Partially updates a accProcStepExecution.
     *
     * @param accProcStepExecution the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccProcStepExecution> partialUpdate(AccProcStepExecution accProcStepExecution);

    /**
     * Get all the accProcStepExecutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccProcStepExecution> findAll(Pageable pageable);

    /**
     * Get the "id" accProcStepExecution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccProcStepExecution> findOne(Long id);

    /**
     * Delete the "id" accProcStepExecution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accProcStepExecution corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccProcStepExecution> search(String query, Pageable pageable);
}
