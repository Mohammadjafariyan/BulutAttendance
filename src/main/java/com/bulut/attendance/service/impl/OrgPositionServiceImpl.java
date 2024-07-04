package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.OrgPosition;
import com.bulut.attendance.repository.OrgPositionRepository;
import com.bulut.attendance.repository.search.OrgPositionSearchRepository;
import com.bulut.attendance.service.OrgPositionService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.OrgPosition}.
 */
@Service
@Transactional
public class OrgPositionServiceImpl implements OrgPositionService {

    private final Logger log = LoggerFactory.getLogger(OrgPositionServiceImpl.class);

    private final OrgPositionRepository orgPositionRepository;

    private final OrgPositionSearchRepository orgPositionSearchRepository;

    public OrgPositionServiceImpl(OrgPositionRepository orgPositionRepository, OrgPositionSearchRepository orgPositionSearchRepository) {
        this.orgPositionRepository = orgPositionRepository;
        this.orgPositionSearchRepository = orgPositionSearchRepository;
    }

    @Override
    public OrgPosition save(OrgPosition orgPosition) {
        log.debug("Request to save OrgPosition : {}", orgPosition);
        orgPosition = orgPositionRepository.save(orgPosition);
        orgPositionSearchRepository.index(orgPosition);
        return orgPosition;
    }

    @Override
    public OrgPosition update(OrgPosition orgPosition) {
        log.debug("Request to update OrgPosition : {}", orgPosition);
        orgPosition = orgPositionRepository.save(orgPosition);
        orgPositionSearchRepository.index(orgPosition);
        return orgPosition;
    }

    @Override
    public Optional<OrgPosition> partialUpdate(OrgPosition orgPosition) {
        log.debug("Request to partially update OrgPosition : {}", orgPosition);

        return orgPositionRepository
            .findById(orgPosition.getId())
            .map(existingOrgPosition -> {
                if (orgPosition.getTitle() != null) {
                    existingOrgPosition.setTitle(orgPosition.getTitle());
                }

                return existingOrgPosition;
            })
            .map(orgPositionRepository::save)
            .map(savedOrgPosition -> {
                orgPositionSearchRepository.index(savedOrgPosition);
                return savedOrgPosition;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgPosition> findAll(Pageable pageable) {
        log.debug("Request to get all OrgPositions");
        return orgPositionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrgPosition> findOne(UUID id) {
        log.debug("Request to get OrgPosition : {}", id);
        return orgPositionRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OrgPosition : {}", id);
        orgPositionRepository.deleteById(id);
        orgPositionSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgPosition> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrgPositions for query {}", query);
        return orgPositionSearchRepository.search(query, pageable);
    }
}
