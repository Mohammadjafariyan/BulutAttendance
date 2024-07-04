package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.Bank;
import com.bulut.attendance.repository.BankRepository;
import com.bulut.attendance.repository.search.BankSearchRepository;
import com.bulut.attendance.service.BankService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.Bank}.
 */
@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    private final BankRepository bankRepository;

    private final BankSearchRepository bankSearchRepository;

    public BankServiceImpl(BankRepository bankRepository, BankSearchRepository bankSearchRepository) {
        this.bankRepository = bankRepository;
        this.bankSearchRepository = bankSearchRepository;
    }

    @Override
    public Bank save(Bank bank) {
        log.debug("Request to save Bank : {}", bank);
        bank = bankRepository.save(bank);
        bankSearchRepository.index(bank);
        return bank;
    }

    @Override
    public Bank update(Bank bank) {
        log.debug("Request to update Bank : {}", bank);
        bank = bankRepository.save(bank);
        bankSearchRepository.index(bank);
        return bank;
    }

    @Override
    public Optional<Bank> partialUpdate(Bank bank) {
        log.debug("Request to partially update Bank : {}", bank);

        return bankRepository
            .findById(bank.getId())
            .map(existingBank -> {
                if (bank.getTitle() != null) {
                    existingBank.setTitle(bank.getTitle());
                }
                if (bank.getCode() != null) {
                    existingBank.setCode(bank.getCode());
                }

                return existingBank;
            })
            .map(bankRepository::save)
            .map(savedBank -> {
                bankSearchRepository.index(savedBank);
                return savedBank;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bank> findAll(Pageable pageable) {
        log.debug("Request to get all Banks");
        return bankRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bank> findOne(UUID id) {
        log.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Bank : {}", id);
        bankRepository.deleteById(id);
        bankSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bank> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Banks for query {}", query);
        return bankSearchRepository.search(query, pageable);
    }
}
