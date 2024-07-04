package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.AccProccParameter;
import com.bulut.attendance.repository.AccProccParameterRepository;
import com.bulut.attendance.repository.search.AccProccParameterSearchRepository;
import com.bulut.attendance.service.AccProccParameterService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.AccProccParameter}.
 */
@Service
@Transactional
public class AccProccParameterServiceImpl implements AccProccParameterService {

    private final Logger log = LoggerFactory.getLogger(AccProccParameterServiceImpl.class);

    private final AccProccParameterRepository accProccParameterRepository;

    private final AccProccParameterSearchRepository accProccParameterSearchRepository;

    public AccProccParameterServiceImpl(
        AccProccParameterRepository accProccParameterRepository,
        AccProccParameterSearchRepository accProccParameterSearchRepository
    ) {
        this.accProccParameterRepository = accProccParameterRepository;
        this.accProccParameterSearchRepository = accProccParameterSearchRepository;
    }

    @Override
    public AccProccParameter save(AccProccParameter accProccParameter) {
        log.debug("Request to save AccProccParameter : {}", accProccParameter);
        accProccParameter = accProccParameterRepository.save(accProccParameter);
        accProccParameterSearchRepository.index(accProccParameter);
        return accProccParameter;
    }

    @Override
    public AccProccParameter update(AccProccParameter accProccParameter) {
        log.debug("Request to update AccProccParameter : {}", accProccParameter);
        accProccParameter = accProccParameterRepository.save(accProccParameter);
        accProccParameterSearchRepository.index(accProccParameter);
        return accProccParameter;
    }

    @Override
    public Optional<AccProccParameter> partialUpdate(AccProccParameter accProccParameter) {
        log.debug("Request to partially update AccProccParameter : {}", accProccParameter);

        return accProccParameterRepository
            .findById(accProccParameter.getId())
            .map(existingAccProccParameter -> {
                if (accProccParameter.getTitle() != null) {
                    existingAccProccParameter.setTitle(accProccParameter.getTitle());
                }
                if (accProccParameter.getManualOrAuto() != null) {
                    existingAccProccParameter.setManualOrAuto(accProccParameter.getManualOrAuto());
                }
                if (accProccParameter.getFormula() != null) {
                    existingAccProccParameter.setFormula(accProccParameter.getFormula());
                }
                if (accProccParameter.getUnit() != null) {
                    existingAccProccParameter.setUnit(accProccParameter.getUnit());
                }
                if (accProccParameter.getIsDeducTax() != null) {
                    existingAccProccParameter.setIsDeducTax(accProccParameter.getIsDeducTax());
                }
                if (accProccParameter.getIsDeducInsurance() != null) {
                    existingAccProccParameter.setIsDeducInsurance(accProccParameter.getIsDeducInsurance());
                }
                if (accProccParameter.getLaborTime() != null) {
                    existingAccProccParameter.setLaborTime(accProccParameter.getLaborTime());
                }
                if (accProccParameter.getHokm() != null) {
                    existingAccProccParameter.setHokm(accProccParameter.getHokm());
                }
                if (accProccParameter.getEarnings() != null) {
                    existingAccProccParameter.setEarnings(accProccParameter.getEarnings());
                }
                if (accProccParameter.getDeduction() != null) {
                    existingAccProccParameter.setDeduction(accProccParameter.getDeduction());
                }
                if (accProccParameter.getOther() != null) {
                    existingAccProccParameter.setOther(accProccParameter.getOther());
                }

                return existingAccProccParameter;
            })
            .map(accProccParameterRepository::save)
            .map(savedAccProccParameter -> {
                accProccParameterSearchRepository.index(savedAccProccParameter);
                return savedAccProccParameter;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccProccParameter> findAll(Pageable pageable) {
        log.debug("Request to get all AccProccParameters");
        return accProccParameterRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccProccParameter> findOne(Long id) {
        log.debug("Request to get AccProccParameter : {}", id);
        return accProccParameterRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccProccParameter : {}", id);
        accProccParameterRepository.deleteById(id);
        accProccParameterSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccProccParameter> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccProccParameters for query {}", query);
        return accProccParameterSearchRepository.search(query, pageable);
    }
}
