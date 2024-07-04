package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.AccountHesab;
import com.bulut.attendance.repository.AccountHesabRepository;
import com.bulut.attendance.service.AccountHesabService;
import com.bulut.attendance.web.rest.errors.BadRequestAlertException;
import com.bulut.attendance.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bulut.attendance.domain.AccountHesab}.
 */
@RestController
@RequestMapping("/api/account-hesabs")
public class AccountHesabResource {

    private final Logger log = LoggerFactory.getLogger(AccountHesabResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceAccountHesab";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountHesabService accountHesabService;

    private final AccountHesabRepository accountHesabRepository;

    public AccountHesabResource(AccountHesabService accountHesabService, AccountHesabRepository accountHesabRepository) {
        this.accountHesabService = accountHesabService;
        this.accountHesabRepository = accountHesabRepository;
    }

    /**
     * {@code POST  /account-hesabs} : Create a new accountHesab.
     *
     * @param accountHesab the accountHesab to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountHesab, or with status {@code 400 (Bad Request)} if the accountHesab has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountHesab> createAccountHesab(@RequestBody AccountHesab accountHesab) throws URISyntaxException {
        log.debug("REST request to save AccountHesab : {}", accountHesab);
        if (accountHesab.getId() != null) {
            throw new BadRequestAlertException("A new accountHesab cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accountHesab = accountHesabService.save(accountHesab);
        return ResponseEntity.created(new URI("/api/account-hesabs/" + accountHesab.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accountHesab.getId().toString()))
            .body(accountHesab);
    }

    /**
     * {@code PUT  /account-hesabs/:id} : Updates an existing accountHesab.
     *
     * @param id the id of the accountHesab to save.
     * @param accountHesab the accountHesab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountHesab,
     * or with status {@code 400 (Bad Request)} if the accountHesab is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountHesab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountHesab> updateAccountHesab(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AccountHesab accountHesab
    ) throws URISyntaxException {
        log.debug("REST request to update AccountHesab : {}, {}", id, accountHesab);
        if (accountHesab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountHesab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountHesabRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accountHesab = accountHesabService.update(accountHesab);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountHesab.getId().toString()))
            .body(accountHesab);
    }

    /**
     * {@code PATCH  /account-hesabs/:id} : Partial updates given fields of an existing accountHesab, field will ignore if it is null
     *
     * @param id the id of the accountHesab to save.
     * @param accountHesab the accountHesab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountHesab,
     * or with status {@code 400 (Bad Request)} if the accountHesab is not valid,
     * or with status {@code 404 (Not Found)} if the accountHesab is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountHesab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountHesab> partialUpdateAccountHesab(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AccountHesab accountHesab
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountHesab partially : {}, {}", id, accountHesab);
        if (accountHesab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountHesab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountHesabRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountHesab> result = accountHesabService.partialUpdate(accountHesab);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountHesab.getId().toString())
        );
    }

    /**
     * {@code GET  /account-hesabs} : get all the accountHesabs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountHesabs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccountHesab>> getAllAccountHesabs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AccountHesabs");
        Page<AccountHesab> page = accountHesabService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-hesabs/:id} : get the "id" accountHesab.
     *
     * @param id the id of the accountHesab to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountHesab, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountHesab> getAccountHesab(@PathVariable("id") UUID id) {
        log.debug("REST request to get AccountHesab : {}", id);
        Optional<AccountHesab> accountHesab = accountHesabService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountHesab);
    }

    /**
     * {@code DELETE  /account-hesabs/:id} : delete the "id" accountHesab.
     *
     * @param id the id of the accountHesab to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountHesab(@PathVariable("id") UUID id) {
        log.debug("REST request to delete AccountHesab : {}", id);
        accountHesabService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /account-hesabs/_search?query=:query} : search for the accountHesab corresponding
     * to the query.
     *
     * @param query the query of the accountHesab search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccountHesab>> searchAccountHesabs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AccountHesabs for query {}", query);
        try {
            Page<AccountHesab> page = accountHesabService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
