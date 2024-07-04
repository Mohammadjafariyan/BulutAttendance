package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.AccountTemplate;
import com.bulut.attendance.repository.AccountTemplateRepository;
import com.bulut.attendance.repository.search.AccountTemplateSearchRepository;
import com.bulut.attendance.service.AccountTemplateService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.AccountTemplate}.
 */
@Service
@Transactional
public class AccountTemplateServiceImpl implements AccountTemplateService {

    private final Logger log = LoggerFactory.getLogger(AccountTemplateServiceImpl.class);

    private final AccountTemplateRepository accountTemplateRepository;

    private final AccountTemplateSearchRepository accountTemplateSearchRepository;

    public AccountTemplateServiceImpl(
        AccountTemplateRepository accountTemplateRepository,
        AccountTemplateSearchRepository accountTemplateSearchRepository
    ) {
        this.accountTemplateRepository = accountTemplateRepository;
        this.accountTemplateSearchRepository = accountTemplateSearchRepository;
    }

    @Override
    public AccountTemplate save(AccountTemplate accountTemplate) {
        log.debug("Request to save AccountTemplate : {}", accountTemplate);
        accountTemplate = accountTemplateRepository.save(accountTemplate);
        accountTemplateSearchRepository.index(accountTemplate);
        return accountTemplate;
    }

    @Override
    public AccountTemplate update(AccountTemplate accountTemplate) {
        log.debug("Request to update AccountTemplate : {}", accountTemplate);
        accountTemplate = accountTemplateRepository.save(accountTemplate);
        accountTemplateSearchRepository.index(accountTemplate);
        return accountTemplate;
    }

    @Override
    public Optional<AccountTemplate> partialUpdate(AccountTemplate accountTemplate) {
        log.debug("Request to partially update AccountTemplate : {}", accountTemplate);

        return accountTemplateRepository
            .findById(accountTemplate.getId())
            .map(existingAccountTemplate -> {
                if (accountTemplate.getTitle() != null) {
                    existingAccountTemplate.setTitle(accountTemplate.getTitle());
                }
                if (accountTemplate.getCode() != null) {
                    existingAccountTemplate.setCode(accountTemplate.getCode());
                }
                if (accountTemplate.getLevel() != null) {
                    existingAccountTemplate.setLevel(accountTemplate.getLevel());
                }
                if (accountTemplate.getLevelTitle() != null) {
                    existingAccountTemplate.setLevelTitle(accountTemplate.getLevelTitle());
                }
                if (accountTemplate.getType() != null) {
                    existingAccountTemplate.setType(accountTemplate.getType());
                }
                if (accountTemplate.getLevelInTree() != null) {
                    existingAccountTemplate.setLevelInTree(accountTemplate.getLevelInTree());
                }
                if (accountTemplate.getDebitAmount() != null) {
                    existingAccountTemplate.setDebitAmount(accountTemplate.getDebitAmount());
                }
                if (accountTemplate.getCreditAmount() != null) {
                    existingAccountTemplate.setCreditAmount(accountTemplate.getCreditAmount());
                }
                if (accountTemplate.getTypeInFormula() != null) {
                    existingAccountTemplate.setTypeInFormula(accountTemplate.getTypeInFormula());
                }

                return existingAccountTemplate;
            })
            .map(accountTemplateRepository::save)
            .map(savedAccountTemplate -> {
                accountTemplateSearchRepository.index(savedAccountTemplate);
                return savedAccountTemplate;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountTemplate> findAll(Pageable pageable) {
        log.debug("Request to get all AccountTemplates");
        return accountTemplateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountTemplate> findOne(UUID id) {
        log.debug("Request to get AccountTemplate : {}", id);
        return accountTemplateRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AccountTemplate : {}", id);
        accountTemplateRepository.deleteById(id);
        accountTemplateSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountTemplate> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountTemplates for query {}", query);
        return accountTemplateSearchRepository.search(query, pageable);
    }
}
