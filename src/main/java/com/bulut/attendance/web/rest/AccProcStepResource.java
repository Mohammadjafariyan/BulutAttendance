package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.AccProcStep;
import com.bulut.attendance.repository.AccProcStepRepository;
import com.bulut.attendance.service.AccProcStepService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.AccProcStep}.
 */
@RestController
@RequestMapping("/api/acc-proc-steps")
public class AccProcStepResource {

    private final Logger log = LoggerFactory.getLogger(AccProcStepResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceAccProcStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccProcStepService accProcStepService;

    private final AccProcStepRepository accProcStepRepository;

    public AccProcStepResource(AccProcStepService accProcStepService, AccProcStepRepository accProcStepRepository) {
        this.accProcStepService = accProcStepService;
        this.accProcStepRepository = accProcStepRepository;
    }

    /**
     * {@code POST  /acc-proc-steps} : Create a new accProcStep.
     *
     * @param accProcStep the accProcStep to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accProcStep, or with status {@code 400 (Bad Request)} if the accProcStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccProcStep> createAccProcStep(@RequestBody AccProcStep accProcStep) throws URISyntaxException {
        log.debug("REST request to save AccProcStep : {}", accProcStep);
        if (accProcStep.getId() != null) {
            throw new BadRequestAlertException("A new accProcStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accProcStep = accProcStepService.save(accProcStep);
        return ResponseEntity.created(new URI("/api/acc-proc-steps/" + accProcStep.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accProcStep.getId().toString()))
            .body(accProcStep);
    }

    /**
     * {@code PUT  /acc-proc-steps/:id} : Updates an existing accProcStep.
     *
     * @param id the id of the accProcStep to save.
     * @param accProcStep the accProcStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accProcStep,
     * or with status {@code 400 (Bad Request)} if the accProcStep is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accProcStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccProcStep> updateAccProcStep(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccProcStep accProcStep
    ) throws URISyntaxException {
        log.debug("REST request to update AccProcStep : {}, {}", id, accProcStep);
        if (accProcStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accProcStep.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accProcStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accProcStep = accProcStepService.update(accProcStep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accProcStep.getId().toString()))
            .body(accProcStep);
    }

    /**
     * {@code PATCH  /acc-proc-steps/:id} : Partial updates given fields of an existing accProcStep, field will ignore if it is null
     *
     * @param id the id of the accProcStep to save.
     * @param accProcStep the accProcStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accProcStep,
     * or with status {@code 400 (Bad Request)} if the accProcStep is not valid,
     * or with status {@code 404 (Not Found)} if the accProcStep is not found,
     * or with status {@code 500 (Internal Server Error)} if the accProcStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccProcStep> partialUpdateAccProcStep(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccProcStep accProcStep
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccProcStep partially : {}, {}", id, accProcStep);
        if (accProcStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accProcStep.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accProcStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccProcStep> result = accProcStepService.partialUpdate(accProcStep);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accProcStep.getId().toString())
        );
    }

    /**
     * {@code GET  /acc-proc-steps} : get all the accProcSteps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accProcSteps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccProcStep>> getAllAccProcSteps(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AccProcSteps");
        Page<AccProcStep> page = accProcStepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acc-proc-steps/:id} : get the "id" accProcStep.
     *
     * @param id the id of the accProcStep to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accProcStep, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccProcStep> getAccProcStep(@PathVariable("id") Long id) {
        log.debug("REST request to get AccProcStep : {}", id);
        Optional<AccProcStep> accProcStep = accProcStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accProcStep);
    }

    /**
     * {@code DELETE  /acc-proc-steps/:id} : delete the "id" accProcStep.
     *
     * @param id the id of the accProcStep to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccProcStep(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccProcStep : {}", id);
        accProcStepService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /acc-proc-steps/_search?query=:query} : search for the accProcStep corresponding
     * to the query.
     *
     * @param query the query of the accProcStep search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccProcStep>> searchAccProcSteps(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AccProcSteps for query {}", query);
        try {
            Page<AccProcStep> page = accProcStepService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
