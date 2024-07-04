package com.bulut.attendance.service;

import com.bulut.attendance.domain.AccountingProcedure;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.AccountingProcedure}.
 */
public interface AccountingProcedureService {
    /**
     * Save a accountingProcedure.
     *
     * @param accountingProcedure the entity to save.
     * @return the persisted entity.
     */
    AccountingProcedure save(AccountingProcedure accountingProcedure);

    /**
     * Updates a accountingProcedure.
     *
     * @param accountingProcedure the entity to update.
     * @return the persisted entity.
     */
    AccountingProcedure update(AccountingProcedure accountingProcedure);

    /**
     * Partially updates a accountingProcedure.
     *
     * @param accountingProcedure the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountingProcedure> partialUpdate(AccountingProcedure accountingProcedure);

    /**
     * Get all the accountingProcedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountingProcedure> findAll(Pageable pageable);

    /**
     * Get the "id" accountingProcedure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountingProcedure> findOne(Long id);

    /**
     * Delete the "id" accountingProcedure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accountingProcedure corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountingProcedure> search(String query, Pageable pageable);
}
