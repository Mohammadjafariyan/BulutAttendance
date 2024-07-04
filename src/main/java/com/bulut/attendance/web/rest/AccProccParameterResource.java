package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.AccProccParameter;
import com.bulut.attendance.repository.AccProccParameterRepository;
import com.bulut.attendance.service.AccProccParameterService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.AccProccParameter}.
 */
@RestController
@RequestMapping("/api/acc-procc-parameters")
public class AccProccParameterResource {

    private final Logger log = LoggerFactory.getLogger(AccProccParameterResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceAccProccParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccProccParameterService accProccParameterService;

    private final AccProccParameterRepository accProccParameterRepository;

    public AccProccParameterResource(
        AccProccParameterService accProccParameterService,
        AccProccParameterRepository accProccParameterRepository
    ) {
        this.accProccParameterService = accProccParameterService;
        this.accProccParameterRepository = accProccParameterRepository;
    }

    /**
     * {@code POST  /acc-procc-parameters} : Create a new accProccParameter.
     *
     * @param accProccParameter the accProccParameter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accProccParameter, or with status {@code 400 (Bad Request)} if the accProccParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccProccParameter> createAccProccParameter(@RequestBody AccProccParameter accProccParameter)
        throws URISyntaxException {
        log.debug("REST request to save AccProccParameter : {}", accProccParameter);
        if (accProccParameter.getId() != null) {
            throw new BadRequestAlertException("A new accProccParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accProccParameter = accProccParameterService.save(accProccParameter);
        return ResponseEntity.created(new URI("/api/acc-procc-parameters/" + accProccParameter.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accProccParameter.getId().toString()))
            .body(accProccParameter);
    }

    /**
     * {@code PUT  /acc-procc-parameters/:id} : Updates an existing accProccParameter.
     *
     * @param id the id of the accProccParameter to save.
     * @param accProccParameter the accProccParameter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accProccParameter,
     * or with status {@code 400 (Bad Request)} if the accProccParameter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accProccParameter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccProccParameter> updateAccProccParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccProccParameter accProccParameter
    ) throws URISyntaxException {
        log.debug("REST request to update AccProccParameter : {}, {}", id, accProccParameter);
        if (accProccParameter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accProccParameter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accProccParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accProccParameter = accProccParameterService.update(accProccParameter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accProccParameter.getId().toString()))
            .body(accProccParameter);
    }

    /**
     * {@code PATCH  /acc-procc-parameters/:id} : Partial updates given fields of an existing accProccParameter, field will ignore if it is null
     *
     * @param id the id of the accProccParameter to save.
     * @param accProccParameter the accProccParameter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accProccParameter,
     * or with status {@code 400 (Bad Request)} if the accProccParameter is not valid,
     * or with status {@code 404 (Not Found)} if the accProccParameter is not found,
     * or with status {@code 500 (Internal Server Error)} if the accProccParameter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccProccParameter> partialUpdateAccProccParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccProccParameter accProccParameter
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccProccParameter partially : {}, {}", id, accProccParameter);
        if (accProccParameter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accProccParameter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accProccParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccProccParameter> result = accProccParameterService.partialUpdate(accProccParameter);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accProccParameter.getId().toString())
        );
    }

    /**
     * {@code GET  /acc-procc-parameters} : get all the accProccParameters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accProccParameters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccProccParameter>> getAllAccProccParameters(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AccProccParameters");
        Page<AccProccParameter> page = accProccParameterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acc-procc-parameters/:id} : get the "id" accProccParameter.
     *
     * @param id the id of the accProccParameter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accProccParameter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccProccParameter> getAccProccParameter(@PathVariable("id") Long id) {
        log.debug("REST request to get AccProccParameter : {}", id);
        Optional<AccProccParameter> accProccParameter = accProccParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accProccParameter);
    }

    /**
     * {@code DELETE  /acc-procc-parameters/:id} : delete the "id" accProccParameter.
     *
     * @param id the id of the accProccParameter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccProccParameter(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccProccParameter : {}", id);
        accProccParameterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /acc-procc-parameters/_search?query=:query} : search for the accProccParameter corresponding
     * to the query.
     *
     * @param query the query of the accProccParameter search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccProccParameter>> searchAccProccParameters(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AccProccParameters for query {}", query);
        try {
            Page<AccProccParameter> page = accProccParameterService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
