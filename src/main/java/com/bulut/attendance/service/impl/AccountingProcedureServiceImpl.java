package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.AccountingProcedure;
import com.bulut.attendance.repository.AccountingProcedureRepository;
import com.bulut.attendance.repository.search.AccountingProcedureSearchRepository;
import com.bulut.attendance.service.AccountingProcedureService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.AccountingProcedure}.
 */
@Service
@Transactional
public class AccountingProcedureServiceImpl implements AccountingProcedureService {

    private final Logger log = LoggerFactory.getLogger(AccountingProcedureServiceImpl.class);

    private final AccountingProcedureRepository accountingProcedureRepository;

    private final AccountingProcedureSearchRepository accountingProcedureSearchRepository;

    public AccountingProcedureServiceImpl(
        AccountingProcedureRepository accountingProcedureRepository,
        AccountingProcedureSearchRepository accountingProcedureSearchRepository
    ) {
        this.accountingProcedureRepository = accountingProcedureRepository;
        this.accountingProcedureSearchRepository = accountingProcedureSearchRepository;
    }

    @Override
    public AccountingProcedure save(AccountingProcedure accountingProcedure) {
        log.debug("Request to save AccountingProcedure : {}", accountingProcedure);
        accountingProcedure = accountingProcedureRepository.save(accountingProcedure);
        accountingProcedureSearchRepository.index(accountingProcedure);
        return accountingProcedure;
    }

    @Override
    public AccountingProcedure update(AccountingProcedure accountingProcedure) {
        log.debug("Request to update AccountingProcedure : {}", accountingProcedure);
        accountingProcedure = accountingProcedureRepository.save(accountingProcedure);
        accountingProcedureSearchRepository.index(accountingProcedure);
        return accountingProcedure;
    }

    @Override
    public Optional<AccountingProcedure> partialUpdate(AccountingProcedure accountingProcedure) {
        log.debug("Request to partially update AccountingProcedure : {}", accountingProcedure);

        return accountingProcedureRepository
            .findById(accountingProcedure.getId())
            .map(existingAccountingProcedure -> {
                if (accountingProcedure.getTitle() != null) {
                    existingAccountingProcedure.setTitle(accountingProcedure.getTitle());
                }

                return existingAccountingProcedure;
            })
            .map(accountingProcedureRepository::save)
            .map(savedAccountingProcedure -> {
                accountingProcedureSearchRepository.index(savedAccountingProcedure);
                return savedAccountingProcedure;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountingProcedure> findAll(Pageable pageable) {
        log.debug("Request to get all AccountingProcedures");
        return accountingProcedureRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountingProcedure> findOne(Long id) {
        log.debug("Request to get AccountingProcedure : {}", id);
        return accountingProcedureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountingProcedure : {}", id);
        accountingProcedureRepository.deleteById(id);
        accountingProcedureSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountingProcedure> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountingProcedures for query {}", query);
        return accountingProcedureSearchRepository.search(query, pageable);
    }
}
