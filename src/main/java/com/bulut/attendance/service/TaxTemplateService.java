package com.bulut.attendance.service;

import com.bulut.attendance.domain.TaxTemplate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.TaxTemplate}.
 */
public interface TaxTemplateService {
    /**
     * Save a taxTemplate.
     *
     * @param taxTemplate the entity to save.
     * @return the persisted entity.
     */
    TaxTemplate save(TaxTemplate taxTemplate);

    /**
     * Updates a taxTemplate.
     *
     * @param taxTemplate the entity to update.
     * @return the persisted entity.
     */
    TaxTemplate update(TaxTemplate taxTemplate);

    /**
     * Partially updates a taxTemplate.
     *
     * @param taxTemplate the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaxTemplate> partialUpdate(TaxTemplate taxTemplate);

    /**
     * Get all the taxTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxTemplate> findAll(Pageable pageable);

    /**
     * Get the "id" taxTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxTemplate> findOne(UUID id);

    /**
     * Delete the "id" taxTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the taxTemplate corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxTemplate> search(String query, Pageable pageable);
}
