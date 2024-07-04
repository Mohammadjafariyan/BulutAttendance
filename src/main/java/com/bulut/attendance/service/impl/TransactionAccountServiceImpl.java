package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.TransactionAccount;
import com.bulut.attendance.repository.TransactionAccountRepository;
import com.bulut.attendance.repository.search.TransactionAccountSearchRepository;
import com.bulut.attendance.service.TransactionAccountService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.TransactionAccount}.
 */
@Service
@Transactional
public class TransactionAccountServiceImpl implements TransactionAccountService {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountServiceImpl.class);

    private final TransactionAccountRepository transactionAccountRepository;

    private final TransactionAccountSearchRepository transactionAccountSearchRepository;

    public TransactionAccountServiceImpl(
        TransactionAccountRepository transactionAccountRepository,
        TransactionAccountSearchRepository transactionAccountSearchRepository
    ) {
        this.transactionAccountRepository = transactionAccountRepository;
        this.transactionAccountSearchRepository = transactionAccountSearchRepository;
    }

    @Override
    public TransactionAccount save(TransactionAccount transactionAccount) {
        log.debug("Request to save TransactionAccount : {}", transactionAccount);
        transactionAccount = transactionAccountRepository.save(transactionAccount);
        transactionAccountSearchRepository.index(transactionAccount);
        return transactionAccount;
    }

    @Override
    public TransactionAccount update(TransactionAccount transactionAccount) {
        log.debug("Request to update TransactionAccount : {}", transactionAccount);
        transactionAccount = transactionAccountRepository.save(transactionAccount);
        transactionAccountSearchRepository.index(transactionAccount);
        return transactionAccount;
    }

    @Override
    public Optional<TransactionAccount> partialUpdate(TransactionAccount transactionAccount) {
        log.debug("Request to partially update TransactionAccount : {}", transactionAccount);

        return transactionAccountRepository
            .findById(transactionAccount.getId())
            .map(existingTransactionAccount -> {
                if (transactionAccount.getDebitAmount() != null) {
                    existingTransactionAccount.setDebitAmount(transactionAccount.getDebitAmount());
                }
                if (transactionAccount.getCreditAmount() != null) {
                    existingTransactionAccount.setCreditAmount(transactionAccount.getCreditAmount());
                }

                return existingTransactionAccount;
            })
            .map(transactionAccountRepository::save)
            .map(savedTransactionAccount -> {
                transactionAccountSearchRepository.index(savedTransactionAccount);
                return savedTransactionAccount;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccount> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionAccounts");
        return transactionAccountRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionAccount> findOne(Long id) {
        log.debug("Request to get TransactionAccount : {}", id);
        return transactionAccountRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionAccount : {}", id);
        transactionAccountRepository.deleteById(id);
        transactionAccountSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAccount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionAccounts for query {}", query);
        return transactionAccountSearchRepository.search(query, pageable);
    }
}
