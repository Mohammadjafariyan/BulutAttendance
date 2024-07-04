package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.AccProcStepExecution;
import com.bulut.attendance.repository.AccProcStepExecutionRepository;
import com.bulut.attendance.repository.search.AccProcStepExecutionSearchRepository;
import com.bulut.attendance.service.AccProcStepExecutionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.AccProcStepExecution}.
 */
@Service
@Transactional
public class AccProcStepExecutionServiceImpl implements AccProcStepExecutionService {

    private final Logger log = LoggerFactory.getLogger(AccProcStepExecutionServiceImpl.class);

    private final AccProcStepExecutionRepository accProcStepExecutionRepository;

    private final AccProcStepExecutionSearchRepository accProcStepExecutionSearchRepository;

    public AccProcStepExecutionServiceImpl(
        AccProcStepExecutionRepository accProcStepExecutionRepository,
        AccProcStepExecutionSearchRepository accProcStepExecutionSearchRepository
    ) {
        this.accProcStepExecutionRepository = accProcStepExecutionRepository;
        this.accProcStepExecutionSearchRepository = accProcStepExecutionSearchRepository;
    }

    @Override
    public AccProcStepExecution save(AccProcStepExecution accProcStepExecution) {
        log.debug("Request to save AccProcStepExecution : {}", accProcStepExecution);
        accProcStepExecution = accProcStepExecutionRepository.save(accProcStepExecution);
        accProcStepExecutionSearchRepository.index(accProcStepExecution);
        return accProcStepExecution;
    }

    @Override
    public AccProcStepExecution update(AccProcStepExecution accProcStepExecution) {
        log.debug("Request to update AccProcStepExecution : {}", accProcStepExecution);
        accProcStepExecution = accProcStepExecutionRepository.save(accProcStepExecution);
        accProcStepExecutionSearchRepository.index(accProcStepExecution);
        return accProcStepExecution;
    }

    @Override
    public Optional<AccProcStepExecution> partialUpdate(AccProcStepExecution accProcStepExecution) {
        log.debug("Request to partially update AccProcStepExecution : {}", accProcStepExecution);

        return accProcStepExecutionRepository
            .findById(accProcStepExecution.getId())
            .map(existingAccProcStepExecution -> {
                if (accProcStepExecution.getDebitAmount() != null) {
                    existingAccProcStepExecution.setDebitAmount(accProcStepExecution.getDebitAmount());
                }
                if (accProcStepExecution.getCreditAmount() != null) {
                    existingAccProcStepExecution.setCreditAmount(accProcStepExecution.getCreditAmount());
                }
                if (accProcStepExecution.getDesc() != null) {
                    existingAccProcStepExecution.setDesc(accProcStepExecution.getDesc());
                }

                return existingAccProcStepExecution;
            })
            .map(accProcStepExecutionRepository::save)
            .map(savedAccProcStepExecution -> {
                accProcStepExecutionSearchRepository.index(savedAccProcStepExecution);
                return savedAccProcStepExecution;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccProcStepExecution> findAll(Pageable pageable) {
        log.debug("Request to get all AccProcStepExecutions");
        return accProcStepExecutionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccProcStepExecution> findOne(Long id) {
        log.debug("Request to get AccProcStepExecution : {}", id);
        return accProcStepExecutionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccProcStepExecution : {}", id);
        accProcStepExecutionRepository.deleteById(id);
        accProcStepExecutionSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccProcStepExecution> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccProcStepExecutions for query {}", query);
        return accProcStepExecutionSearchRepository.search(query, pageable);
    }
}
