package com.bulut.attendance.service;

import com.bulut.attendance.domain.PersonnelStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.PersonnelStatus}.
 */
public interface PersonnelStatusService {
    /**
     * Save a personnelStatus.
     *
     * @param personnelStatus the entity to save.
     * @return the persisted entity.
     */
    PersonnelStatus save(PersonnelStatus personnelStatus);

    /**
     * Updates a personnelStatus.
     *
     * @param personnelStatus the entity to update.
     * @return the persisted entity.
     */
    PersonnelStatus update(PersonnelStatus personnelStatus);

    /**
     * Partially updates a personnelStatus.
     *
     * @param personnelStatus the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonnelStatus> partialUpdate(PersonnelStatus personnelStatus);

    /**
     * Get all the personnelStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PersonnelStatus> findAll(Pageable pageable);

    /**
     * Get the "id" personnelStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonnelStatus> findOne(UUID id);

    /**
     * Delete the "id" personnelStatus.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the personnelStatus corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PersonnelStatus> search(String query, Pageable pageable);
}
