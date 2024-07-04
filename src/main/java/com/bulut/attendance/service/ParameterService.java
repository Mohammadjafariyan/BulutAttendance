package com.bulut.attendance.service;

import com.bulut.attendance.domain.Parameter;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.Parameter}.
 */
public interface ParameterService {
    /**
     * Save a parameter.
     *
     * @param parameter the entity to save.
     * @return the persisted entity.
     */
    Parameter save(Parameter parameter);

    /**
     * Updates a parameter.
     *
     * @param parameter the entity to update.
     * @return the persisted entity.
     */
    Parameter update(Parameter parameter);

    /**
     * Partially updates a parameter.
     *
     * @param parameter the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Parameter> partialUpdate(Parameter parameter);

    /**
     * Get all the parameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Parameter> findAll(Pageable pageable);

    /**
     * Get the "id" parameter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Parameter> findOne(Long id);

    /**
     * Delete the "id" parameter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the parameter corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Parameter> search(String query, Pageable pageable);
}
