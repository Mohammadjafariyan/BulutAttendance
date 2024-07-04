package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.HrLetterType;
import com.bulut.attendance.repository.HrLetterTypeRepository;
import com.bulut.attendance.repository.search.HrLetterTypeSearchRepository;
import com.bulut.attendance.service.HrLetterTypeService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.HrLetterType}.
 */
@Service
@Transactional
public class HrLetterTypeServiceImpl implements HrLetterTypeService {

    private final Logger log = LoggerFactory.getLogger(HrLetterTypeServiceImpl.class);

    private final HrLetterTypeRepository hrLetterTypeRepository;

    private final HrLetterTypeSearchRepository hrLetterTypeSearchRepository;

    public HrLetterTypeServiceImpl(
        HrLetterTypeRepository hrLetterTypeRepository,
        HrLetterTypeSearchRepository hrLetterTypeSearchRepository
    ) {
        this.hrLetterTypeRepository = hrLetterTypeRepository;
        this.hrLetterTypeSearchRepository = hrLetterTypeSearchRepository;
    }

    @Override
    public HrLetterType save(HrLetterType hrLetterType) {
        log.debug("Request to save HrLetterType : {}", hrLetterType);
        hrLetterType = hrLetterTypeRepository.save(hrLetterType);
        hrLetterTypeSearchRepository.index(hrLetterType);
        return hrLetterType;
    }

    @Override
    public HrLetterType update(HrLetterType hrLetterType) {
        log.debug("Request to update HrLetterType : {}", hrLetterType);
        hrLetterType = hrLetterTypeRepository.save(hrLetterType);
        hrLetterTypeSearchRepository.index(hrLetterType);
        return hrLetterType;
    }

    @Override
    public Optional<HrLetterType> partialUpdate(HrLetterType hrLetterType) {
        log.debug("Request to partially update HrLetterType : {}", hrLetterType);

        return hrLetterTypeRepository
            .findById(hrLetterType.getId())
            .map(existingHrLetterType -> {
                if (hrLetterType.getTitle() != null) {
                    existingHrLetterType.setTitle(hrLetterType.getTitle());
                }

                return existingHrLetterType;
            })
            .map(hrLetterTypeRepository::save)
            .map(savedHrLetterType -> {
                hrLetterTypeSearchRepository.index(savedHrLetterType);
                return savedHrLetterType;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HrLetterType> findAll(Pageable pageable) {
        log.debug("Request to get all HrLetterTypes");
        return hrLetterTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HrLetterType> findOne(UUID id) {
        log.debug("Request to get HrLetterType : {}", id);
        return hrLetterTypeRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete HrLetterType : {}", id);
        hrLetterTypeRepository.deleteById(id);
        hrLetterTypeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HrLetterType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HrLetterTypes for query {}", query);
        return hrLetterTypeSearchRepository.search(query, pageable);
    }
}
