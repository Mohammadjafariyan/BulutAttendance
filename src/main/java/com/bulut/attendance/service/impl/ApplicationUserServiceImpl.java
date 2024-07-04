package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.ApplicationUser;
import com.bulut.attendance.repository.ApplicationUserRepository;
import com.bulut.attendance.repository.search.ApplicationUserSearchRepository;
import com.bulut.attendance.service.ApplicationUserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserServiceImpl.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserSearchRepository applicationUserSearchRepository;

    public ApplicationUserServiceImpl(
        ApplicationUserRepository applicationUserRepository,
        ApplicationUserSearchRepository applicationUserSearchRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserSearchRepository = applicationUserSearchRepository;
    }

    @Override
    public ApplicationUser save(ApplicationUser applicationUser) {
        log.debug("Request to save ApplicationUser : {}", applicationUser);
        applicationUser = applicationUserRepository.save(applicationUser);
        applicationUserSearchRepository.index(applicationUser);
        return applicationUser;
    }

    @Override
    public ApplicationUser update(ApplicationUser applicationUser) {
        log.debug("Request to update ApplicationUser : {}", applicationUser);
        applicationUser = applicationUserRepository.save(applicationUser);
        applicationUserSearchRepository.index(applicationUser);
        return applicationUser;
    }

    @Override
    public Optional<ApplicationUser> partialUpdate(ApplicationUser applicationUser) {
        log.debug("Request to partially update ApplicationUser : {}", applicationUser);

        return applicationUserRepository
            .findById(applicationUser.getId())
            .map(existingApplicationUser -> {
                if (applicationUser.getUserId() != null) {
                    existingApplicationUser.setUserId(applicationUser.getUserId());
                }

                return existingApplicationUser;
            })
            .map(applicationUserRepository::save)
            .map(savedApplicationUser -> {
                applicationUserSearchRepository.index(savedApplicationUser);
                return savedApplicationUser;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationUser> findAll() {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationUser> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
        applicationUserSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationUser> search(String query) {
        log.debug("Request to search ApplicationUsers for query {}", query);
        try {
            return StreamSupport.stream(applicationUserSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
