package com.bulut.attendance.service;

import com.bulut.attendance.domain.HrLetterType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.HrLetterType}.
 */
public interface HrLetterTypeService {
    /**
     * Save a hrLetterType.
     *
     * @param hrLetterType the entity to save.
     * @return the persisted entity.
     */
    HrLetterType save(HrLetterType hrLetterType);

    /**
     * Updates a hrLetterType.
     *
     * @param hrLetterType the entity to update.
     * @return the persisted entity.
     */
    HrLetterType update(HrLetterType hrLetterType);

    /**
     * Partially updates a hrLetterType.
     *
     * @param hrLetterType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HrLetterType> partialUpdate(HrLetterType hrLetterType);

    /**
     * Get all the hrLetterTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HrLetterType> findAll(Pageable pageable);

    /**
     * Get the "id" hrLetterType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HrLetterType> findOne(UUID id);

    /**
     * Delete the "id" hrLetterType.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the hrLetterType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HrLetterType> search(String query, Pageable pageable);
}
