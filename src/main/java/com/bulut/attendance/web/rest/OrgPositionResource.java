package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.OrgPosition;
import com.bulut.attendance.repository.OrgPositionRepository;
import com.bulut.attendance.service.OrgPositionService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.OrgPosition}.
 */
@RestController
@RequestMapping("/api/org-positions")
public class OrgPositionResource {

    private final Logger log = LoggerFactory.getLogger(OrgPositionResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceOrgPosition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgPositionService orgPositionService;

    private final OrgPositionRepository orgPositionRepository;

    public OrgPositionResource(OrgPositionService orgPositionService, OrgPositionRepository orgPositionRepository) {
        this.orgPositionService = orgPositionService;
        this.orgPositionRepository = orgPositionRepository;
    }

    /**
     * {@code POST  /org-positions} : Create a new orgPosition.
     *
     * @param orgPosition the orgPosition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgPosition, or with status {@code 400 (Bad Request)} if the orgPosition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrgPosition> createOrgPosition(@RequestBody OrgPosition orgPosition) throws URISyntaxException {
        log.debug("REST request to save OrgPosition : {}", orgPosition);
        if (orgPosition.getId() != null) {
            throw new BadRequestAlertException("A new orgPosition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orgPosition = orgPositionService.save(orgPosition);
        return ResponseEntity.created(new URI("/api/org-positions/" + orgPosition.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orgPosition.getId().toString()))
            .body(orgPosition);
    }

    /**
     * {@code PUT  /org-positions/:id} : Updates an existing orgPosition.
     *
     * @param id the id of the orgPosition to save.
     * @param orgPosition the orgPosition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgPosition,
     * or with status {@code 400 (Bad Request)} if the orgPosition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgPosition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrgPosition> updateOrgPosition(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody OrgPosition orgPosition
    ) throws URISyntaxException {
        log.debug("REST request to update OrgPosition : {}, {}", id, orgPosition);
        if (orgPosition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgPosition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orgPosition = orgPositionService.update(orgPosition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgPosition.getId().toString()))
            .body(orgPosition);
    }

    /**
     * {@code PATCH  /org-positions/:id} : Partial updates given fields of an existing orgPosition, field will ignore if it is null
     *
     * @param id the id of the orgPosition to save.
     * @param orgPosition the orgPosition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgPosition,
     * or with status {@code 400 (Bad Request)} if the orgPosition is not valid,
     * or with status {@code 404 (Not Found)} if the orgPosition is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgPosition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrgPosition> partialUpdateOrgPosition(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody OrgPosition orgPosition
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgPosition partially : {}, {}", id, orgPosition);
        if (orgPosition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgPosition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgPosition> result = orgPositionService.partialUpdate(orgPosition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgPosition.getId().toString())
        );
    }

    /**
     * {@code GET  /org-positions} : get all the orgPositions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgPositions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrgPosition>> getAllOrgPositions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OrgPositions");
        Page<OrgPosition> page = orgPositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /org-positions/:id} : get the "id" orgPosition.
     *
     * @param id the id of the orgPosition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgPosition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrgPosition> getOrgPosition(@PathVariable("id") UUID id) {
        log.debug("REST request to get OrgPosition : {}", id);
        Optional<OrgPosition> orgPosition = orgPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgPosition);
    }

    /**
     * {@code DELETE  /org-positions/:id} : delete the "id" orgPosition.
     *
     * @param id the id of the orgPosition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrgPosition(@PathVariable("id") UUID id) {
        log.debug("REST request to delete OrgPosition : {}", id);
        orgPositionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /org-positions/_search?query=:query} : search for the orgPosition corresponding
     * to the query.
     *
     * @param query the query of the orgPosition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<OrgPosition>> searchOrgPositions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of OrgPositions for query {}", query);
        try {
            Page<OrgPosition> page = orgPositionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
