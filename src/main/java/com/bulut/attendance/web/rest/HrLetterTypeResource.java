package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.HrLetterType;
import com.bulut.attendance.repository.HrLetterTypeRepository;
import com.bulut.attendance.service.HrLetterTypeService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.HrLetterType}.
 */
@RestController
@RequestMapping("/api/hr-letter-types")
public class HrLetterTypeResource {

    private final Logger log = LoggerFactory.getLogger(HrLetterTypeResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceHrLetterType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HrLetterTypeService hrLetterTypeService;

    private final HrLetterTypeRepository hrLetterTypeRepository;

    public HrLetterTypeResource(HrLetterTypeService hrLetterTypeService, HrLetterTypeRepository hrLetterTypeRepository) {
        this.hrLetterTypeService = hrLetterTypeService;
        this.hrLetterTypeRepository = hrLetterTypeRepository;
    }

    /**
     * {@code POST  /hr-letter-types} : Create a new hrLetterType.
     *
     * @param hrLetterType the hrLetterType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hrLetterType, or with status {@code 400 (Bad Request)} if the hrLetterType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HrLetterType> createHrLetterType(@RequestBody HrLetterType hrLetterType) throws URISyntaxException {
        log.debug("REST request to save HrLetterType : {}", hrLetterType);
        if (hrLetterType.getId() != null) {
            throw new BadRequestAlertException("A new hrLetterType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hrLetterType = hrLetterTypeService.save(hrLetterType);
        return ResponseEntity.created(new URI("/api/hr-letter-types/" + hrLetterType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hrLetterType.getId().toString()))
            .body(hrLetterType);
    }

    /**
     * {@code PUT  /hr-letter-types/:id} : Updates an existing hrLetterType.
     *
     * @param id the id of the hrLetterType to save.
     * @param hrLetterType the hrLetterType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrLetterType,
     * or with status {@code 400 (Bad Request)} if the hrLetterType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hrLetterType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HrLetterType> updateHrLetterType(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody HrLetterType hrLetterType
    ) throws URISyntaxException {
        log.debug("REST request to update HrLetterType : {}, {}", id, hrLetterType);
        if (hrLetterType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrLetterType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hrLetterTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hrLetterType = hrLetterTypeService.update(hrLetterType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hrLetterType.getId().toString()))
            .body(hrLetterType);
    }

    /**
     * {@code PATCH  /hr-letter-types/:id} : Partial updates given fields of an existing hrLetterType, field will ignore if it is null
     *
     * @param id the id of the hrLetterType to save.
     * @param hrLetterType the hrLetterType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrLetterType,
     * or with status {@code 400 (Bad Request)} if the hrLetterType is not valid,
     * or with status {@code 404 (Not Found)} if the hrLetterType is not found,
     * or with status {@code 500 (Internal Server Error)} if the hrLetterType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HrLetterType> partialUpdateHrLetterType(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody HrLetterType hrLetterType
    ) throws URISyntaxException {
        log.debug("REST request to partial update HrLetterType partially : {}, {}", id, hrLetterType);
        if (hrLetterType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrLetterType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hrLetterTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HrLetterType> result = hrLetterTypeService.partialUpdate(hrLetterType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hrLetterType.getId().toString())
        );
    }

    /**
     * {@code GET  /hr-letter-types} : get all the hrLetterTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hrLetterTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HrLetterType>> getAllHrLetterTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HrLetterTypes");
        Page<HrLetterType> page = hrLetterTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hr-letter-types/:id} : get the "id" hrLetterType.
     *
     * @param id the id of the hrLetterType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hrLetterType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HrLetterType> getHrLetterType(@PathVariable("id") UUID id) {
        log.debug("REST request to get HrLetterType : {}", id);
        Optional<HrLetterType> hrLetterType = hrLetterTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hrLetterType);
    }

    /**
     * {@code DELETE  /hr-letter-types/:id} : delete the "id" hrLetterType.
     *
     * @param id the id of the hrLetterType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHrLetterType(@PathVariable("id") UUID id) {
        log.debug("REST request to delete HrLetterType : {}", id);
        hrLetterTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /hr-letter-types/_search?query=:query} : search for the hrLetterType corresponding
     * to the query.
     *
     * @param query the query of the hrLetterType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<HrLetterType>> searchHrLetterTypes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of HrLetterTypes for query {}", query);
        try {
            Page<HrLetterType> page = hrLetterTypeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
