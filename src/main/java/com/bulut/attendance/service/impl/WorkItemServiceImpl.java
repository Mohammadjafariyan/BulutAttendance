package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.WorkItem;
import com.bulut.attendance.repository.WorkItemRepository;
import com.bulut.attendance.repository.search.WorkItemSearchRepository;
import com.bulut.attendance.service.WorkItemService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.WorkItem}.
 */
@Service
@Transactional
public class WorkItemServiceImpl implements WorkItemService {

    private final Logger log = LoggerFactory.getLogger(WorkItemServiceImpl.class);

    private final WorkItemRepository workItemRepository;

    private final WorkItemSearchRepository workItemSearchRepository;

    public WorkItemServiceImpl(WorkItemRepository workItemRepository, WorkItemSearchRepository workItemSearchRepository) {
        this.workItemRepository = workItemRepository;
        this.workItemSearchRepository = workItemSearchRepository;
    }

    @Override
    public WorkItem save(WorkItem workItem) {
        log.debug("Request to save WorkItem : {}", workItem);
        workItem = workItemRepository.save(workItem);
        workItemSearchRepository.index(workItem);
        return workItem;
    }

    @Override
    public WorkItem update(WorkItem workItem) {
        log.debug("Request to update WorkItem : {}", workItem);
        workItem = workItemRepository.save(workItem);
        workItemSearchRepository.index(workItem);
        return workItem;
    }

    @Override
    public Optional<WorkItem> partialUpdate(WorkItem workItem) {
        log.debug("Request to partially update WorkItem : {}", workItem);

        return workItemRepository
            .findById(workItem.getId())
            .map(existingWorkItem -> {
                if (workItem.getAmount() != null) {
                    existingWorkItem.setAmount(workItem.getAmount());
                }

                return existingWorkItem;
            })
            .map(workItemRepository::save)
            .map(savedWorkItem -> {
                workItemSearchRepository.index(savedWorkItem);
                return savedWorkItem;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkItem> findAll(Pageable pageable) {
        log.debug("Request to get all WorkItems");
        return workItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkItem> findOne(UUID id) {
        log.debug("Request to get WorkItem : {}", id);
        return workItemRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete WorkItem : {}", id);
        workItemRepository.deleteById(id);
        workItemSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkItem> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkItems for query {}", query);
        return workItemSearchRepository.search(query, pageable);
    }
}
