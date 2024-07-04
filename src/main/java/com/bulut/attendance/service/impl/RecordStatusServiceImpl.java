package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.RecordStatus;
import com.bulut.attendance.repository.RecordStatusRepository;
import com.bulut.attendance.repository.search.RecordStatusSearchRepository;
import com.bulut.attendance.service.RecordStatusService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.RecordStatus}.
 */
@Service
@Transactional
public class RecordStatusServiceImpl implements RecordStatusService {

    private final Logger log = LoggerFactory.getLogger(RecordStatusServiceImpl.class);

    private final RecordStatusRepository recordStatusRepository;

    private final RecordStatusSearchRepository recordStatusSearchRepository;

    public RecordStatusServiceImpl(
        RecordStatusRepository recordStatusRepository,
        RecordStatusSearchRepository recordStatusSearchRepository
    ) {
        this.recordStatusRepository = recordStatusRepository;
        this.recordStatusSearchRepository = recordStatusSearchRepository;
    }

    @Override
    public RecordStatus save(RecordStatus recordStatus) {
        log.debug("Request to save RecordStatus : {}", recordStatus);
        recordStatus = recordStatusRepository.save(recordStatus);
        recordStatusSearchRepository.index(recordStatus);
        return recordStatus;
    }

    @Override
    public RecordStatus update(RecordStatus recordStatus) {
        log.debug("Request to update RecordStatus : {}", recordStatus);
        recordStatus = recordStatusRepository.save(recordStatus);
        recordStatusSearchRepository.index(recordStatus);
        return recordStatus;
    }

    @Override
    public Optional<RecordStatus> partialUpdate(RecordStatus recordStatus) {
        log.debug("Request to partially update RecordStatus : {}", recordStatus);

        return recordStatusRepository
            .findById(recordStatus.getId())
            .map(existingRecordStatus -> {
                if (recordStatus.getFromDateTime() != null) {
                    existingRecordStatus.setFromDateTime(recordStatus.getFromDateTime());
                }
                if (recordStatus.getToDateTime() != null) {
                    existingRecordStatus.setToDateTime(recordStatus.getToDateTime());
                }
                if (recordStatus.getIsDeleted() != null) {
                    existingRecordStatus.setIsDeleted(recordStatus.getIsDeleted());
                }

                return existingRecordStatus;
            })
            .map(recordStatusRepository::save)
            .map(savedRecordStatus -> {
                recordStatusSearchRepository.index(savedRecordStatus);
                return savedRecordStatus;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordStatus> findAll() {
        log.debug("Request to get all RecordStatuses");
        return recordStatusRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecordStatus> findOne(UUID id) {
        log.debug("Request to get RecordStatus : {}", id);
        return recordStatusRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete RecordStatus : {}", id);
        recordStatusRepository.deleteById(id);
        recordStatusSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordStatus> search(String query) {
        log.debug("Request to search RecordStatuses for query {}", query);
        try {
            return StreamSupport.stream(recordStatusSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
