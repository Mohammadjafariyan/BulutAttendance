package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.HrLetter;
import com.bulut.attendance.repository.HrLetterRepository;
import com.bulut.attendance.service.HrLetterService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.HrLetter}.
 */
@RestController
@RequestMapping("/api/hr-letters")
public class HrLetterResource {

    private final Logger log = LoggerFactory.getLogger(HrLetterResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceHrLetter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HrLetterService hrLetterService;

    private final HrLetterRepository hrLetterRepository;

    public HrLetterResource(HrLetterService hrLetterService, HrLetterRepository hrLetterRepository) {
        this.hrLetterService = hrLetterService;
        this.hrLetterRepository = hrLetterRepository;
    }

    /**
     * {@code POST  /hr-letters} : Create a new hrLetter.
     *
     * @param hrLetter the hrLetter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hrLetter, or with status {@code 400 (Bad Request)} if the hrLetter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HrLetter> createHrLetter(@RequestBody HrLetter hrLetter) throws URISyntaxException {
        log.debug("REST request to save HrLetter : {}", hrLetter);
        if (hrLetter.getId() != null) {
            throw new BadRequestAlertException("A new hrLetter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hrLetter = hrLetterService.save(hrLetter);
        return ResponseEntity.created(new URI("/api/hr-letters/" + hrLetter.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hrLetter.getId().toString()))
            .body(hrLetter);
    }

    /**
     * {@code PUT  /hr-letters/:id} : Updates an existing hrLetter.
     *
     * @param id the id of the hrLetter to save.
     * @param hrLetter the hrLetter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrLetter,
     * or with status {@code 400 (Bad Request)} if the hrLetter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hrLetter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HrLetter> updateHrLetter(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody HrLetter hrLetter
    ) throws URISyntaxException {
        log.debug("REST request to update HrLetter : {}, {}", id, hrLetter);
        if (hrLetter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrLetter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hrLetterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hrLetter = hrLetterService.update(hrLetter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hrLetter.getId().toString()))
            .body(hrLetter);
    }

    /**
     * {@code PATCH  /hr-letters/:id} : Partial updates given fields of an existing hrLetter, field will ignore if it is null
     *
     * @param id the id of the hrLetter to save.
     * @param hrLetter the hrLetter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrLetter,
     * or with status {@code 400 (Bad Request)} if the hrLetter is not valid,
     * or with status {@code 404 (Not Found)} if the hrLetter is not found,
     * or with status {@code 500 (Internal Server Error)} if the hrLetter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HrLetter> partialUpdateHrLetter(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody HrLetter hrLetter
    ) throws URISyntaxException {
        log.debug("REST request to partial update HrLetter partially : {}, {}", id, hrLetter);
        if (hrLetter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrLetter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hrLetterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HrLetter> result = hrLetterService.partialUpdate(hrLetter);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hrLetter.getId().toString())
        );
    }

    /**
     * {@code GET  /hr-letters} : get all the hrLetters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hrLetters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HrLetter>> getAllHrLetters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HrLetters");
        Page<HrLetter> page = hrLetterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hr-letters/:id} : get the "id" hrLetter.
     *
     * @param id the id of the hrLetter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hrLetter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HrLetter> getHrLetter(@PathVariable("id") UUID id) {
        log.debug("REST request to get HrLetter : {}", id);
        Optional<HrLetter> hrLetter = hrLetterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hrLetter);
    }

    /**
     * {@code DELETE  /hr-letters/:id} : delete the "id" hrLetter.
     *
     * @param id the id of the hrLetter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHrLetter(@PathVariable("id") UUID id) {
        log.debug("REST request to delete HrLetter : {}", id);
        hrLetterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /hr-letters/_search?query=:query} : search for the hrLetter corresponding
     * to the query.
     *
     * @param query the query of the hrLetter search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<HrLetter>> searchHrLetters(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of HrLetters for query {}", query);
        try {
            Page<HrLetter> page = hrLetterService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
