package com.bulut.attendance.service;

import com.bulut.attendance.domain.HrLetterParameter;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.HrLetterParameter}.
 */
public interface HrLetterParameterService {
    /**
     * Save a hrLetterParameter.
     *
     * @param hrLetterParameter the entity to save.
     * @return the persisted entity.
     */
    HrLetterParameter save(HrLetterParameter hrLetterParameter);

    /**
     * Updates a hrLetterParameter.
     *
     * @param hrLetterParameter the entity to update.
     * @return the persisted entity.
     */
    HrLetterParameter update(HrLetterParameter hrLetterParameter);

    /**
     * Partially updates a hrLetterParameter.
     *
     * @param hrLetterParameter the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HrLetterParameter> partialUpdate(HrLetterParameter hrLetterParameter);

    /**
     * Get all the hrLetterParameters.
     *
     * @return the list of entities.
     */
    List<HrLetterParameter> findAll();

    /**
     * Get all the HrLetterParameter where WorkItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<HrLetterParameter> findAllWhereWorkItemIsNull();

    /**
     * Get the "id" hrLetterParameter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HrLetterParameter> findOne(Long id);

    /**
     * Delete the "id" hrLetterParameter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the hrLetterParameter corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<HrLetterParameter> search(String query);
}
