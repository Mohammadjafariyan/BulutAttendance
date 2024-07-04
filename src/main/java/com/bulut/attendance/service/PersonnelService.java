package com.bulut.attendance.service;

import com.bulut.attendance.domain.Personnel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.Personnel}.
 */
public interface PersonnelService {
    /**
     * Save a personnel.
     *
     * @param personnel the entity to save.
     * @return the persisted entity.
     */
    Personnel save(Personnel personnel);

    /**
     * Updates a personnel.
     *
     * @param personnel the entity to update.
     * @return the persisted entity.
     */
    Personnel update(Personnel personnel);

    /**
     * Partially updates a personnel.
     *
     * @param personnel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Personnel> partialUpdate(Personnel personnel);

    /**
     * Get all the personnel.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Personnel> findAll(Pageable pageable);

    /**
     * Get the "id" personnel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Personnel> findOne(UUID id);

    /**
     * Delete the "id" personnel.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the personnel corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Personnel> search(String query, Pageable pageable);
}
