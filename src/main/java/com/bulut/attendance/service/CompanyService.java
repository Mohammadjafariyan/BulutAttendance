package com.bulut.attendance.service;

import com.bulut.attendance.domain.Company;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.Company}.
 */
public interface CompanyService {
    /**
     * Save a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    Company save(Company company);

    /**
     * Updates a company.
     *
     * @param company the entity to update.
     * @return the persisted entity.
     */
    Company update(Company company);

    /**
     * Partially updates a company.
     *
     * @param company the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Company> partialUpdate(Company company);

    /**
     * Get all the companies.
     *
     * @return the list of entities.
     */
    List<Company> findAll();

    /**
     * Get the "id" company.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Company> findOne(Long id);

    /**
     * Delete the "id" company.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the company corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Company> search(String query);
}
