package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.AccountingProcedure;
import com.bulut.attendance.repository.AccountingProcedureRepository;
import com.bulut.attendance.service.AccountingProcedureService;
import com.bulut.attendance.web.rest.errors.BadRequestAlertException;
import com.bulut.attendance.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.bulut.attendance.domain.AccountingProcedure}.
 */
@RestController
@RequestMapping("/api/accounting-procedures")
public class AccountingProcedureResource {

    private final Logger log = LoggerFactory.getLogger(AccountingProcedureResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceAccountingProcedure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountingProcedureService accountingProcedureService;

    private final AccountingProcedureRepository accountingProcedureRepository;

    public AccountingProcedureResource(
        AccountingProcedureService accountingProcedureService,
        AccountingProcedureRepository accountingProcedureRepository
    ) {
        this.accountingProcedureService = accountingProcedureService;
        this.accountingProcedureRepository = accountingProcedureRepository;
    }

    /**
     * {@code POST  /accounting-procedures} : Create a new accountingProcedure.
     *
     * @param accountingProcedure the accountingProcedure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountingProcedure, or with status {@code 400 (Bad Request)} if the accountingProcedure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountingProcedure> createAccountingProcedure(@RequestBody AccountingProcedure accountingProcedure)
        throws URISyntaxException {
        log.debug("REST request to save AccountingProcedure : {}", accountingProcedure);
        if (accountingProcedure.getId() != null) {
            throw new BadRequestAlertException("A new accountingProcedure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accountingProcedure = accountingProcedureService.save(accountingProcedure);
        return ResponseEntity.created(new URI("/api/accounting-procedures/" + accountingProcedure.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accountingProcedure.getId().toString()))
            .body(accountingProcedure);
    }

    /**
     * {@code PUT  /accounting-procedures/:id} : Updates an existing accountingProcedure.
     *
     * @param id the id of the accountingProcedure to save.
     * @param accountingProcedure the accountingProcedure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountingProcedure,
     * or with status {@code 400 (Bad Request)} if the accountingProcedure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountingProcedure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountingProcedure> updateAccountingProcedure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountingProcedure accountingProcedure
    ) throws URISyntaxException {
        log.debug("REST request to update AccountingProcedure : {}, {}", id, accountingProcedure);
        if (accountingProcedure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountingProcedure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountingProcedureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accountingProcedure = accountingProcedureService.update(accountingProcedure);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountingProcedure.getId().toString()))
            .body(accountingProcedure);
    }

    /**
     * {@code PATCH  /accounting-procedures/:id} : Partial updates given fields of an existing accountingProcedure, field will ignore if it is null
     *
     * @param id the id of the accountingProcedure to save.
     * @param accountingProcedure the accountingProcedure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountingProcedure,
     * or with status {@code 400 (Bad Request)} if the accountingProcedure is not valid,
     * or with status {@code 404 (Not Found)} if the accountingProcedure is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountingProcedure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountingProcedure> partialUpdateAccountingProcedure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountingProcedure accountingProcedure
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountingProcedure partially : {}, {}", id, accountingProcedure);
        if (accountingProcedure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountingProcedure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountingProcedureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountingProcedure> result = accountingProcedureService.partialUpdate(accountingProcedure);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountingProcedure.getId().toString())
        );
    }

    /**
     * {@code GET  /accounting-procedures} : get all the accountingProcedures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountingProcedures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccountingProcedure>> getAllAccountingProcedures(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AccountingProcedures");
        Page<AccountingProcedure> page = accountingProcedureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accounting-procedures/:id} : get the "id" accountingProcedure.
     *
     * @param id the id of the accountingProcedure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountingProcedure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountingProcedure> getAccountingProcedure(@PathVariable("id") Long id) {
        log.debug("REST request to get AccountingProcedure : {}", id);
        Optional<AccountingProcedure> accountingProcedure = accountingProcedureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountingProcedure);
    }

    /**
     * {@code DELETE  /accounting-procedures/:id} : delete the "id" accountingProcedure.
     *
     * @param id the id of the accountingProcedure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountingProcedure(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccountingProcedure : {}", id);
        accountingProcedureService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /accounting-procedures/_search?query=:query} : search for the accountingProcedure corresponding
     * to the query.
     *
     * @param query the query of the accountingProcedure search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccountingProcedure>> searchAccountingProcedures(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AccountingProcedures for query {}", query);
        try {
            Page<AccountingProcedure> page = accountingProcedureService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
