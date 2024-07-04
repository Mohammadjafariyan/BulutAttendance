package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.TransactionAccount;
import com.bulut.attendance.repository.TransactionAccountRepository;
import com.bulut.attendance.service.TransactionAccountService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.TransactionAccount}.
 */
@RestController
@RequestMapping("/api/transaction-accounts")
public class TransactionAccountResource {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceTransactionAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionAccountService transactionAccountService;

    private final TransactionAccountRepository transactionAccountRepository;

    public TransactionAccountResource(
        TransactionAccountService transactionAccountService,
        TransactionAccountRepository transactionAccountRepository
    ) {
        this.transactionAccountService = transactionAccountService;
        this.transactionAccountRepository = transactionAccountRepository;
    }

    /**
     * {@code POST  /transaction-accounts} : Create a new transactionAccount.
     *
     * @param transactionAccount the transactionAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionAccount, or with status {@code 400 (Bad Request)} if the transactionAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionAccount> createTransactionAccount(@RequestBody TransactionAccount transactionAccount)
        throws URISyntaxException {
        log.debug("REST request to save TransactionAccount : {}", transactionAccount);
        if (transactionAccount.getId() != null) {
            throw new BadRequestAlertException("A new transactionAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionAccount = transactionAccountService.save(transactionAccount);
        return ResponseEntity.created(new URI("/api/transaction-accounts/" + transactionAccount.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transactionAccount.getId().toString()))
            .body(transactionAccount);
    }

    /**
     * {@code PUT  /transaction-accounts/:id} : Updates an existing transactionAccount.
     *
     * @param id the id of the transactionAccount to save.
     * @param transactionAccount the transactionAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccount,
     * or with status {@code 400 (Bad Request)} if the transactionAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionAccount> updateTransactionAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionAccount transactionAccount
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionAccount : {}, {}", id, transactionAccount);
        if (transactionAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transactionAccount = transactionAccountService.update(transactionAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccount.getId().toString()))
            .body(transactionAccount);
    }

    /**
     * {@code PATCH  /transaction-accounts/:id} : Partial updates given fields of an existing transactionAccount, field will ignore if it is null
     *
     * @param id the id of the transactionAccount to save.
     * @param transactionAccount the transactionAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccount,
     * or with status {@code 400 (Bad Request)} if the transactionAccount is not valid,
     * or with status {@code 404 (Not Found)} if the transactionAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionAccount> partialUpdateTransactionAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransactionAccount transactionAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionAccount partially : {}, {}", id, transactionAccount);
        if (transactionAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionAccount> result = transactionAccountService.partialUpdate(transactionAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-accounts} : get all the transactionAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccounts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionAccount>> getAllTransactionAccounts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TransactionAccounts");
        Page<TransactionAccount> page = transactionAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-accounts/:id} : get the "id" transactionAccount.
     *
     * @param id the id of the transactionAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionAccount> getTransactionAccount(@PathVariable("id") Long id) {
        log.debug("REST request to get TransactionAccount : {}", id);
        Optional<TransactionAccount> transactionAccount = transactionAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionAccount);
    }

    /**
     * {@code DELETE  /transaction-accounts/:id} : delete the "id" transactionAccount.
     *
     * @param id the id of the transactionAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionAccount(@PathVariable("id") Long id) {
        log.debug("REST request to delete TransactionAccount : {}", id);
        transactionAccountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /transaction-accounts/_search?query=:query} : search for the transactionAccount corresponding
     * to the query.
     *
     * @param query the query of the transactionAccount search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TransactionAccount>> searchTransactionAccounts(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransactionAccounts for query {}", query);
        try {
            Page<TransactionAccount> page = transactionAccountService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
