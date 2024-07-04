package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.Leave;
import com.bulut.attendance.repository.LeaveRepository;
import com.bulut.attendance.service.LeaveService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.Leave}.
 */
@RestController
@RequestMapping("/api/leaves")
public class LeaveResource {

    private final Logger log = LoggerFactory.getLogger(LeaveResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceLeave";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveService leaveService;

    private final LeaveRepository leaveRepository;

    public LeaveResource(LeaveService leaveService, LeaveRepository leaveRepository) {
        this.leaveService = leaveService;
        this.leaveRepository = leaveRepository;
    }

    /**
     * {@code POST  /leaves} : Create a new leave.
     *
     * @param leave the leave to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leave, or with status {@code 400 (Bad Request)} if the leave has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Leave> createLeave(@RequestBody Leave leave) throws URISyntaxException {
        log.debug("REST request to save Leave : {}", leave);
        if (leave.getId() != null) {
            throw new BadRequestAlertException("A new leave cannot already have an ID", ENTITY_NAME, "idexists");
        }
        leave = leaveService.save(leave);
        return ResponseEntity.created(new URI("/api/leaves/" + leave.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, leave.getId().toString()))
            .body(leave);
    }

    /**
     * {@code PUT  /leaves/:id} : Updates an existing leave.
     *
     * @param id the id of the leave to save.
     * @param leave the leave to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leave,
     * or with status {@code 400 (Bad Request)} if the leave is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leave couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Leave> updateLeave(@PathVariable(value = "id", required = false) final UUID id, @RequestBody Leave leave)
        throws URISyntaxException {
        log.debug("REST request to update Leave : {}, {}", id, leave);
        if (leave.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leave.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        leave = leaveService.update(leave);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leave.getId().toString()))
            .body(leave);
    }

    /**
     * {@code PATCH  /leaves/:id} : Partial updates given fields of an existing leave, field will ignore if it is null
     *
     * @param id the id of the leave to save.
     * @param leave the leave to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leave,
     * or with status {@code 400 (Bad Request)} if the leave is not valid,
     * or with status {@code 404 (Not Found)} if the leave is not found,
     * or with status {@code 500 (Internal Server Error)} if the leave couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Leave> partialUpdateLeave(@PathVariable(value = "id", required = false) final UUID id, @RequestBody Leave leave)
        throws URISyntaxException {
        log.debug("REST request to partial update Leave partially : {}, {}", id, leave);
        if (leave.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leave.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Leave> result = leaveService.partialUpdate(leave);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leave.getId().toString())
        );
    }

    /**
     * {@code GET  /leaves} : get all the leaves.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaves in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Leave>> getAllLeaves(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Leaves");
        Page<Leave> page = leaveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leaves/:id} : get the "id" leave.
     *
     * @param id the id of the leave to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leave, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Leave> getLeave(@PathVariable("id") UUID id) {
        log.debug("REST request to get Leave : {}", id);
        Optional<Leave> leave = leaveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leave);
    }

    /**
     * {@code DELETE  /leaves/:id} : delete the "id" leave.
     *
     * @param id the id of the leave to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Leave : {}", id);
        leaveService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /leaves/_search?query=:query} : search for the leave corresponding
     * to the query.
     *
     * @param query the query of the leave search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<Leave>> searchLeaves(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Leaves for query {}", query);
        try {
            Page<Leave> page = leaveService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
