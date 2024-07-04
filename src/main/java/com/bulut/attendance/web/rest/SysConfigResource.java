package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.SysConfig;
import com.bulut.attendance.repository.SysConfigRepository;
import com.bulut.attendance.service.SysConfigService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.SysConfig}.
 */
@RestController
@RequestMapping("/api/sys-configs")
public class SysConfigResource {

    private final Logger log = LoggerFactory.getLogger(SysConfigResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceSysConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysConfigService sysConfigService;

    private final SysConfigRepository sysConfigRepository;

    public SysConfigResource(SysConfigService sysConfigService, SysConfigRepository sysConfigRepository) {
        this.sysConfigService = sysConfigService;
        this.sysConfigRepository = sysConfigRepository;
    }

    /**
     * {@code POST  /sys-configs} : Create a new sysConfig.
     *
     * @param sysConfig the sysConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysConfig, or with status {@code 400 (Bad Request)} if the sysConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SysConfig> createSysConfig(@RequestBody SysConfig sysConfig) throws URISyntaxException {
        log.debug("REST request to save SysConfig : {}", sysConfig);
        if (sysConfig.getId() != null) {
            throw new BadRequestAlertException("A new sysConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sysConfig = sysConfigService.save(sysConfig);
        return ResponseEntity.created(new URI("/api/sys-configs/" + sysConfig.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sysConfig.getId().toString()))
            .body(sysConfig);
    }

    /**
     * {@code PUT  /sys-configs/:id} : Updates an existing sysConfig.
     *
     * @param id the id of the sysConfig to save.
     * @param sysConfig the sysConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysConfig,
     * or with status {@code 400 (Bad Request)} if the sysConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SysConfig> updateSysConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody SysConfig sysConfig
    ) throws URISyntaxException {
        log.debug("REST request to update SysConfig : {}, {}", id, sysConfig);
        if (sysConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sysConfig = sysConfigService.update(sysConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysConfig.getId().toString()))
            .body(sysConfig);
    }

    /**
     * {@code PATCH  /sys-configs/:id} : Partial updates given fields of an existing sysConfig, field will ignore if it is null
     *
     * @param id the id of the sysConfig to save.
     * @param sysConfig the sysConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysConfig,
     * or with status {@code 400 (Bad Request)} if the sysConfig is not valid,
     * or with status {@code 404 (Not Found)} if the sysConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SysConfig> partialUpdateSysConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody SysConfig sysConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysConfig partially : {}, {}", id, sysConfig);
        if (sysConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysConfig> result = sysConfigService.partialUpdate(sysConfig);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-configs} : get all the sysConfigs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SysConfig>> getAllSysConfigs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SysConfigs");
        Page<SysConfig> page = sysConfigService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sys-configs/:id} : get the "id" sysConfig.
     *
     * @param id the id of the sysConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SysConfig> getSysConfig(@PathVariable("id") UUID id) {
        log.debug("REST request to get SysConfig : {}", id);
        Optional<SysConfig> sysConfig = sysConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysConfig);
    }

    /**
     * {@code DELETE  /sys-configs/:id} : delete the "id" sysConfig.
     *
     * @param id the id of the sysConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSysConfig(@PathVariable("id") UUID id) {
        log.debug("REST request to delete SysConfig : {}", id);
        sysConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /sys-configs/_search?query=:query} : search for the sysConfig corresponding
     * to the query.
     *
     * @param query the query of the sysConfig search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SysConfig>> searchSysConfigs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of SysConfigs for query {}", query);
        try {
            Page<SysConfig> page = sysConfigService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
