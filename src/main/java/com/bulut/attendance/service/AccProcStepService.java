package com.bulut.attendance.service;

import com.bulut.attendance.domain.AccProcStep;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.AccProcStep}.
 */
public interface AccProcStepService {
    /**
     * Save a accProcStep.
     *
     * @param accProcStep the entity to save.
     * @return the persisted entity.
     */
    AccProcStep save(AccProcStep accProcStep);

    /**
     * Updates a accProcStep.
     *
     * @param accProcStep the entity to update.
     * @return the persisted entity.
     */
    AccProcStep update(AccProcStep accProcStep);

    /**
     * Partially updates a accProcStep.
     *
     * @param accProcStep the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccProcStep> partialUpdate(AccProcStep accProcStep);

    /**
     * Get all the accProcSteps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccProcStep> findAll(Pageable pageable);

    /**
     * Get the "id" accProcStep.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccProcStep> findOne(Long id);

    /**
     * Delete the "id" accProcStep.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accProcStep corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccProcStep> search(String query, Pageable pageable);
}
