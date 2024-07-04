package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.HrLetterParameter;
import com.bulut.attendance.repository.HrLetterParameterRepository;
import com.bulut.attendance.service.HrLetterParameterService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bulut.attendance.domain.HrLetterParameter}.
 */
@RestController
@RequestMapping("/api/hr-letter-parameters")
public class HrLetterParameterResource {

    private final Logger log = LoggerFactory.getLogger(HrLetterParameterResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceHrLetterParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HrLetterParameterService hrLetterParameterService;

    private final HrLetterParameterRepository hrLetterParameterRepository;

    public HrLetterParameterResource(
        HrLetterParameterService hrLetterParameterService,
        HrLetterParameterRepository hrLetterParameterRepository
    ) {
        this.hrLetterParameterService = hrLetterParameterService;
        this.hrLetterParameterRepository = hrLetterParameterRepository;
    }

    /**
     * {@code POST  /hr-letter-parameters} : Create a new hrLetterParameter.
     *
     * @param hrLetterParameter the hrLetterParameter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hrLetterParameter, or with status {@code 400 (Bad Request)} if the hrLetterParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HrLetterParameter> createHrLetterParameter(@RequestBody HrLetterParameter hrLetterParameter)
        throws URISyntaxException {
        log.debug("REST request to save HrLetterParameter : {}", hrLetterParameter);
        if (hrLetterParameter.getId() != null) {
            throw new BadRequestAlertException("A new hrLetterParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hrLetterParameter = hrLetterParameterService.save(hrLetterParameter);
        return ResponseEntity.created(new URI("/api/hr-letter-parameters/" + hrLetterParameter.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hrLetterParameter.getId().toString()))
            .body(hrLetterParameter);
    }

    /**
     * {@code PUT  /hr-letter-parameters/:id} : Updates an existing hrLetterParameter.
     *
     * @param id the id of the hrLetterParameter to save.
     * @param hrLetterParameter the hrLetterParameter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrLetterParameter,
     * or with status {@code 400 (Bad Request)} if the hrLetterParameter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hrLetterParameter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HrLetterParameter> updateHrLetterParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HrLetterParameter hrLetterParameter
    ) throws URISyntaxException {
        log.debug("REST request to update HrLetterParameter : {}, {}", id, hrLetterParameter);
        if (hrLetterParameter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrLetterParameter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hrLetterParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hrLetterParameter = hrLetterParameterService.update(hrLetterParameter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hrLetterParameter.getId().toString()))
            .body(hrLetterParameter);
    }

    /**
     * {@code PATCH  /hr-letter-parameters/:id} : Partial updates given fields of an existing hrLetterParameter, field will ignore if it is null
     *
     * @param id the id of the hrLetterParameter to save.
     * @param hrLetterParameter the hrLetterParameter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrLetterParameter,
     * or with status {@code 400 (Bad Request)} if the hrLetterParameter is not valid,
     * or with status {@code 404 (Not Found)} if the hrLetterParameter is not found,
     * or with status {@code 500 (Internal Server Error)} if the hrLetterParameter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HrLetterParameter> partialUpdateHrLetterParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HrLetterParameter hrLetterParameter
    ) throws URISyntaxException {
        log.debug("REST request to partial update HrLetterParameter partially : {}, {}", id, hrLetterParameter);
        if (hrLetterParameter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrLetterParameter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hrLetterParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HrLetterParameter> result = hrLetterParameterService.partialUpdate(hrLetterParameter);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hrLetterParameter.getId().toString())
        );
    }

    /**
     * {@code GET  /hr-letter-parameters} : get all the hrLetterParameters.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hrLetterParameters in body.
     */
    @GetMapping("")
    public List<HrLetterParameter> getAllHrLetterParameters(@RequestParam(name = "filter", required = false) String filter) {
        if ("workitem-is-null".equals(filter)) {
            log.debug("REST request to get all HrLetterParameters where workItem is null");
            return hrLetterParameterService.findAllWhereWorkItemIsNull();
        }
        log.debug("REST request to get all HrLetterParameters");
        return hrLetterParameterService.findAll();
    }

    /**
     * {@code GET  /hr-letter-parameters/:id} : get the "id" hrLetterParameter.
     *
     * @param id the id of the hrLetterParameter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hrLetterParameter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HrLetterParameter> getHrLetterParameter(@PathVariable("id") Long id) {
        log.debug("REST request to get HrLetterParameter : {}", id);
        Optional<HrLetterParameter> hrLetterParameter = hrLetterParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hrLetterParameter);
    }

    /**
     * {@code DELETE  /hr-letter-parameters/:id} : delete the "id" hrLetterParameter.
     *
     * @param id the id of the hrLetterParameter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHrLetterParameter(@PathVariable("id") Long id) {
        log.debug("REST request to delete HrLetterParameter : {}", id);
        hrLetterParameterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /hr-letter-parameters/_search?query=:query} : search for the hrLetterParameter corresponding
     * to the query.
     *
     * @param query the query of the hrLetterParameter search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<HrLetterParameter> searchHrLetterParameters(@RequestParam("query") String query) {
        log.debug("REST request to search HrLetterParameters for query {}", query);
        try {
            return hrLetterParameterService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
