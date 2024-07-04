package com.bulut.attendance.service;

import com.bulut.attendance.domain.TransactionAccount;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.TransactionAccount}.
 */
public interface TransactionAccountService {
    /**
     * Save a transactionAccount.
     *
     * @param transactionAccount the entity to save.
     * @return the persisted entity.
     */
    TransactionAccount save(TransactionAccount transactionAccount);

    /**
     * Updates a transactionAccount.
     *
     * @param transactionAccount the entity to update.
     * @return the persisted entity.
     */
    TransactionAccount update(TransactionAccount transactionAccount);

    /**
     * Partially updates a transactionAccount.
     *
     * @param transactionAccount the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionAccount> partialUpdate(TransactionAccount transactionAccount);

    /**
     * Get all the transactionAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionAccount> findAll(Pageable pageable);

    /**
     * Get the "id" transactionAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionAccount> findOne(Long id);

    /**
     * Delete the "id" transactionAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the transactionAccount corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionAccount> search(String query, Pageable pageable);
}
