package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.PersonnelStatusAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.PersonnelStatus;
import com.bulut.attendance.repository.PersonnelStatusRepository;
import com.bulut.attendance.repository.search.PersonnelStatusSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonnelStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonnelStatusResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personnel-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/personnel-statuses/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonnelStatusRepository personnelStatusRepository;

    @Autowired
    private PersonnelStatusSearchRepository personnelStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonnelStatusMockMvc;

    private PersonnelStatus personnelStatus;

    private PersonnelStatus insertedPersonnelStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonnelStatus createEntity(EntityManager em) {
        PersonnelStatus personnelStatus = new PersonnelStatus().title(DEFAULT_TITLE);
        return personnelStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonnelStatus createUpdatedEntity(EntityManager em) {
        PersonnelStatus personnelStatus = new PersonnelStatus().title(UPDATED_TITLE);
        return personnelStatus;
    }

    @BeforeEach
    public void initTest() {
        personnelStatus = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPersonnelStatus != null) {
            personnelStatusRepository.delete(insertedPersonnelStatus);
            personnelStatusSearchRepository.delete(insertedPersonnelStatus);
            insertedPersonnelStatus = null;
        }
    }

    @Test
    @Transactional
    void createPersonnelStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        // Create the PersonnelStatus
        var returnedPersonnelStatus = om.readValue(
            restPersonnelStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnelStatus)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonnelStatus.class
        );

        // Validate the PersonnelStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPersonnelStatusUpdatableFieldsEquals(returnedPersonnelStatus, getPersistedPersonnelStatus(returnedPersonnelStatus));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPersonnelStatus = returnedPersonnelStatus;
    }

    @Test
    @Transactional
    void createPersonnelStatusWithExistingId() throws Exception {
        // Create the PersonnelStatus with an existing ID
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnelStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnelStatus)))
            .andExpect(status().isBadRequest());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPersonnelStatuses() throws Exception {
        // Initialize the database
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);

        // Get all the personnelStatusList
        restPersonnelStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnelStatus.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getPersonnelStatus() throws Exception {
        // Initialize the database
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);

        // Get the personnelStatus
        restPersonnelStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, personnelStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personnelStatus.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingPersonnelStatus() throws Exception {
        // Get the personnelStatus
        restPersonnelStatusMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonnelStatus() throws Exception {
        // Initialize the database
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        personnelStatusSearchRepository.save(personnelStatus);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());

        // Update the personnelStatus
        PersonnelStatus updatedPersonnelStatus = personnelStatusRepository.findById(personnelStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonnelStatus are not directly saved in db
        em.detach(updatedPersonnelStatus);
        updatedPersonnelStatus.title(UPDATED_TITLE);

        restPersonnelStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonnelStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPersonnelStatus))
            )
            .andExpect(status().isOk());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonnelStatusToMatchAllProperties(updatedPersonnelStatus);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PersonnelStatus> personnelStatusSearchList = Streamable.of(personnelStatusSearchRepository.findAll()).toList();
                PersonnelStatus testPersonnelStatusSearch = personnelStatusSearchList.get(searchDatabaseSizeAfter - 1);

                assertPersonnelStatusAllPropertiesEquals(testPersonnelStatusSearch, updatedPersonnelStatus);
            });
    }

    @Test
    @Transactional
    void putNonExistingPersonnelStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        personnelStatus.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personnelStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personnelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonnelStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        personnelStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personnelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonnelStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        personnelStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnelStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePersonnelStatusWithPatch() throws Exception {
        // Initialize the database
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personnelStatus using partial update
        PersonnelStatus partialUpdatedPersonnelStatus = new PersonnelStatus();
        partialUpdatedPersonnelStatus.setId(personnelStatus.getId());

        partialUpdatedPersonnelStatus.title(UPDATED_TITLE);

        restPersonnelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnelStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonnelStatus))
            )
            .andExpect(status().isOk());

        // Validate the PersonnelStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonnelStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonnelStatus, personnelStatus),
            getPersistedPersonnelStatus(personnelStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonnelStatusWithPatch() throws Exception {
        // Initialize the database
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personnelStatus using partial update
        PersonnelStatus partialUpdatedPersonnelStatus = new PersonnelStatus();
        partialUpdatedPersonnelStatus.setId(personnelStatus.getId());

        partialUpdatedPersonnelStatus.title(UPDATED_TITLE);

        restPersonnelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnelStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonnelStatus))
            )
            .andExpect(status().isOk());

        // Validate the PersonnelStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonnelStatusUpdatableFieldsEquals(
            partialUpdatedPersonnelStatus,
            getPersistedPersonnelStatus(partialUpdatedPersonnelStatus)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPersonnelStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        personnelStatus.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personnelStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personnelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonnelStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        personnelStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personnelStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonnelStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        personnelStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personnelStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonnelStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePersonnelStatus() throws Exception {
        // Initialize the database
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);
        personnelStatusRepository.save(personnelStatus);
        personnelStatusSearchRepository.save(personnelStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the personnelStatus
        restPersonnelStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, personnelStatus.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPersonnelStatus() throws Exception {
        // Initialize the database
        insertedPersonnelStatus = personnelStatusRepository.saveAndFlush(personnelStatus);
        personnelStatusSearchRepository.save(personnelStatus);

        // Search the personnelStatus
        restPersonnelStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + personnelStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnelStatus.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    protected long getRepositoryCount() {
        return personnelStatusRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PersonnelStatus getPersistedPersonnelStatus(PersonnelStatus personnelStatus) {
        return personnelStatusRepository.findById(personnelStatus.getId()).orElseThrow();
    }

    protected void assertPersistedPersonnelStatusToMatchAllProperties(PersonnelStatus expectedPersonnelStatus) {
        assertPersonnelStatusAllPropertiesEquals(expectedPersonnelStatus, getPersistedPersonnelStatus(expectedPersonnelStatus));
    }

    protected void assertPersistedPersonnelStatusToMatchUpdatableProperties(PersonnelStatus expectedPersonnelStatus) {
        assertPersonnelStatusAllUpdatablePropertiesEquals(expectedPersonnelStatus, getPersistedPersonnelStatus(expectedPersonnelStatus));
    }
}
