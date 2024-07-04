package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.Work;
import com.bulut.attendance.repository.WorkRepository;
import com.bulut.attendance.service.WorkService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.Work}.
 */
@RestController
@RequestMapping("/api/works")
public class WorkResource {

    private final Logger log = LoggerFactory.getLogger(WorkResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceWork";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkService workService;

    private final WorkRepository workRepository;

    public WorkResource(WorkService workService, WorkRepository workRepository) {
        this.workService = workService;
        this.workRepository = workRepository;
    }

    /**
     * {@code POST  /works} : Create a new work.
     *
     * @param work the work to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new work, or with status {@code 400 (Bad Request)} if the work has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Work> createWork(@RequestBody Work work) throws URISyntaxException {
        log.debug("REST request to save Work : {}", work);
        if (work.getId() != null) {
            throw new BadRequestAlertException("A new work cannot already have an ID", ENTITY_NAME, "idexists");
        }
        work = workService.save(work);
        return ResponseEntity.created(new URI("/api/works/" + work.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, work.getId().toString()))
            .body(work);
    }

    /**
     * {@code PUT  /works/:id} : Updates an existing work.
     *
     * @param id the id of the work to save.
     * @param work the work to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated work,
     * or with status {@code 400 (Bad Request)} if the work is not valid,
     * or with status {@code 500 (Internal Server Error)} if the work couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Work> updateWork(@PathVariable(value = "id", required = false) final UUID id, @RequestBody Work work)
        throws URISyntaxException {
        log.debug("REST request to update Work : {}, {}", id, work);
        if (work.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, work.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        work = workService.update(work);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, work.getId().toString()))
            .body(work);
    }

    /**
     * {@code PATCH  /works/:id} : Partial updates given fields of an existing work, field will ignore if it is null
     *
     * @param id the id of the work to save.
     * @param work the work to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated work,
     * or with status {@code 400 (Bad Request)} if the work is not valid,
     * or with status {@code 404 (Not Found)} if the work is not found,
     * or with status {@code 500 (Internal Server Error)} if the work couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Work> partialUpdateWork(@PathVariable(value = "id", required = false) final UUID id, @RequestBody Work work)
        throws URISyntaxException {
        log.debug("REST request to partial update Work partially : {}, {}", id, work);
        if (work.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, work.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Work> result = workService.partialUpdate(work);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, work.getId().toString())
        );
    }

    /**
     * {@code GET  /works} : get all the works.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of works in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Work>> getAllWorks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Works");
        Page<Work> page = workService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /works/:id} : get the "id" work.
     *
     * @param id the id of the work to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the work, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Work> getWork(@PathVariable("id") UUID id) {
        log.debug("REST request to get Work : {}", id);
        Optional<Work> work = workService.findOne(id);
        return ResponseUtil.wrapOrNotFound(work);
    }

    /**
     * {@code DELETE  /works/:id} : delete the "id" work.
     *
     * @param id the id of the work to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWork(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Work : {}", id);
        workService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /works/_search?query=:query} : search for the work corresponding
     * to the query.
     *
     * @param query the query of the work search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<Work>> searchWorks(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Works for query {}", query);
        try {
            Page<Work> page = workService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
