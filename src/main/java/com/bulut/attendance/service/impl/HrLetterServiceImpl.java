package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.HrLetter;
import com.bulut.attendance.repository.HrLetterRepository;
import com.bulut.attendance.repository.search.HrLetterSearchRepository;
import com.bulut.attendance.service.HrLetterService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.HrLetter}.
 */
@Service
@Transactional
public class HrLetterServiceImpl implements HrLetterService {

    private final Logger log = LoggerFactory.getLogger(HrLetterServiceImpl.class);

    private final HrLetterRepository hrLetterRepository;

    private final HrLetterSearchRepository hrLetterSearchRepository;

    public HrLetterServiceImpl(HrLetterRepository hrLetterRepository, HrLetterSearchRepository hrLetterSearchRepository) {
        this.hrLetterRepository = hrLetterRepository;
        this.hrLetterSearchRepository = hrLetterSearchRepository;
    }

    @Override
    public HrLetter save(HrLetter hrLetter) {
        log.debug("Request to save HrLetter : {}", hrLetter);
        hrLetter = hrLetterRepository.save(hrLetter);
        hrLetterSearchRepository.index(hrLetter);
        return hrLetter;
    }

    @Override
    public HrLetter update(HrLetter hrLetter) {
        log.debug("Request to update HrLetter : {}", hrLetter);
        hrLetter = hrLetterRepository.save(hrLetter);
        hrLetterSearchRepository.index(hrLetter);
        return hrLetter;
    }

    @Override
    public Optional<HrLetter> partialUpdate(HrLetter hrLetter) {
        log.debug("Request to partially update HrLetter : {}", hrLetter);

        return hrLetterRepository
            .findById(hrLetter.getId())
            .map(existingHrLetter -> {
                if (hrLetter.getTitle() != null) {
                    existingHrLetter.setTitle(hrLetter.getTitle());
                }
                if (hrLetter.getUniqueNumber() != null) {
                    existingHrLetter.setUniqueNumber(hrLetter.getUniqueNumber());
                }
                if (hrLetter.getIssueDate() != null) {
                    existingHrLetter.setIssueDate(hrLetter.getIssueDate());
                }
                if (hrLetter.getExecutionDate() != null) {
                    existingHrLetter.setExecutionDate(hrLetter.getExecutionDate());
                }
                if (hrLetter.getBpmsApproveStatus() != null) {
                    existingHrLetter.setBpmsApproveStatus(hrLetter.getBpmsApproveStatus());
                }

                return existingHrLetter;
            })
            .map(hrLetterRepository::save)
            .map(savedHrLetter -> {
                hrLetterSearchRepository.index(savedHrLetter);
                return savedHrLetter;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HrLetter> findAll(Pageable pageable) {
        log.debug("Request to get all HrLetters");
        return hrLetterRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HrLetter> findOne(UUID id) {
        log.debug("Request to get HrLetter : {}", id);
        return hrLetterRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete HrLetter : {}", id);
        hrLetterRepository.deleteById(id);
        hrLetterSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HrLetter> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HrLetters for query {}", query);
        return hrLetterSearchRepository.search(query, pageable);
    }
}
