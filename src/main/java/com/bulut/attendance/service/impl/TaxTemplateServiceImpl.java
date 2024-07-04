package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.TaxTemplate;
import com.bulut.attendance.repository.TaxTemplateRepository;
import com.bulut.attendance.repository.search.TaxTemplateSearchRepository;
import com.bulut.attendance.service.TaxTemplateService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.TaxTemplate}.
 */
@Service
@Transactional
public class TaxTemplateServiceImpl implements TaxTemplateService {

    private final Logger log = LoggerFactory.getLogger(TaxTemplateServiceImpl.class);

    private final TaxTemplateRepository taxTemplateRepository;

    private final TaxTemplateSearchRepository taxTemplateSearchRepository;

    public TaxTemplateServiceImpl(TaxTemplateRepository taxTemplateRepository, TaxTemplateSearchRepository taxTemplateSearchRepository) {
        this.taxTemplateRepository = taxTemplateRepository;
        this.taxTemplateSearchRepository = taxTemplateSearchRepository;
    }

    @Override
    public TaxTemplate save(TaxTemplate taxTemplate) {
        log.debug("Request to save TaxTemplate : {}", taxTemplate);
        taxTemplate = taxTemplateRepository.save(taxTemplate);
        taxTemplateSearchRepository.index(taxTemplate);
        return taxTemplate;
    }

    @Override
    public TaxTemplate update(TaxTemplate taxTemplate) {
        log.debug("Request to update TaxTemplate : {}", taxTemplate);
        taxTemplate = taxTemplateRepository.save(taxTemplate);
        taxTemplateSearchRepository.index(taxTemplate);
        return taxTemplate;
    }

    @Override
    public Optional<TaxTemplate> partialUpdate(TaxTemplate taxTemplate) {
        log.debug("Request to partially update TaxTemplate : {}", taxTemplate);

        return taxTemplateRepository
            .findById(taxTemplate.getId())
            .map(existingTaxTemplate -> {
                if (taxTemplate.getRangeFrom() != null) {
                    existingTaxTemplate.setRangeFrom(taxTemplate.getRangeFrom());
                }
                if (taxTemplate.getRangeTo() != null) {
                    existingTaxTemplate.setRangeTo(taxTemplate.getRangeTo());
                }
                if (taxTemplate.getPercent() != null) {
                    existingTaxTemplate.setPercent(taxTemplate.getPercent());
                }
                if (taxTemplate.getYear() != null) {
                    existingTaxTemplate.setYear(taxTemplate.getYear());
                }

                return existingTaxTemplate;
            })
            .map(taxTemplateRepository::save)
            .map(savedTaxTemplate -> {
                taxTemplateSearchRepository.index(savedTaxTemplate);
                return savedTaxTemplate;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxTemplate> findAll(Pageable pageable) {
        log.debug("Request to get all TaxTemplates");
        return taxTemplateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaxTemplate> findOne(UUID id) {
        log.debug("Request to get TaxTemplate : {}", id);
        return taxTemplateRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TaxTemplate : {}", id);
        taxTemplateRepository.deleteById(id);
        taxTemplateSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxTemplate> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaxTemplates for query {}", query);
        return taxTemplateSearchRepository.search(query, pageable);
    }
}
