package com.bulut.attendance.service;

import com.bulut.attendance.domain.HrLetter;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.HrLetter}.
 */
public interface HrLetterService {
    /**
     * Save a hrLetter.
     *
     * @param hrLetter the entity to save.
     * @return the persisted entity.
     */
    HrLetter save(HrLetter hrLetter);

    /**
     * Updates a hrLetter.
     *
     * @param hrLetter the entity to update.
     * @return the persisted entity.
     */
    HrLetter update(HrLetter hrLetter);

    /**
     * Partially updates a hrLetter.
     *
     * @param hrLetter the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HrLetter> partialUpdate(HrLetter hrLetter);

    /**
     * Get all the hrLetters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HrLetter> findAll(Pageable pageable);

    /**
     * Get the "id" hrLetter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HrLetter> findOne(UUID id);

    /**
     * Delete the "id" hrLetter.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the hrLetter corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HrLetter> search(String query, Pageable pageable);
}
