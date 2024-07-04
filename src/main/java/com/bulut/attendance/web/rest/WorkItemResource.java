package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.WorkItem;
import com.bulut.attendance.repository.WorkItemRepository;
import com.bulut.attendance.service.WorkItemService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.WorkItem}.
 */
@RestController
@RequestMapping("/api/work-items")
public class WorkItemResource {

    private final Logger log = LoggerFactory.getLogger(WorkItemResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceWorkItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkItemService workItemService;

    private final WorkItemRepository workItemRepository;

    public WorkItemResource(WorkItemService workItemService, WorkItemRepository workItemRepository) {
        this.workItemService = workItemService;
        this.workItemRepository = workItemRepository;
    }

    /**
     * {@code POST  /work-items} : Create a new workItem.
     *
     * @param workItem the workItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workItem, or with status {@code 400 (Bad Request)} if the workItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkItem> createWorkItem(@RequestBody WorkItem workItem) throws URISyntaxException {
        log.debug("REST request to save WorkItem : {}", workItem);
        if (workItem.getId() != null) {
            throw new BadRequestAlertException("A new workItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workItem = workItemService.save(workItem);
        return ResponseEntity.created(new URI("/api/work-items/" + workItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, workItem.getId().toString()))
            .body(workItem);
    }

    /**
     * {@code PUT  /work-items/:id} : Updates an existing workItem.
     *
     * @param id the id of the workItem to save.
     * @param workItem the workItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workItem,
     * or with status {@code 400 (Bad Request)} if the workItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkItem> updateWorkItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody WorkItem workItem
    ) throws URISyntaxException {
        log.debug("REST request to update WorkItem : {}, {}", id, workItem);
        if (workItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        workItem = workItemService.update(workItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workItem.getId().toString()))
            .body(workItem);
    }

    /**
     * {@code PATCH  /work-items/:id} : Partial updates given fields of an existing workItem, field will ignore if it is null
     *
     * @param id the id of the workItem to save.
     * @param workItem the workItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workItem,
     * or with status {@code 400 (Bad Request)} if the workItem is not valid,
     * or with status {@code 404 (Not Found)} if the workItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the workItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkItem> partialUpdateWorkItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody WorkItem workItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkItem partially : {}, {}", id, workItem);
        if (workItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkItem> result = workItemService.partialUpdate(workItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workItem.getId().toString())
        );
    }

    /**
     * {@code GET  /work-items} : get all the workItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WorkItem>> getAllWorkItems(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WorkItems");
        Page<WorkItem> page = workItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-items/:id} : get the "id" workItem.
     *
     * @param id the id of the workItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkItem> getWorkItem(@PathVariable("id") UUID id) {
        log.debug("REST request to get WorkItem : {}", id);
        Optional<WorkItem> workItem = workItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workItem);
    }

    /**
     * {@code DELETE  /work-items/:id} : delete the "id" workItem.
     *
     * @param id the id of the workItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkItem(@PathVariable("id") UUID id) {
        log.debug("REST request to delete WorkItem : {}", id);
        workItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /work-items/_search?query=:query} : search for the workItem corresponding
     * to the query.
     *
     * @param query the query of the workItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<WorkItem>> searchWorkItems(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of WorkItems for query {}", query);
        try {
            Page<WorkItem> page = workItemService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
