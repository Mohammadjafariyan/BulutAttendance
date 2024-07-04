package com.bulut.attendance.service;

import com.bulut.attendance.domain.Bank;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bulut.attendance.domain.Bank}.
 */
public interface BankService {
    /**
     * Save a bank.
     *
     * @param bank the entity to save.
     * @return the persisted entity.
     */
    Bank save(Bank bank);

    /**
     * Updates a bank.
     *
     * @param bank the entity to update.
     * @return the persisted entity.
     */
    Bank update(Bank bank);

    /**
     * Partially updates a bank.
     *
     * @param bank the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Bank> partialUpdate(Bank bank);

    /**
     * Get all the banks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Bank> findAll(Pageable pageable);

    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Bank> findOne(UUID id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the bank corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Bank> search(String query, Pageable pageable);
}
