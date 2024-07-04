package com.bulut.attendance.service;

import com.bulut.attendance.domain.AccProccParameter;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.AccProccParameter}.
 */
public interface AccProccParameterService {
    /**
     * Save a accProccParameter.
     *
     * @param accProccParameter the entity to save.
     * @return the persisted entity.
     */
    AccProccParameter save(AccProccParameter accProccParameter);

    /**
     * Updates a accProccParameter.
     *
     * @param accProccParameter the entity to update.
     * @return the persisted entity.
     */
    AccProccParameter update(AccProccParameter accProccParameter);

    /**
     * Partially updates a accProccParameter.
     *
     * @param accProccParameter the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccProccParameter> partialUpdate(AccProccParameter accProccParameter);

    /**
     * Get all the accProccParameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccProccParameter> findAll(Pageable pageable);

    /**
     * Get the "id" accProccParameter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccProccParameter> findOne(Long id);

    /**
     * Delete the "id" accProccParameter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accProccParameter corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccProccParameter> search(String query, Pageable pageable);
}
