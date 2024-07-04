package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.Work;
import com.bulut.attendance.repository.WorkRepository;
import com.bulut.attendance.repository.search.WorkSearchRepository;
import com.bulut.attendance.service.WorkService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.Work}.
 */
@Service
@Transactional
public class WorkServiceImpl implements WorkService {

    private final Logger log = LoggerFactory.getLogger(WorkServiceImpl.class);

    private final WorkRepository workRepository;

    private final WorkSearchRepository workSearchRepository;

    public WorkServiceImpl(WorkRepository workRepository, WorkSearchRepository workSearchRepository) {
        this.workRepository = workRepository;
        this.workSearchRepository = workSearchRepository;
    }

    @Override
    public Work save(Work work) {
        log.debug("Request to save Work : {}", work);
        work = workRepository.save(work);
        workSearchRepository.index(work);
        return work;
    }

    @Override
    public Work update(Work work) {
        log.debug("Request to update Work : {}", work);
        work = workRepository.save(work);
        workSearchRepository.index(work);
        return work;
    }

    @Override
    public Optional<Work> partialUpdate(Work work) {
        log.debug("Request to partially update Work : {}", work);

        return workRepository
            .findById(work.getId())
            .map(existingWork -> {
                if (work.getIssueDate() != null) {
                    existingWork.setIssueDate(work.getIssueDate());
                }
                if (work.getDesc() != null) {
                    existingWork.setDesc(work.getDesc());
                }
                if (work.getYear() != null) {
                    existingWork.setYear(work.getYear());
                }
                if (work.getMonth() != null) {
                    existingWork.setMonth(work.getMonth());
                }

                return existingWork;
            })
            .map(workRepository::save)
            .map(savedWork -> {
                workSearchRepository.index(savedWork);
                return savedWork;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Work> findAll(Pageable pageable) {
        log.debug("Request to get all Works");
        return workRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Work> findOne(UUID id) {
        log.debug("Request to get Work : {}", id);
        return workRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Work : {}", id);
        workRepository.deleteById(id);
        workSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Work> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Works for query {}", query);
        return workSearchRepository.search(query, pageable);
    }
}
