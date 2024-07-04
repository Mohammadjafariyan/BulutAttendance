package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.AccProcStepExecution;
import com.bulut.attendance.repository.AccProcStepExecutionRepository;
import com.bulut.attendance.service.AccProcStepExecutionService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.AccProcStepExecution}.
 */
@RestController
@RequestMapping("/api/acc-proc-step-executions")
public class AccProcStepExecutionResource {

    private final Logger log = LoggerFactory.getLogger(AccProcStepExecutionResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceAccProcStepExecution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccProcStepExecutionService accProcStepExecutionService;

    private final AccProcStepExecutionRepository accProcStepExecutionRepository;

    public AccProcStepExecutionResource(
        AccProcStepExecutionService accProcStepExecutionService,
        AccProcStepExecutionRepository accProcStepExecutionRepository
    ) {
        this.accProcStepExecutionService = accProcStepExecutionService;
        this.accProcStepExecutionRepository = accProcStepExecutionRepository;
    }

    /**
     * {@code POST  /acc-proc-step-executions} : Create a new accProcStepExecution.
     *
     * @param accProcStepExecution the accProcStepExecution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accProcStepExecution, or with status {@code 400 (Bad Request)} if the accProcStepExecution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccProcStepExecution> createAccProcStepExecution(@RequestBody AccProcStepExecution accProcStepExecution)
        throws URISyntaxException {
        log.debug("REST request to save AccProcStepExecution : {}", accProcStepExecution);
        if (accProcStepExecution.getId() != null) {
            throw new BadRequestAlertException("A new accProcStepExecution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accProcStepExecution = accProcStepExecutionService.save(accProcStepExecution);
        return ResponseEntity.created(new URI("/api/acc-proc-step-executions/" + accProcStepExecution.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accProcStepExecution.getId().toString()))
            .body(accProcStepExecution);
    }

    /**
     * {@code PUT  /acc-proc-step-executions/:id} : Updates an existing accProcStepExecution.
     *
     * @param id the id of the accProcStepExecution to save.
     * @param accProcStepExecution the accProcStepExecution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accProcStepExecution,
     * or with status {@code 400 (Bad Request)} if the accProcStepExecution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accProcStepExecution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccProcStepExecution> updateAccProcStepExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccProcStepExecution accProcStepExecution
    ) throws URISyntaxException {
        log.debug("REST request to update AccProcStepExecution : {}, {}", id, accProcStepExecution);
        if (accProcStepExecution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accProcStepExecution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accProcStepExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accProcStepExecution = accProcStepExecutionService.update(accProcStepExecution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accProcStepExecution.getId().toString()))
            .body(accProcStepExecution);
    }

    /**
     * {@code PATCH  /acc-proc-step-executions/:id} : Partial updates given fields of an existing accProcStepExecution, field will ignore if it is null
     *
     * @param id the id of the accProcStepExecution to save.
     * @param accProcStepExecution the accProcStepExecution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accProcStepExecution,
     * or with status {@code 400 (Bad Request)} if the accProcStepExecution is not valid,
     * or with status {@code 404 (Not Found)} if the accProcStepExecution is not found,
     * or with status {@code 500 (Internal Server Error)} if the accProcStepExecution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccProcStepExecution> partialUpdateAccProcStepExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccProcStepExecution accProcStepExecution
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccProcStepExecution partially : {}, {}", id, accProcStepExecution);
        if (accProcStepExecution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accProcStepExecution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accProcStepExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccProcStepExecution> result = accProcStepExecutionService.partialUpdate(accProcStepExecution);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accProcStepExecution.getId().toString())
        );
    }

    /**
     * {@code GET  /acc-proc-step-executions} : get all the accProcStepExecutions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accProcStepExecutions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccProcStepExecution>> getAllAccProcStepExecutions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AccProcStepExecutions");
        Page<AccProcStepExecution> page = accProcStepExecutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acc-proc-step-executions/:id} : get the "id" accProcStepExecution.
     *
     * @param id the id of the accProcStepExecution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accProcStepExecution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccProcStepExecution> getAccProcStepExecution(@PathVariable("id") Long id) {
        log.debug("REST request to get AccProcStepExecution : {}", id);
        Optional<AccProcStepExecution> accProcStepExecution = accProcStepExecutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accProcStepExecution);
    }

    /**
     * {@code DELETE  /acc-proc-step-executions/:id} : delete the "id" accProcStepExecution.
     *
     * @param id the id of the accProcStepExecution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccProcStepExecution(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccProcStepExecution : {}", id);
        accProcStepExecutionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /acc-proc-step-executions/_search?query=:query} : search for the accProcStepExecution corresponding
     * to the query.
     *
     * @param query the query of the accProcStepExecution search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccProcStepExecution>> searchAccProcStepExecutions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AccProcStepExecutions for query {}", query);
        try {
            Page<AccProcStepExecution> page = accProcStepExecutionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
