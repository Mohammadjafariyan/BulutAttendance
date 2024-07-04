package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.AccountingProcedureExecution;
import com.bulut.attendance.repository.AccountingProcedureExecutionRepository;
import com.bulut.attendance.repository.search.AccountingProcedureExecutionSearchRepository;
import com.bulut.attendance.service.AccountingProcedureExecutionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.AccountingProcedureExecution}.
 */
@Service
@Transactional
public class AccountingProcedureExecutionServiceImpl implements AccountingProcedureExecutionService {

    private final Logger log = LoggerFactory.getLogger(AccountingProcedureExecutionServiceImpl.class);

    private final AccountingProcedureExecutionRepository accountingProcedureExecutionRepository;

    private final AccountingProcedureExecutionSearchRepository accountingProcedureExecutionSearchRepository;

    public AccountingProcedureExecutionServiceImpl(
        AccountingProcedureExecutionRepository accountingProcedureExecutionRepository,
        AccountingProcedureExecutionSearchRepository accountingProcedureExecutionSearchRepository
    ) {
        this.accountingProcedureExecutionRepository = accountingProcedureExecutionRepository;
        this.accountingProcedureExecutionSearchRepository = accountingProcedureExecutionSearchRepository;
    }

    @Override
    public AccountingProcedureExecution save(AccountingProcedureExecution accountingProcedureExecution) {
        log.debug("Request to save AccountingProcedureExecution : {}", accountingProcedureExecution);
        accountingProcedureExecution = accountingProcedureExecutionRepository.save(accountingProcedureExecution);
        accountingProcedureExecutionSearchRepository.index(accountingProcedureExecution);
        return accountingProcedureExecution;
    }

    @Override
    public AccountingProcedureExecution update(AccountingProcedureExecution accountingProcedureExecution) {
        log.debug("Request to update AccountingProcedureExecution : {}", accountingProcedureExecution);
        accountingProcedureExecution = accountingProcedureExecutionRepository.save(accountingProcedureExecution);
        accountingProcedureExecutionSearchRepository.index(accountingProcedureExecution);
        return accountingProcedureExecution;
    }

    @Override
    public Optional<AccountingProcedureExecution> partialUpdate(AccountingProcedureExecution accountingProcedureExecution) {
        log.debug("Request to partially update AccountingProcedureExecution : {}", accountingProcedureExecution);

        return accountingProcedureExecutionRepository
            .findById(accountingProcedureExecution.getId())
            .map(existingAccountingProcedureExecution -> {
                if (accountingProcedureExecution.getDateTime() != null) {
                    existingAccountingProcedureExecution.setDateTime(accountingProcedureExecution.getDateTime());
                }
                if (accountingProcedureExecution.getDesc() != null) {
                    existingAccountingProcedureExecution.setDesc(accountingProcedureExecution.getDesc());
                }

                return existingAccountingProcedureExecution;
            })
            .map(accountingProcedureExecutionRepository::save)
            .map(savedAccountingProcedureExecution -> {
                accountingProcedureExecutionSearchRepository.index(savedAccountingProcedureExecution);
                return savedAccountingProcedureExecution;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountingProcedureExecution> findAll(Pageable pageable) {
        log.debug("Request to get all AccountingProcedureExecutions");
        return accountingProcedureExecutionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountingProcedureExecution> findOne(Long id) {
        log.debug("Request to get AccountingProcedureExecution : {}", id);
        return accountingProcedureExecutionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountingProcedureExecution : {}", id);
        accountingProcedureExecutionRepository.deleteById(id);
        accountingProcedureExecutionSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountingProcedureExecution> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountingProcedureExecutions for query {}", query);
        return accountingProcedureExecutionSearchRepository.search(query, pageable);
    }
}
