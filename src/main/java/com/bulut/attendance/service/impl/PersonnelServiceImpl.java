package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.Personnel;
import com.bulut.attendance.repository.PersonnelRepository;
import com.bulut.attendance.repository.search.PersonnelSearchRepository;
import com.bulut.attendance.service.PersonnelService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.Personnel}.
 */
@Service
@Transactional
public class PersonnelServiceImpl implements PersonnelService {

    private final Logger log = LoggerFactory.getLogger(PersonnelServiceImpl.class);

    private final PersonnelRepository personnelRepository;

    private final PersonnelSearchRepository personnelSearchRepository;

    public PersonnelServiceImpl(PersonnelRepository personnelRepository, PersonnelSearchRepository personnelSearchRepository) {
        this.personnelRepository = personnelRepository;
        this.personnelSearchRepository = personnelSearchRepository;
    }

    @Override
    public Personnel save(Personnel personnel) {
        log.debug("Request to save Personnel : {}", personnel);
        personnel = personnelRepository.save(personnel);
        personnelSearchRepository.index(personnel);
        return personnel;
    }

    @Override
    public Personnel update(Personnel personnel) {
        log.debug("Request to update Personnel : {}", personnel);
        personnel = personnelRepository.save(personnel);
        personnelSearchRepository.index(personnel);
        return personnel;
    }

    @Override
    public Optional<Personnel> partialUpdate(Personnel personnel) {
        log.debug("Request to partially update Personnel : {}", personnel);

        return personnelRepository
            .findById(personnel.getId())
            .map(existingPersonnel -> {
                if (personnel.getFirstName() != null) {
                    existingPersonnel.setFirstName(personnel.getFirstName());
                }
                if (personnel.getLastName() != null) {
                    existingPersonnel.setLastName(personnel.getLastName());
                }
                if (personnel.getRequitmentDate() != null) {
                    existingPersonnel.setRequitmentDate(personnel.getRequitmentDate());
                }
                if (personnel.getFather() != null) {
                    existingPersonnel.setFather(personnel.getFather());
                }
                if (personnel.getShenasname() != null) {
                    existingPersonnel.setShenasname(personnel.getShenasname());
                }
                if (personnel.getMahalesodur() != null) {
                    existingPersonnel.setMahalesodur(personnel.getMahalesodur());
                }
                if (personnel.getBirthday() != null) {
                    existingPersonnel.setBirthday(personnel.getBirthday());
                }
                if (personnel.getIsSingle() != null) {
                    existingPersonnel.setIsSingle(personnel.getIsSingle());
                }
                if (personnel.getLastEducation() != null) {
                    existingPersonnel.setLastEducation(personnel.getLastEducation());
                }
                if (personnel.getEducationField() != null) {
                    existingPersonnel.setEducationField(personnel.getEducationField());
                }
                if (personnel.getChildren() != null) {
                    existingPersonnel.setChildren(personnel.getChildren());
                }

                return existingPersonnel;
            })
            .map(personnelRepository::save)
            .map(savedPersonnel -> {
                personnelSearchRepository.index(savedPersonnel);
                return savedPersonnel;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Personnel> findAll(Pageable pageable) {
        log.debug("Request to get all Personnel");
        return personnelRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Personnel> findOne(UUID id) {
        log.debug("Request to get Personnel : {}", id);
        return personnelRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Personnel : {}", id);
        personnelRepository.deleteById(id);
        personnelSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Personnel> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Personnel for query {}", query);
        return personnelSearchRepository.search(query, pageable);
    }
}
