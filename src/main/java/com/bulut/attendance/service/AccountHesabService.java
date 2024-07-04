package com.bulut.attendance.service;

import com.bulut.attendance.domain.AccountHesab;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.AccountHesab}.
 */
public interface AccountHesabService {
    /**
     * Save a accountHesab.
     *
     * @param accountHesab the entity to save.
     * @return the persisted entity.
     */
    AccountHesab save(AccountHesab accountHesab);

    /**
     * Updates a accountHesab.
     *
     * @param accountHesab the entity to update.
     * @return the persisted entity.
     */
    AccountHesab update(AccountHesab accountHesab);

    /**
     * Partially updates a accountHesab.
     *
     * @param accountHesab the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountHesab> partialUpdate(AccountHesab accountHesab);

    /**
     * Get all the accountHesabs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountHesab> findAll(Pageable pageable);

    /**
     * Get the "id" accountHesab.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountHesab> findOne(UUID id);

    /**
     * Delete the "id" accountHesab.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the accountHesab corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountHesab> search(String query, Pageable pageable);
}
