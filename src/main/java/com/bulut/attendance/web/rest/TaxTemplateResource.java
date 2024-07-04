package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.TaxTemplate;
import com.bulut.attendance.repository.TaxTemplateRepository;
import com.bulut.attendance.service.TaxTemplateService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.TaxTemplate}.
 */
@RestController
@RequestMapping("/api/tax-templates")
public class TaxTemplateResource {

    private final Logger log = LoggerFactory.getLogger(TaxTemplateResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceTaxTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxTemplateService taxTemplateService;

    private final TaxTemplateRepository taxTemplateRepository;

    public TaxTemplateResource(TaxTemplateService taxTemplateService, TaxTemplateRepository taxTemplateRepository) {
        this.taxTemplateService = taxTemplateService;
        this.taxTemplateRepository = taxTemplateRepository;
    }

    /**
     * {@code POST  /tax-templates} : Create a new taxTemplate.
     *
     * @param taxTemplate the taxTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxTemplate, or with status {@code 400 (Bad Request)} if the taxTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaxTemplate> createTaxTemplate(@RequestBody TaxTemplate taxTemplate) throws URISyntaxException {
        log.debug("REST request to save TaxTemplate : {}", taxTemplate);
        if (taxTemplate.getId() != null) {
            throw new BadRequestAlertException("A new taxTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taxTemplate = taxTemplateService.save(taxTemplate);
        return ResponseEntity.created(new URI("/api/tax-templates/" + taxTemplate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, taxTemplate.getId().toString()))
            .body(taxTemplate);
    }

    /**
     * {@code PUT  /tax-templates/:id} : Updates an existing taxTemplate.
     *
     * @param id the id of the taxTemplate to save.
     * @param taxTemplate the taxTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxTemplate,
     * or with status {@code 400 (Bad Request)} if the taxTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaxTemplate> updateTaxTemplate(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TaxTemplate taxTemplate
    ) throws URISyntaxException {
        log.debug("REST request to update TaxTemplate : {}, {}", id, taxTemplate);
        if (taxTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taxTemplate = taxTemplateService.update(taxTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxTemplate.getId().toString()))
            .body(taxTemplate);
    }

    /**
     * {@code PATCH  /tax-templates/:id} : Partial updates given fields of an existing taxTemplate, field will ignore if it is null
     *
     * @param id the id of the taxTemplate to save.
     * @param taxTemplate the taxTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxTemplate,
     * or with status {@code 400 (Bad Request)} if the taxTemplate is not valid,
     * or with status {@code 404 (Not Found)} if the taxTemplate is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxTemplate> partialUpdateTaxTemplate(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TaxTemplate taxTemplate
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxTemplate partially : {}, {}", id, taxTemplate);
        if (taxTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxTemplate> result = taxTemplateService.partialUpdate(taxTemplate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxTemplate.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-templates} : get all the taxTemplates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaxTemplate>> getAllTaxTemplates(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TaxTemplates");
        Page<TaxTemplate> page = taxTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-templates/:id} : get the "id" taxTemplate.
     *
     * @param id the id of the taxTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaxTemplate> getTaxTemplate(@PathVariable("id") UUID id) {
        log.debug("REST request to get TaxTemplate : {}", id);
        Optional<TaxTemplate> taxTemplate = taxTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxTemplate);
    }

    /**
     * {@code DELETE  /tax-templates/:id} : delete the "id" taxTemplate.
     *
     * @param id the id of the taxTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxTemplate(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TaxTemplate : {}", id);
        taxTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tax-templates/_search?query=:query} : search for the taxTemplate corresponding
     * to the query.
     *
     * @param query the query of the taxTemplate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TaxTemplate>> searchTaxTemplates(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TaxTemplates for query {}", query);
        try {
            Page<TaxTemplate> page = taxTemplateService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
