package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.HrLetterParameter;
import com.bulut.attendance.repository.HrLetterParameterRepository;
import com.bulut.attendance.repository.search.HrLetterParameterSearchRepository;
import com.bulut.attendance.service.HrLetterParameterService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.HrLetterParameter}.
 */
@Service
@Transactional
public class HrLetterParameterServiceImpl implements HrLetterParameterService {

    private final Logger log = LoggerFactory.getLogger(HrLetterParameterServiceImpl.class);

    private final HrLetterParameterRepository hrLetterParameterRepository;

    private final HrLetterParameterSearchRepository hrLetterParameterSearchRepository;

    public HrLetterParameterServiceImpl(
        HrLetterParameterRepository hrLetterParameterRepository,
        HrLetterParameterSearchRepository hrLetterParameterSearchRepository
    ) {
        this.hrLetterParameterRepository = hrLetterParameterRepository;
        this.hrLetterParameterSearchRepository = hrLetterParameterSearchRepository;
    }

    @Override
    public HrLetterParameter save(HrLetterParameter hrLetterParameter) {
        log.debug("Request to save HrLetterParameter : {}", hrLetterParameter);
        hrLetterParameter = hrLetterParameterRepository.save(hrLetterParameter);
        hrLetterParameterSearchRepository.index(hrLetterParameter);
        return hrLetterParameter;
    }

    @Override
    public HrLetterParameter update(HrLetterParameter hrLetterParameter) {
        log.debug("Request to update HrLetterParameter : {}", hrLetterParameter);
        hrLetterParameter = hrLetterParameterRepository.save(hrLetterParameter);
        hrLetterParameterSearchRepository.index(hrLetterParameter);
        return hrLetterParameter;
    }

    @Override
    public Optional<HrLetterParameter> partialUpdate(HrLetterParameter hrLetterParameter) {
        log.debug("Request to partially update HrLetterParameter : {}", hrLetterParameter);

        return hrLetterParameterRepository
            .findById(hrLetterParameter.getId())
            .map(existingHrLetterParameter -> {
                if (hrLetterParameter.getTitle() != null) {
                    existingHrLetterParameter.setTitle(hrLetterParameter.getTitle());
                }
                if (hrLetterParameter.getManualOrAuto() != null) {
                    existingHrLetterParameter.setManualOrAuto(hrLetterParameter.getManualOrAuto());
                }
                if (hrLetterParameter.getFormula() != null) {
                    existingHrLetterParameter.setFormula(hrLetterParameter.getFormula());
                }
                if (hrLetterParameter.getUnit() != null) {
                    existingHrLetterParameter.setUnit(hrLetterParameter.getUnit());
                }
                if (hrLetterParameter.getIsDeducTax() != null) {
                    existingHrLetterParameter.setIsDeducTax(hrLetterParameter.getIsDeducTax());
                }
                if (hrLetterParameter.getIsDeducInsurance() != null) {
                    existingHrLetterParameter.setIsDeducInsurance(hrLetterParameter.getIsDeducInsurance());
                }
                if (hrLetterParameter.getLaborTime() != null) {
                    existingHrLetterParameter.setLaborTime(hrLetterParameter.getLaborTime());
                }
                if (hrLetterParameter.getHokm() != null) {
                    existingHrLetterParameter.setHokm(hrLetterParameter.getHokm());
                }
                if (hrLetterParameter.getEarnings() != null) {
                    existingHrLetterParameter.setEarnings(hrLetterParameter.getEarnings());
                }
                if (hrLetterParameter.getDeduction() != null) {
                    existingHrLetterParameter.setDeduction(hrLetterParameter.getDeduction());
                }
                if (hrLetterParameter.getOther() != null) {
                    existingHrLetterParameter.setOther(hrLetterParameter.getOther());
                }
                if (hrLetterParameter.getIsEnabled() != null) {
                    existingHrLetterParameter.setIsEnabled(hrLetterParameter.getIsEnabled());
                }

                return existingHrLetterParameter;
            })
            .map(hrLetterParameterRepository::save)
            .map(savedHrLetterParameter -> {
                hrLetterParameterSearchRepository.index(savedHrLetterParameter);
                return savedHrLetterParameter;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<HrLetterParameter> findAll() {
        log.debug("Request to get all HrLetterParameters");
        return hrLetterParameterRepository.findAll();
    }

    /**
     *  Get all the hrLetterParameters where WorkItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HrLetterParameter> findAllWhereWorkItemIsNull() {
        log.debug("Request to get all hrLetterParameters where WorkItem is null");
        return StreamSupport.stream(hrLetterParameterRepository.findAll().spliterator(), false)
            .filter(hrLetterParameter -> hrLetterParameter.getWorkItem() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HrLetterParameter> findOne(Long id) {
        log.debug("Request to get HrLetterParameter : {}", id);
        return hrLetterParameterRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HrLetterParameter : {}", id);
        hrLetterParameterRepository.deleteById(id);
        hrLetterParameterSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HrLetterParameter> search(String query) {
        log.debug("Request to search HrLetterParameters for query {}", query);
        try {
            return StreamSupport.stream(hrLetterParameterSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
