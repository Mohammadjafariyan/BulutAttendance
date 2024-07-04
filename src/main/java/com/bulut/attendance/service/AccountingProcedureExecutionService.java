package com.bulut.attendance.service;

import com.bulut.attendance.domain.AccountingProcedureExecution;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.AccountingProcedureExecution}.
 */
public interface AccountingProcedureExecutionService {
    /**
     * Save a accountingProcedureExecution.
     *
     * @param accountingProcedureExecution the entity to save.
     * @return the persisted entity.
     */
    AccountingProcedureExecution save(AccountingProcedureExecution accountingProcedureExecution);

    /**
     * Updates a accountingProcedureExecution.
     *
     * @param accountingProcedureExecution the entity to update.
     * @return the persisted entity.
     */
    AccountingProcedureExecution update(AccountingProcedureExecution accountingProcedureExecution);

    /**
     * Partially updates a accountingProcedureExecution.
     *
     * @param accountingProcedureExecution the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountingProcedureExecution> partialUpdate(AccountingProcedureExecution accountingProcedureExecution);

    /**
     * Get all the accountingProcedureExecutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountingProcedureExecution> findAll(Pageable pageable);

    /**
     * Get the "id" accountingProcedureExecution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountingProcedureExecution> findOne(Long id);

    /**
     * Delete the "id" accountingProcedureExecution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accountingProcedureExecution corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountingProcedureExecution> search(String query, Pageable pageable);
}
