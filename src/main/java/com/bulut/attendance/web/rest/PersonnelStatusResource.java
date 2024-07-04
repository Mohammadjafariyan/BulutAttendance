package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.PersonnelStatus;
import com.bulut.attendance.repository.PersonnelStatusRepository;
import com.bulut.attendance.service.PersonnelStatusService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.PersonnelStatus}.
 */
@RestController
@RequestMapping("/api/personnel-statuses")
public class PersonnelStatusResource {

    private final Logger log = LoggerFactory.getLogger(PersonnelStatusResource.class);

    private static final String ENTITY_NAME = "bulutAttendancePersonnelStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonnelStatusService personnelStatusService;

    private final PersonnelStatusRepository personnelStatusRepository;

    public PersonnelStatusResource(PersonnelStatusService personnelStatusService, PersonnelStatusRepository personnelStatusRepository) {
        this.personnelStatusService = personnelStatusService;
        this.personnelStatusRepository = personnelStatusRepository;
    }

    /**
     * {@code POST  /personnel-statuses} : Create a new personnelStatus.
     *
     * @param personnelStatus the personnelStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personnelStatus, or with status {@code 400 (Bad Request)} if the personnelStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonnelStatus> createPersonnelStatus(@RequestBody PersonnelStatus personnelStatus) throws URISyntaxException {
        log.debug("REST request to save PersonnelStatus : {}", personnelStatus);
        if (personnelStatus.getId() != null) {
            throw new BadRequestAlertException("A new personnelStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        personnelStatus = personnelStatusService.save(personnelStatus);
        return ResponseEntity.created(new URI("/api/personnel-statuses/" + personnelStatus.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, personnelStatus.getId().toString()))
            .body(personnelStatus);
    }

    /**
     * {@code PUT  /personnel-statuses/:id} : Updates an existing personnelStatus.
     *
     * @param id the id of the personnelStatus to save.
     * @param personnelStatus the personnelStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnelStatus,
     * or with status {@code 400 (Bad Request)} if the personnelStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personnelStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonnelStatus> updatePersonnelStatus(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody PersonnelStatus personnelStatus
    ) throws URISyntaxException {
        log.debug("REST request to update PersonnelStatus : {}, {}", id, personnelStatus);
        if (personnelStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personnelStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personnelStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        personnelStatus = personnelStatusService.update(personnelStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnelStatus.getId().toString()))
            .body(personnelStatus);
    }

    /**
     * {@code PATCH  /personnel-statuses/:id} : Partial updates given fields of an existing personnelStatus, field will ignore if it is null
     *
     * @param id the id of the personnelStatus to save.
     * @param personnelStatus the personnelStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personnelStatus,
     * or with status {@code 400 (Bad Request)} if the personnelStatus is not valid,
     * or with status {@code 404 (Not Found)} if the personnelStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the personnelStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonnelStatus> partialUpdatePersonnelStatus(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody PersonnelStatus personnelStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonnelStatus partially : {}, {}", id, personnelStatus);
        if (personnelStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personnelStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personnelStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonnelStatus> result = personnelStatusService.partialUpdate(personnelStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personnelStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /personnel-statuses} : get all the personnelStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personnelStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PersonnelStatus>> getAllPersonnelStatuses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PersonnelStatuses");
        Page<PersonnelStatus> page = personnelStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personnel-statuses/:id} : get the "id" personnelStatus.
     *
     * @param id the id of the personnelStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personnelStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonnelStatus> getPersonnelStatus(@PathVariable("id") UUID id) {
        log.debug("REST request to get PersonnelStatus : {}", id);
        Optional<PersonnelStatus> personnelStatus = personnelStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personnelStatus);
    }

    /**
     * {@code DELETE  /personnel-statuses/:id} : delete the "id" personnelStatus.
     *
     * @param id the id of the personnelStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonnelStatus(@PathVariable("id") UUID id) {
        log.debug("REST request to delete PersonnelStatus : {}", id);
        personnelStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /personnel-statuses/_search?query=:query} : search for the personnelStatus corresponding
     * to the query.
     *
     * @param query the query of the personnelStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<PersonnelStatus>> searchPersonnelStatuses(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of PersonnelStatuses for query {}", query);
        try {
            Page<PersonnelStatus> page = personnelStatusService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
