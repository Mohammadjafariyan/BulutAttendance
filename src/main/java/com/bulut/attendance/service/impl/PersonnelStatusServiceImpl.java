package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.PersonnelStatus;
import com.bulut.attendance.repository.PersonnelStatusRepository;
import com.bulut.attendance.repository.search.PersonnelStatusSearchRepository;
import com.bulut.attendance.service.PersonnelStatusService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.PersonnelStatus}.
 */
@Service
@Transactional
public class PersonnelStatusServiceImpl implements PersonnelStatusService {

    private final Logger log = LoggerFactory.getLogger(PersonnelStatusServiceImpl.class);

    private final PersonnelStatusRepository personnelStatusRepository;

    private final PersonnelStatusSearchRepository personnelStatusSearchRepository;

    public PersonnelStatusServiceImpl(
        PersonnelStatusRepository personnelStatusRepository,
        PersonnelStatusSearchRepository personnelStatusSearchRepository
    ) {
        this.personnelStatusRepository = personnelStatusRepository;
        this.personnelStatusSearchRepository = personnelStatusSearchRepository;
    }

    @Override
    public PersonnelStatus save(PersonnelStatus personnelStatus) {
        log.debug("Request to save PersonnelStatus : {}", personnelStatus);
        personnelStatus = personnelStatusRepository.save(personnelStatus);
        personnelStatusSearchRepository.index(personnelStatus);
        return personnelStatus;
    }

    @Override
    public PersonnelStatus update(PersonnelStatus personnelStatus) {
        log.debug("Request to update PersonnelStatus : {}", personnelStatus);
        personnelStatus = personnelStatusRepository.save(personnelStatus);
        personnelStatusSearchRepository.index(personnelStatus);
        return personnelStatus;
    }

    @Override
    public Optional<PersonnelStatus> partialUpdate(PersonnelStatus personnelStatus) {
        log.debug("Request to partially update PersonnelStatus : {}", personnelStatus);

        return personnelStatusRepository
            .findById(personnelStatus.getId())
            .map(existingPersonnelStatus -> {
                if (personnelStatus.getTitle() != null) {
                    existingPersonnelStatus.setTitle(personnelStatus.getTitle());
                }

                return existingPersonnelStatus;
            })
            .map(personnelStatusRepository::save)
            .map(savedPersonnelStatus -> {
                personnelStatusSearchRepository.index(savedPersonnelStatus);
                return savedPersonnelStatus;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonnelStatus> findAll(Pageable pageable) {
        log.debug("Request to get all PersonnelStatuses");
        return personnelStatusRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonnelStatus> findOne(UUID id) {
        log.debug("Request to get PersonnelStatus : {}", id);
        return personnelStatusRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PersonnelStatus : {}", id);
        personnelStatusRepository.deleteById(id);
        personnelStatusSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonnelStatus> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PersonnelStatuses for query {}", query);
        return personnelStatusSearchRepository.search(query, pageable);
    }
}
