package com.bulut.attendance.service;

import com.bulut.attendance.domain.AccountTemplate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.AccountTemplate}.
 */
public interface AccountTemplateService {
    /**
     * Save a accountTemplate.
     *
     * @param accountTemplate the entity to save.
     * @return the persisted entity.
     */
    AccountTemplate save(AccountTemplate accountTemplate);

    /**
     * Updates a accountTemplate.
     *
     * @param accountTemplate the entity to update.
     * @return the persisted entity.
     */
    AccountTemplate update(AccountTemplate accountTemplate);

    /**
     * Partially updates a accountTemplate.
     *
     * @param accountTemplate the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountTemplate> partialUpdate(AccountTemplate accountTemplate);

    /**
     * Get all the accountTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountTemplate> findAll(Pageable pageable);

    /**
     * Get the "id" accountTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountTemplate> findOne(UUID id);

    /**
     * Delete the "id" accountTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the accountTemplate corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountTemplate> search(String query, Pageable pageable);
}
