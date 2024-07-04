package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.Company;
import com.bulut.attendance.repository.CompanyRepository;
import com.bulut.attendance.repository.search.CompanySearchRepository;
import com.bulut.attendance.service.CompanyService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.Company}.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    private final CompanySearchRepository companySearchRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanySearchRepository companySearchRepository) {
        this.companyRepository = companyRepository;
        this.companySearchRepository = companySearchRepository;
    }

    @Override
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        company = companyRepository.save(company);
        companySearchRepository.index(company);
        return company;
    }

    @Override
    public Company update(Company company) {
        log.debug("Request to update Company : {}", company);
        company = companyRepository.save(company);
        companySearchRepository.index(company);
        return company;
    }

    @Override
    public Optional<Company> partialUpdate(Company company) {
        log.debug("Request to partially update Company : {}", company);

        return companyRepository
            .findById(company.getId())
            .map(existingCompany -> {
                if (company.getTitle() != null) {
                    existingCompany.setTitle(company.getTitle());
                }
                if (company.getLogo() != null) {
                    existingCompany.setLogo(company.getLogo());
                }

                return existingCompany;
            })
            .map(companyRepository::save)
            .map(savedCompany -> {
                companySearchRepository.index(savedCompany);
                return savedCompany;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> findAll() {
        log.debug("Request to get all Companies");
        return companyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
        companySearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> search(String query) {
        log.debug("Request to search Companies for query {}", query);
        try {
            return StreamSupport.stream(companySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
