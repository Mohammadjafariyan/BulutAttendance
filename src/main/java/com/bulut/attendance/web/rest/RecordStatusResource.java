package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.RecordStatus;
import com.bulut.attendance.repository.RecordStatusRepository;
import com.bulut.attendance.service.RecordStatusService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bulut.attendance.domain.RecordStatus}.
 */
@RestController
@RequestMapping("/api/record-statuses")
public class RecordStatusResource {

    private final Logger log = LoggerFactory.getLogger(RecordStatusResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceRecordStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecordStatusService recordStatusService;

    private final RecordStatusRepository recordStatusRepository;

    public RecordStatusResource(RecordStatusService recordStatusService, RecordStatusRepository recordStatusRepository) {
        this.recordStatusService = recordStatusService;
        this.recordStatusRepository = recordStatusRepository;
    }

    /**
     * {@code POST  /record-statuses} : Create a new recordStatus.
     *
     * @param recordStatus the recordStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recordStatus, or with status {@code 400 (Bad Request)} if the recordStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecordStatus> createRecordStatus(@RequestBody RecordStatus recordStatus) throws URISyntaxException {
        log.debug("REST request to save RecordStatus : {}", recordStatus);
        if (recordStatus.getId() != null) {
            throw new BadRequestAlertException("A new recordStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recordStatus = recordStatusService.save(recordStatus);
        return ResponseEntity.created(new URI("/api/record-statuses/" + recordStatus.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recordStatus.getId().toString()))
            .body(recordStatus);
    }

    /**
     * {@code PUT  /record-statuses/:id} : Updates an existing recordStatus.
     *
     * @param id the id of the recordStatus to save.
     * @param recordStatus the recordStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordStatus,
     * or with status {@code 400 (Bad Request)} if the recordStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recordStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecordStatus> updateRecordStatus(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody RecordStatus recordStatus
    ) throws URISyntaxException {
        log.debug("REST request to update RecordStatus : {}, {}", id, recordStatus);
        if (recordStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recordStatus = recordStatusService.update(recordStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordStatus.getId().toString()))
            .body(recordStatus);
    }

    /**
     * {@code PATCH  /record-statuses/:id} : Partial updates given fields of an existing recordStatus, field will ignore if it is null
     *
     * @param id the id of the recordStatus to save.
     * @param recordStatus the recordStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordStatus,
     * or with status {@code 400 (Bad Request)} if the recordStatus is not valid,
     * or with status {@code 404 (Not Found)} if the recordStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the recordStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecordStatus> partialUpdateRecordStatus(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody RecordStatus recordStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update RecordStatus partially : {}, {}", id, recordStatus);
        if (recordStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecordStatus> result = recordStatusService.partialUpdate(recordStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /record-statuses} : get all the recordStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recordStatuses in body.
     */
    @GetMapping("")
    public List<RecordStatus> getAllRecordStatuses() {
        log.debug("REST request to get all RecordStatuses");
        return recordStatusService.findAll();
    }

    /**
     * {@code GET  /record-statuses/:id} : get the "id" recordStatus.
     *
     * @param id the id of the recordStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recordStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecordStatus> getRecordStatus(@PathVariable("id") UUID id) {
        log.debug("REST request to get RecordStatus : {}", id);
        Optional<RecordStatus> recordStatus = recordStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recordStatus);
    }

    /**
     * {@code DELETE  /record-statuses/:id} : delete the "id" recordStatus.
     *
     * @param id the id of the recordStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecordStatus(@PathVariable("id") UUID id) {
        log.debug("REST request to delete RecordStatus : {}", id);
        recordStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /record-statuses/_search?query=:query} : search for the recordStatus corresponding
     * to the query.
     *
     * @param query the query of the recordStatus search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<RecordStatus> searchRecordStatuses(@RequestParam("query") String query) {
        log.debug("REST request to search RecordStatuses for query {}", query);
        try {
            return recordStatusService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
