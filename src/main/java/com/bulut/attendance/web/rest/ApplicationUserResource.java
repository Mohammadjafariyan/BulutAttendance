package com.bulut.attendance.web.rest;

import com.bulut.attendance.domain.ApplicationUser;
import com.bulut.attendance.repository.ApplicationUserRepository;
import com.bulut.attendance.service.ApplicationUserService;
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
 * REST controller for managing {@link com.bulut.attendance.domain.ApplicationUser}.
 */
@RestController
@RequestMapping("/api/application-users")
public class ApplicationUserResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserResource.class);

    private static final String ENTITY_NAME = "bulutAttendanceApplicationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationUserService applicationUserService;

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserResource(ApplicationUserService applicationUserService, ApplicationUserRepository applicationUserRepository) {
        this.applicationUserService = applicationUserService;
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * {@code POST  /application-users} : Create a new applicationUser.
     *
     * @param applicationUser the applicationUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationUser, or with status {@code 400 (Bad Request)} if the applicationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationUser> createApplicationUser(@RequestBody ApplicationUser applicationUser) throws URISyntaxException {
        log.debug("REST request to save ApplicationUser : {}", applicationUser);
        if (applicationUser.getId() != null) {
            throw new BadRequestAlertException("A new applicationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        applicationUser = applicationUserService.save(applicationUser);
        return ResponseEntity.created(new URI("/api/application-users/" + applicationUser.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, applicationUser.getId().toString()))
            .body(applicationUser);
    }

    /**
     * {@code PUT  /application-users/:id} : Updates an existing applicationUser.
     *
     * @param id the id of the applicationUser to save.
     * @param applicationUser the applicationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUser,
     * or with status {@code 400 (Bad Request)} if the applicationUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUser> updateApplicationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUser applicationUser
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationUser : {}, {}", id, applicationUser);
        if (applicationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        applicationUser = applicationUserService.update(applicationUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationUser.getId().toString()))
            .body(applicationUser);
    }

    /**
     * {@code PATCH  /application-users/:id} : Partial updates given fields of an existing applicationUser, field will ignore if it is null
     *
     * @param id the id of the applicationUser to save.
     * @param applicationUser the applicationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUser,
     * or with status {@code 400 (Bad Request)} if the applicationUser is not valid,
     * or with status {@code 404 (Not Found)} if the applicationUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationUser> partialUpdateApplicationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUser applicationUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationUser partially : {}, {}", id, applicationUser);
        if (applicationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationUser> result = applicationUserService.partialUpdate(applicationUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationUser.getId().toString())
        );
    }

    /**
     * {@code GET  /application-users} : get all the applicationUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationUsers in body.
     */
    @GetMapping("")
    public List<ApplicationUser> getAllApplicationUsers() {
        log.debug("REST request to get all ApplicationUsers");
        return applicationUserService.findAll();
    }

    /**
     * {@code GET  /application-users/:id} : get the "id" applicationUser.
     *
     * @param id the id of the applicationUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUser> getApplicationUser(@PathVariable("id") Long id) {
        log.debug("REST request to get ApplicationUser : {}", id);
        Optional<ApplicationUser> applicationUser = applicationUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationUser);
    }

    /**
     * {@code DELETE  /application-users/:id} : delete the "id" applicationUser.
     *
     * @param id the id of the applicationUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete ApplicationUser : {}", id);
        applicationUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /application-users/_search?query=:query} : search for the applicationUser corresponding
     * to the query.
     *
     * @param query the query of the applicationUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ApplicationUser> searchApplicationUsers(@RequestParam("query") String query) {
        log.debug("REST request to search ApplicationUsers for query {}", query);
        try {
            return applicationUserService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
