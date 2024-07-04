package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.LeaveSummary;
import com.bulut.attendance.repository.LeaveSummaryRepository;
import com.bulut.attendance.service.LeaveSummaryService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.LeaveSummary}.
 */
@RestController
@RequestMapping("/api/leave-summaries")
public class LeaveSummaryResource {

    private final Logger log = LoggerFactory.getLogger(LeaveSummaryResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceLeaveSummary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveSummaryService leaveSummaryService;

    private final LeaveSummaryRepository leaveSummaryRepository;

    public LeaveSummaryResource(LeaveSummaryService leaveSummaryService, LeaveSummaryRepository leaveSummaryRepository) {
        this.leaveSummaryService = leaveSummaryService;
        this.leaveSummaryRepository = leaveSummaryRepository;
    }

    /**
     * {@code POST  /leave-summaries} : Create a new leaveSummary.
     *
     * @param leaveSummary the leaveSummary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveSummary, or with status {@code 400 (Bad Request)} if the leaveSummary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LeaveSummary> createLeaveSummary(@RequestBody LeaveSummary leaveSummary) throws URISyntaxException {
        log.debug("REST request to save LeaveSummary : {}", leaveSummary);
        if (leaveSummary.getId() != null) {
            throw new BadRequestAlertException("A new leaveSummary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        leaveSummary = leaveSummaryService.save(leaveSummary);
        return ResponseEntity.created(new URI("/api/leave-summaries/" + leaveSummary.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, leaveSummary.getId().toString()))
            .body(leaveSummary);
    }

    /**
     * {@code PUT  /leave-summaries/:id} : Updates an existing leaveSummary.
     *
     * @param id the id of the leaveSummary to save.
     * @param leaveSummary the leaveSummary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveSummary,
     * or with status {@code 400 (Bad Request)} if the leaveSummary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveSummary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LeaveSummary> updateLeaveSummary(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody LeaveSummary leaveSummary
    ) throws URISyntaxException {
        log.debug("REST request to update LeaveSummary : {}, {}", id, leaveSummary);
        if (leaveSummary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveSummary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveSummaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        leaveSummary = leaveSummaryService.update(leaveSummary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveSummary.getId().toString()))
            .body(leaveSummary);
    }

    /**
     * {@code PATCH  /leave-summaries/:id} : Partial updates given fields of an existing leaveSummary, field will ignore if it is null
     *
     * @param id the id of the leaveSummary to save.
     * @param leaveSummary the leaveSummary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveSummary,
     * or with status {@code 400 (Bad Request)} if the leaveSummary is not valid,
     * or with status {@code 404 (Not Found)} if the leaveSummary is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaveSummary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaveSummary> partialUpdateLeaveSummary(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody LeaveSummary leaveSummary
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaveSummary partially : {}, {}", id, leaveSummary);
        if (leaveSummary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveSummary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveSummaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaveSummary> result = leaveSummaryService.partialUpdate(leaveSummary);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveSummary.getId().toString())
        );
    }

    /**
     * {@code GET  /leave-summaries} : get all the leaveSummaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveSummaries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LeaveSummary>> getAllLeaveSummaries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LeaveSummaries");
        Page<LeaveSummary> page = leaveSummaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-summaries/:id} : get the "id" leaveSummary.
     *
     * @param id the id of the leaveSummary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveSummary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeaveSummary> getLeaveSummary(@PathVariable("id") UUID id) {
        log.debug("REST request to get LeaveSummary : {}", id);
        Optional<LeaveSummary> leaveSummary = leaveSummaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveSummary);
    }

    /**
     * {@code DELETE  /leave-summaries/:id} : delete the "id" leaveSummary.
     *
     * @param id the id of the leaveSummary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveSummary(@PathVariable("id") UUID id) {
        log.debug("REST request to delete LeaveSummary : {}", id);
        leaveSummaryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /leave-summaries/_search?query=:query} : search for the leaveSummary corresponding
     * to the query.
     *
     * @param query the query of the leaveSummary search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<LeaveSummary>> searchLeaveSummaries(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaveSummaries for query {}", query);
        try {
            Page<LeaveSummary> page = leaveSummaryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
