package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.AccountingProcedureExecution;
import com.bulut.attendance.repository.AccountingProcedureExecutionRepository;
import com.bulut.attendance.service.AccountingProcedureExecutionService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.AccountingProcedureExecution}.
 */
@RestController
@RequestMapping("/api/accounting-procedure-executions")
public class AccountingProcedureExecutionResource {

    private final Logger log = LoggerFactory.getLogger(AccountingProcedureExecutionResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceAccountingProcedureExecution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountingProcedureExecutionService accountingProcedureExecutionService;

    private final AccountingProcedureExecutionRepository accountingProcedureExecutionRepository;

    public AccountingProcedureExecutionResource(
        AccountingProcedureExecutionService accountingProcedureExecutionService,
        AccountingProcedureExecutionRepository accountingProcedureExecutionRepository
    ) {
        this.accountingProcedureExecutionService = accountingProcedureExecutionService;
        this.accountingProcedureExecutionRepository = accountingProcedureExecutionRepository;
    }

    /**
     * {@code POST  /accounting-procedure-executions} : Create a new accountingProcedureExecution.
     *
     * @param accountingProcedureExecution the accountingProcedureExecution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountingProcedureExecution, or with status {@code 400 (Bad Request)} if the accountingProcedureExecution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountingProcedureExecution> createAccountingProcedureExecution(
        @RequestBody AccountingProcedureExecution accountingProcedureExecution
    ) throws URISyntaxException {
        log.debug("REST request to save AccountingProcedureExecution : {}", accountingProcedureExecution);
        if (accountingProcedureExecution.getId() != null) {
            throw new BadRequestAlertException("A new accountingProcedureExecution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accountingProcedureExecution = accountingProcedureExecutionService.save(accountingProcedureExecution);
        return ResponseEntity.created(new URI("/api/accounting-procedure-executions/" + accountingProcedureExecution.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accountingProcedureExecution.getId().toString())
            )
            .body(accountingProcedureExecution);
    }

    /**
     * {@code PUT  /accounting-procedure-executions/:id} : Updates an existing accountingProcedureExecution.
     *
     * @param id the id of the accountingProcedureExecution to save.
     * @param accountingProcedureExecution the accountingProcedureExecution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountingProcedureExecution,
     * or with status {@code 400 (Bad Request)} if the accountingProcedureExecution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountingProcedureExecution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountingProcedureExecution> updateAccountingProcedureExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountingProcedureExecution accountingProcedureExecution
    ) throws URISyntaxException {
        log.debug("REST request to update AccountingProcedureExecution : {}, {}", id, accountingProcedureExecution);
        if (accountingProcedureExecution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountingProcedureExecution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountingProcedureExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accountingProcedureExecution = accountingProcedureExecutionService.update(accountingProcedureExecution);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountingProcedureExecution.getId().toString())
            )
            .body(accountingProcedureExecution);
    }

    /**
     * {@code PATCH  /accounting-procedure-executions/:id} : Partial updates given fields of an existing accountingProcedureExecution, field will ignore if it is null
     *
     * @param id the id of the accountingProcedureExecution to save.
     * @param accountingProcedureExecution the accountingProcedureExecution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountingProcedureExecution,
     * or with status {@code 400 (Bad Request)} if the accountingProcedureExecution is not valid,
     * or with status {@code 404 (Not Found)} if the accountingProcedureExecution is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountingProcedureExecution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountingProcedureExecution> partialUpdateAccountingProcedureExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountingProcedureExecution accountingProcedureExecution
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountingProcedureExecution partially : {}, {}", id, accountingProcedureExecution);
        if (accountingProcedureExecution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountingProcedureExecution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountingProcedureExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountingProcedureExecution> result = accountingProcedureExecutionService.partialUpdate(accountingProcedureExecution);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountingProcedureExecution.getId().toString())
        );
    }

    /**
     * {@code GET  /accounting-procedure-executions} : get all the accountingProcedureExecutions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountingProcedureExecutions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccountingProcedureExecution>> getAllAccountingProcedureExecutions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AccountingProcedureExecutions");
        Page<AccountingProcedureExecution> page = accountingProcedureExecutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accounting-procedure-executions/:id} : get the "id" accountingProcedureExecution.
     *
     * @param id the id of the accountingProcedureExecution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountingProcedureExecution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountingProcedureExecution> getAccountingProcedureExecution(@PathVariable("id") Long id) {
        log.debug("REST request to get AccountingProcedureExecution : {}", id);
        Optional<AccountingProcedureExecution> accountingProcedureExecution = accountingProcedureExecutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountingProcedureExecution);
    }

    /**
     * {@code DELETE  /accounting-procedure-executions/:id} : delete the "id" accountingProcedureExecution.
     *
     * @param id the id of the accountingProcedureExecution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountingProcedureExecution(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccountingProcedureExecution : {}", id);
        accountingProcedureExecutionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /accounting-procedure-executions/_search?query=:query} : search for the accountingProcedureExecution corresponding
     * to the query.
     *
     * @param query the query of the accountingProcedureExecution search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccountingProcedureExecution>> searchAccountingProcedureExecutions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AccountingProcedureExecutions for query {}", query);
        try {
            Page<AccountingProcedureExecution> page = accountingProcedureExecutionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
