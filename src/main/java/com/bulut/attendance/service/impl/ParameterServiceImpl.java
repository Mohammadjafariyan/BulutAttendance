package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.Parameter;
import com.bulut.attendance.repository.ParameterRepository;
import com.bulut.attendance.repository.search.ParameterSearchRepository;
import com.bulut.attendance.service.ParameterService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.Parameter}.
 */
@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    private final Logger log = LoggerFactory.getLogger(ParameterServiceImpl.class);

    private final ParameterRepository parameterRepository;

    private final ParameterSearchRepository parameterSearchRepository;

    public ParameterServiceImpl(ParameterRepository parameterRepository, ParameterSearchRepository parameterSearchRepository) {
        this.parameterRepository = parameterRepository;
        this.parameterSearchRepository = parameterSearchRepository;
    }

    @Override
    public Parameter save(Parameter parameter) {
        log.debug("Request to save Parameter : {}", parameter);
        parameter = parameterRepository.save(parameter);
        parameterSearchRepository.index(parameter);
        return parameter;
    }

    @Override
    public Parameter update(Parameter parameter) {
        log.debug("Request to update Parameter : {}", parameter);
        parameter = parameterRepository.save(parameter);
        parameterSearchRepository.index(parameter);
        return parameter;
    }

    @Override
    public Optional<Parameter> partialUpdate(Parameter parameter) {
        log.debug("Request to partially update Parameter : {}", parameter);

        return parameterRepository
            .findById(parameter.getId())
            .map(existingParameter -> {
                if (parameter.getTitle() != null) {
                    existingParameter.setTitle(parameter.getTitle());
                }
                if (parameter.getManualOrAuto() != null) {
                    existingParameter.setManualOrAuto(parameter.getManualOrAuto());
                }
                if (parameter.getFormula() != null) {
                    existingParameter.setFormula(parameter.getFormula());
                }
                if (parameter.getUnit() != null) {
                    existingParameter.setUnit(parameter.getUnit());
                }
                if (parameter.getIsDeducTax() != null) {
                    existingParameter.setIsDeducTax(parameter.getIsDeducTax());
                }
                if (parameter.getIsDeducInsurance() != null) {
                    existingParameter.setIsDeducInsurance(parameter.getIsDeducInsurance());
                }
                if (parameter.getLaborTime() != null) {
                    existingParameter.setLaborTime(parameter.getLaborTime());
                }
                if (parameter.getHokm() != null) {
                    existingParameter.setHokm(parameter.getHokm());
                }
                if (parameter.getEarnings() != null) {
                    existingParameter.setEarnings(parameter.getEarnings());
                }
                if (parameter.getDeduction() != null) {
                    existingParameter.setDeduction(parameter.getDeduction());
                }
                if (parameter.getOther() != null) {
                    existingParameter.setOther(parameter.getOther());
                }

                return existingParameter;
            })
            .map(parameterRepository::save)
            .map(savedParameter -> {
                parameterSearchRepository.index(savedParameter);
                return savedParameter;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parameter> findAll(Pageable pageable) {
        log.debug("Request to get all Parameters");
        return parameterRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Parameter> findOne(Long id) {
        log.debug("Request to get Parameter : {}", id);
        return parameterRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Parameter : {}", id);
        parameterRepository.deleteById(id);
        parameterSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parameter> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Parameters for query {}", query);
        return parameterSearchRepository.search(query, pageable);
    }
}
