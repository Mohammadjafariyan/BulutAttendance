package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.SysConfig;
import com.bulut.attendance.repository.SysConfigRepository;
import com.bulut.attendance.repository.search.SysConfigSearchRepository;
import com.bulut.attendance.service.SysConfigService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.SysConfig}.
 */
@Service
@Transactional
public class SysConfigServiceImpl implements SysConfigService {

    private final Logger log = LoggerFactory.getLogger(SysConfigServiceImpl.class);

    private final SysConfigRepository sysConfigRepository;

    private final SysConfigSearchRepository sysConfigSearchRepository;

    public SysConfigServiceImpl(SysConfigRepository sysConfigRepository, SysConfigSearchRepository sysConfigSearchRepository) {
        this.sysConfigRepository = sysConfigRepository;
        this.sysConfigSearchRepository = sysConfigSearchRepository;
    }

    @Override
    public SysConfig save(SysConfig sysConfig) {
        log.debug("Request to save SysConfig : {}", sysConfig);
        sysConfig = sysConfigRepository.save(sysConfig);
        sysConfigSearchRepository.index(sysConfig);
        return sysConfig;
    }

    @Override
    public SysConfig update(SysConfig sysConfig) {
        log.debug("Request to update SysConfig : {}", sysConfig);
        sysConfig = sysConfigRepository.save(sysConfig);
        sysConfigSearchRepository.index(sysConfig);
        return sysConfig;
    }

    @Override
    public Optional<SysConfig> partialUpdate(SysConfig sysConfig) {
        log.debug("Request to partially update SysConfig : {}", sysConfig);

        return sysConfigRepository
            .findById(sysConfig.getId())
            .map(existingSysConfig -> {
                if (sysConfig.getTaxFormula() != null) {
                    existingSysConfig.setTaxFormula(sysConfig.getTaxFormula());
                }
                if (sysConfig.getSanavatFormula() != null) {
                    existingSysConfig.setSanavatFormula(sysConfig.getSanavatFormula());
                }
                if (sysConfig.getYear() != null) {
                    existingSysConfig.setYear(sysConfig.getYear());
                }

                return existingSysConfig;
            })
            .map(sysConfigRepository::save)
            .map(savedSysConfig -> {
                sysConfigSearchRepository.index(savedSysConfig);
                return savedSysConfig;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SysConfig> findAll(Pageable pageable) {
        log.debug("Request to get all SysConfigs");
        return sysConfigRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SysConfig> findOne(UUID id) {
        log.debug("Request to get SysConfig : {}", id);
        return sysConfigRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SysConfig : {}", id);
        sysConfigRepository.deleteById(id);
        sysConfigSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SysConfig> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SysConfigs for query {}", query);
        return sysConfigSearchRepository.search(query, pageable);
    }
}
