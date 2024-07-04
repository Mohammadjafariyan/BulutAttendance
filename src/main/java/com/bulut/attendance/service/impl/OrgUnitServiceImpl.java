package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.OrgUnit;
import com.bulut.attendance.repository.OrgUnitRepository;
import com.bulut.attendance.repository.search.OrgUnitSearchRepository;
import com.bulut.attendance.service.OrgUnitService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.OrgUnit}.
 */
@Service
@Transactional
public class OrgUnitServiceImpl implements OrgUnitService {

    private final Logger log = LoggerFactory.getLogger(OrgUnitServiceImpl.class);

    private final OrgUnitRepository orgUnitRepository;

    private final OrgUnitSearchRepository orgUnitSearchRepository;

    public OrgUnitServiceImpl(OrgUnitRepository orgUnitRepository, OrgUnitSearchRepository orgUnitSearchRepository) {
        this.orgUnitRepository = orgUnitRepository;
        this.orgUnitSearchRepository = orgUnitSearchRepository;
    }

    @Override
    public OrgUnit save(OrgUnit orgUnit) {
        log.debug("Request to save OrgUnit : {}", orgUnit);
        orgUnit = orgUnitRepository.save(orgUnit);
        orgUnitSearchRepository.index(orgUnit);
        return orgUnit;
    }

    @Override
    public OrgUnit update(OrgUnit orgUnit) {
        log.debug("Request to update OrgUnit : {}", orgUnit);
        orgUnit = orgUnitRepository.save(orgUnit);
        orgUnitSearchRepository.index(orgUnit);
        return orgUnit;
    }

    @Override
    public Optional<OrgUnit> partialUpdate(OrgUnit orgUnit) {
        log.debug("Request to partially update OrgUnit : {}", orgUnit);

        return orgUnitRepository
            .findById(orgUnit.getId())
            .map(existingOrgUnit -> {
                if (orgUnit.getTitle() != null) {
                    existingOrgUnit.setTitle(orgUnit.getTitle());
                }

                return existingOrgUnit;
            })
            .map(orgUnitRepository::save)
            .map(savedOrgUnit -> {
                orgUnitSearchRepository.index(savedOrgUnit);
                return savedOrgUnit;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgUnit> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUnits");
        return orgUnitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrgUnit> findOne(UUID id) {
        log.debug("Request to get OrgUnit : {}", id);
        return orgUnitRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OrgUnit : {}", id);
        orgUnitRepository.deleteById(id);
        orgUnitSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgUnit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrgUnits for query {}", query);
        return orgUnitSearchRepository.search(query, pageable);
    }
}
