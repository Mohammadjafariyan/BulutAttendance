package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.AccountHesab;
import com.bulut.attendance.repository.AccountHesabRepository;
import com.bulut.attendance.repository.search.AccountHesabSearchRepository;
import com.bulut.attendance.service.AccountHesabService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.AccountHesab}.
 */
@Service
@Transactional
public class AccountHesabServiceImpl implements AccountHesabService {

    private final Logger log = LoggerFactory.getLogger(AccountHesabServiceImpl.class);

    private final AccountHesabRepository accountHesabRepository;

    private final AccountHesabSearchRepository accountHesabSearchRepository;

    public AccountHesabServiceImpl(
        AccountHesabRepository accountHesabRepository,
        AccountHesabSearchRepository accountHesabSearchRepository
    ) {
        this.accountHesabRepository = accountHesabRepository;
        this.accountHesabSearchRepository = accountHesabSearchRepository;
    }

    @Override
    public AccountHesab save(AccountHesab accountHesab) {
        log.debug("Request to save AccountHesab : {}", accountHesab);
        accountHesab = accountHesabRepository.save(accountHesab);
        accountHesabSearchRepository.index(accountHesab);
        return accountHesab;
    }

    @Override
    public AccountHesab update(AccountHesab accountHesab) {
        log.debug("Request to update AccountHesab : {}", accountHesab);
        accountHesab = accountHesabRepository.save(accountHesab);
        accountHesabSearchRepository.index(accountHesab);
        return accountHesab;
    }

    @Override
    public Optional<AccountHesab> partialUpdate(AccountHesab accountHesab) {
        log.debug("Request to partially update AccountHesab : {}", accountHesab);

        return accountHesabRepository
            .findById(accountHesab.getId())
            .map(existingAccountHesab -> {
                if (accountHesab.getTitle() != null) {
                    existingAccountHesab.setTitle(accountHesab.getTitle());
                }
                if (accountHesab.getCode() != null) {
                    existingAccountHesab.setCode(accountHesab.getCode());
                }
                if (accountHesab.getLevel() != null) {
                    existingAccountHesab.setLevel(accountHesab.getLevel());
                }
                if (accountHesab.getLevelTitle() != null) {
                    existingAccountHesab.setLevelTitle(accountHesab.getLevelTitle());
                }
                if (accountHesab.getType() != null) {
                    existingAccountHesab.setType(accountHesab.getType());
                }
                if (accountHesab.getLevelInTree() != null) {
                    existingAccountHesab.setLevelInTree(accountHesab.getLevelInTree());
                }
                if (accountHesab.getDebitAmount() != null) {
                    existingAccountHesab.setDebitAmount(accountHesab.getDebitAmount());
                }
                if (accountHesab.getCreditAmount() != null) {
                    existingAccountHesab.setCreditAmount(accountHesab.getCreditAmount());
                }
                if (accountHesab.getTypeInFormula() != null) {
                    existingAccountHesab.setTypeInFormula(accountHesab.getTypeInFormula());
                }

                return existingAccountHesab;
            })
            .map(accountHesabRepository::save)
            .map(savedAccountHesab -> {
                accountHesabSearchRepository.index(savedAccountHesab);
                return savedAccountHesab;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountHesab> findAll(Pageable pageable) {
        log.debug("Request to get all AccountHesabs");
        return accountHesabRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountHesab> findOne(UUID id) {
        log.debug("Request to get AccountHesab : {}", id);
        return accountHesabRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountHesab : {}", id);
        accountHesabRepository.deleteById(id);
        accountHesabSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountHesab> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountHesabs for query {}", query);
        return accountHesabSearchRepository.search(query, pageable);
    }
}
