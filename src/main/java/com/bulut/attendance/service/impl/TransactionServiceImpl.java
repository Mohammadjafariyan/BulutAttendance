package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.Transaction;
import com.bulut.attendance.repository.TransactionRepository;
import com.bulut.attendance.repository.search.TransactionSearchRepository;
import com.bulut.attendance.service.TransactionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final TransactionSearchRepository transactionSearchRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionSearchRepository transactionSearchRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionSearchRepository = transactionSearchRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        transaction = transactionRepository.save(transaction);
        transactionSearchRepository.index(transaction);
        return transaction;
    }

    @Override
    public Transaction update(Transaction transaction) {
        log.debug("Request to update Transaction : {}", transaction);
        transaction = transactionRepository.save(transaction);
        transactionSearchRepository.index(transaction);
        return transaction;
    }

    @Override
    public Optional<Transaction> partialUpdate(Transaction transaction) {
        log.debug("Request to partially update Transaction : {}", transaction);

        return transactionRepository
            .findById(transaction.getId())
            .map(existingTransaction -> {
                if (transaction.getIssueDateTime() != null) {
                    existingTransaction.setIssueDateTime(transaction.getIssueDateTime());
                }
                if (transaction.getTotalAmount() != null) {
                    existingTransaction.setTotalAmount(transaction.getTotalAmount());
                }
                if (transaction.getDesc() != null) {
                    existingTransaction.setDesc(transaction.getDesc());
                }
                if (transaction.getDocNumber() != null) {
                    existingTransaction.setDocNumber(transaction.getDocNumber());
                }

                return existingTransaction;
            })
            .map(transactionRepository::save)
            .map(savedTransaction -> {
                transactionSearchRepository.index(savedTransaction);
                return savedTransaction;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
        transactionSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Transactions for query {}", query);
        return transactionSearchRepository.search(query, pageable);
    }
}
