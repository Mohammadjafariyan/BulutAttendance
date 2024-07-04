package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.AccountTemplate;
import com.bulut.attendance.repository.AccountTemplateRepository;
import com.bulut.attendance.service.AccountTemplateService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.AccountTemplate}.
 */
@RestController
@RequestMapping("/api/account-templates")
public class AccountTemplateResource {

    private final Logger log = LoggerFactory.getLogger(AccountTemplateResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceAccountTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountTemplateService accountTemplateService;

    private final AccountTemplateRepository accountTemplateRepository;

    public AccountTemplateResource(AccountTemplateService accountTemplateService, AccountTemplateRepository accountTemplateRepository) {
        this.accountTemplateService = accountTemplateService;
        this.accountTemplateRepository = accountTemplateRepository;
    }

    /**
     * {@code POST  /account-templates} : Create a new accountTemplate.
     *
     * @param accountTemplate the accountTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountTemplate, or with status {@code 400 (Bad Request)} if the accountTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountTemplate> createAccountTemplate(@RequestBody AccountTemplate accountTemplate) throws URISyntaxException {
        log.debug("REST request to save AccountTemplate : {}", accountTemplate);
        if (accountTemplate.getId() != null) {
            throw new BadRequestAlertException("A new accountTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accountTemplate = accountTemplateService.save(accountTemplate);
        return ResponseEntity.created(new URI("/api/account-templates/" + accountTemplate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accountTemplate.getId().toString()))
            .body(accountTemplate);
    }

    /**
     * {@code PUT  /account-templates/:id} : Updates an existing accountTemplate.
     *
     * @param id the id of the accountTemplate to save.
     * @param accountTemplate the accountTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountTemplate,
     * or with status {@code 400 (Bad Request)} if the accountTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountTemplate> updateAccountTemplate(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AccountTemplate accountTemplate
    ) throws URISyntaxException {
        log.debug("REST request to update AccountTemplate : {}, {}", id, accountTemplate);
        if (accountTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accountTemplate = accountTemplateService.update(accountTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountTemplate.getId().toString()))
            .body(accountTemplate);
    }

    /**
     * {@code PATCH  /account-templates/:id} : Partial updates given fields of an existing accountTemplate, field will ignore if it is null
     *
     * @param id the id of the accountTemplate to save.
     * @param accountTemplate the accountTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountTemplate,
     * or with status {@code 400 (Bad Request)} if the accountTemplate is not valid,
     * or with status {@code 404 (Not Found)} if the accountTemplate is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountTemplate> partialUpdateAccountTemplate(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AccountTemplate accountTemplate
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountTemplate partially : {}, {}", id, accountTemplate);
        if (accountTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountTemplate> result = accountTemplateService.partialUpdate(accountTemplate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountTemplate.getId().toString())
        );
    }

    /**
     * {@code GET  /account-templates} : get all the accountTemplates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccountTemplate>> getAllAccountTemplates(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AccountTemplates");
        Page<AccountTemplate> page = accountTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-templates/:id} : get the "id" accountTemplate.
     *
     * @param id the id of the accountTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountTemplate> getAccountTemplate(@PathVariable("id") UUID id) {
        log.debug("REST request to get AccountTemplate : {}", id);
        Optional<AccountTemplate> accountTemplate = accountTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountTemplate);
    }

    /**
     * {@code DELETE  /account-templates/:id} : delete the "id" accountTemplate.
     *
     * @param id the id of the accountTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountTemplate(@PathVariable("id") UUID id) {
        log.debug("REST request to delete AccountTemplate : {}", id);
        accountTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /account-templates/_search?query=:query} : search for the accountTemplate corresponding
     * to the query.
     *
     * @param query the query of the accountTemplate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AccountTemplate>> searchAccountTemplates(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AccountTemplates for query {}", query);
        try {
            Page<AccountTemplate> page = accountTemplateService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
