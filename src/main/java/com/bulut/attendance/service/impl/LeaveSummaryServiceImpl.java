package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.LeaveSummary;
import com.bulut.attendance.repository.LeaveSummaryRepository;
import com.bulut.attendance.repository.search.LeaveSummarySearchRepository;
import com.bulut.attendance.service.LeaveSummaryService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.LeaveSummary}.
 */
@Service
@Transactional
public class LeaveSummaryServiceImpl implements LeaveSummaryService {

    private final Logger log = LoggerFactory.getLogger(LeaveSummaryServiceImpl.class);

    private final LeaveSummaryRepository leaveSummaryRepository;

    private final LeaveSummarySearchRepository leaveSummarySearchRepository;

    public LeaveSummaryServiceImpl(
        LeaveSummaryRepository leaveSummaryRepository,
        LeaveSummarySearchRepository leaveSummarySearchRepository
    ) {
        this.leaveSummaryRepository = leaveSummaryRepository;
        this.leaveSummarySearchRepository = leaveSummarySearchRepository;
    }

    @Override
    public LeaveSummary save(LeaveSummary leaveSummary) {
        log.debug("Request to save LeaveSummary : {}", leaveSummary);
        leaveSummary = leaveSummaryRepository.save(leaveSummary);
        leaveSummarySearchRepository.index(leaveSummary);
        return leaveSummary;
    }

    @Override
    public LeaveSummary update(LeaveSummary leaveSummary) {
        log.debug("Request to update LeaveSummary : {}", leaveSummary);
        leaveSummary = leaveSummaryRepository.save(leaveSummary);
        leaveSummarySearchRepository.index(leaveSummary);
        return leaveSummary;
    }

    @Override
    public Optional<LeaveSummary> partialUpdate(LeaveSummary leaveSummary) {
        log.debug("Request to partially update LeaveSummary : {}", leaveSummary);

        return leaveSummaryRepository
            .findById(leaveSummary.getId())
            .map(existingLeaveSummary -> {
                if (leaveSummary.getRemainHours() != null) {
                    existingLeaveSummary.setRemainHours(leaveSummary.getRemainHours());
                }
                if (leaveSummary.getRemainDays() != null) {
                    existingLeaveSummary.setRemainDays(leaveSummary.getRemainDays());
                }
                if (leaveSummary.getYear() != null) {
                    existingLeaveSummary.setYear(leaveSummary.getYear());
                }

                return existingLeaveSummary;
            })
            .map(leaveSummaryRepository::save)
            .map(savedLeaveSummary -> {
                leaveSummarySearchRepository.index(savedLeaveSummary);
                return savedLeaveSummary;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveSummary> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveSummaries");
        return leaveSummaryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaveSummary> findOne(UUID id) {
        log.debug("Request to get LeaveSummary : {}", id);
        return leaveSummaryRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete LeaveSummary : {}", id);
        leaveSummaryRepository.deleteById(id);
        leaveSummarySearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveSummary> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaveSummaries for query {}", query);
        return leaveSummarySearchRepository.search(query, pageable);
    }
}
