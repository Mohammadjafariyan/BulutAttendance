package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.AccProcStep;
import com.bulut.attendance.repository.AccProcStepRepository;
import com.bulut.attendance.repository.search.AccProcStepSearchRepository;
import com.bulut.attendance.service.AccProcStepService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.AccProcStep}.
 */
@Service
@Transactional
public class AccProcStepServiceImpl implements AccProcStepService {

    private final Logger log = LoggerFactory.getLogger(AccProcStepServiceImpl.class);

    private final AccProcStepRepository accProcStepRepository;

    private final AccProcStepSearchRepository accProcStepSearchRepository;

    public AccProcStepServiceImpl(AccProcStepRepository accProcStepRepository, AccProcStepSearchRepository accProcStepSearchRepository) {
        this.accProcStepRepository = accProcStepRepository;
        this.accProcStepSearchRepository = accProcStepSearchRepository;
    }

    @Override
    public AccProcStep save(AccProcStep accProcStep) {
        log.debug("Request to save AccProcStep : {}", accProcStep);
        accProcStep = accProcStepRepository.save(accProcStep);
        accProcStepSearchRepository.index(accProcStep);
        return accProcStep;
    }

    @Override
    public AccProcStep update(AccProcStep accProcStep) {
        log.debug("Request to update AccProcStep : {}", accProcStep);
        accProcStep = accProcStepRepository.save(accProcStep);
        accProcStepSearchRepository.index(accProcStep);
        return accProcStep;
    }

    @Override
    public Optional<AccProcStep> partialUpdate(AccProcStep accProcStep) {
        log.debug("Request to partially update AccProcStep : {}", accProcStep);

        return accProcStepRepository
            .findById(accProcStep.getId())
            .map(accProcStepRepository::save)
            .map(savedAccProcStep -> {
                accProcStepSearchRepository.index(savedAccProcStep);
                return savedAccProcStep;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccProcStep> findAll(Pageable pageable) {
        log.debug("Request to get all AccProcSteps");
        return accProcStepRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccProcStep> findOne(Long id) {
        log.debug("Request to get AccProcStep : {}", id);
        return accProcStepRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccProcStep : {}", id);
        accProcStepRepository.deleteById(id);
        accProcStepSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccProcStep> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccProcSteps for query {}", query);
        return accProcStepSearchRepository.search(query, pageable);
    }
}
