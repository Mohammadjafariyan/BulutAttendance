package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.RecordStatusAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.RecordStatus;
import com.bulut.attendance.repository.RecordStatusRepository;
import com.bulut.attendance.repository.search.RecordStatusSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link RecordStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecordStatusResourceIT {

    private static final ZonedDateTime DEFAULT_FROM_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FROM_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TO_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TO_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/record-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/record-statuses/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecordStatusRepository recordStatusRepository;

    @Autowired
    private RecordStatusSearchRepository recordStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecordStatusMockMvc;

    private RecordStatus recordStatus;

    private RecordStatus insertedRecordStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordStatus createEntity(EntityManager em) {
        RecordStatus recordStatus = new RecordStatus()
            .fromDateTime(DEFAULT_FROM_DATE_TIME)
            .toDateTime(DEFAULT_TO_DATE_TIME)
            .isDeleted(DEFAULT_IS_DELETED);
        return recordStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordStatus createUpdatedEntity(EntityManager em) {
        RecordStatus recordStatus = new RecordStatus()
            .fromDateTime(UPDATED_FROM_DATE_TIME)
            .toDateTime(UPDATED_TO_DATE_TIME)
            .isDeleted(UPDATED_IS_DELETED);
        return recordStatus;
    }

    @BeforeEach
    public void initTest() {
        recordStatus = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRecordStatus != null) {
            recordStatusRepository.delete(insertedRecordStatus);
            recordStatusSearchRepository.delete(insertedRecordStatus);
            insertedRecordStatus = null;
        }
    }

    @Test
    @Transactional
    void createRecordStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        // Create the RecordStatus
        var returnedRecordStatus = om.readValue(
            restRecordStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordStatus)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecordStatus.class
        );

        // Validate the RecordStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRecordStatusUpdatableFieldsEquals(returnedRecordStatus, getPersistedRecordStatus(returnedRecordStatus));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedRecordStatus = returnedRecordStatus;
    }

    @Test
    @Transactional
    void createRecordStatusWithExistingId() throws Exception {
        // Create the RecordStatus with an existing ID
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordStatus)))
            .andExpect(status().isBadRequest());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllRecordStatuses() throws Exception {
        // Initialize the database
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);

        // Get all the recordStatusList
        restRecordStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recordStatus.getId().toString())))
            .andExpect(jsonPath("$.[*].fromDateTime").value(hasItem(sameInstant(DEFAULT_FROM_DATE_TIME))))
            .andExpect(jsonPath("$.[*].toDateTime").value(hasItem(sameInstant(DEFAULT_TO_DATE_TIME))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getRecordStatus() throws Exception {
        // Initialize the database
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);

        // Get the recordStatus
        restRecordStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, recordStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recordStatus.getId().toString()))
            .andExpect(jsonPath("$.fromDateTime").value(sameInstant(DEFAULT_FROM_DATE_TIME)))
            .andExpect(jsonPath("$.toDateTime").value(sameInstant(DEFAULT_TO_DATE_TIME)))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRecordStatus() throws Exception {
        // Get the recordStatus
        restRecordStatusMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecordStatus() throws Exception {
        // Initialize the database
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        recordStatusSearchRepository.save(recordStatus);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());

        // Update the recordStatus
        RecordStatus updatedRecordStatus = recordStatusRepository.findById(recordStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecordStatus are not directly saved in db
        em.detach(updatedRecordStatus);
        updatedRecordStatus.fromDateTime(UPDATED_FROM_DATE_TIME).toDateTime(UPDATED_TO_DATE_TIME).isDeleted(UPDATED_IS_DELETED);

        restRecordStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecordStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRecordStatus))
            )
            .andExpect(status().isOk());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecordStatusToMatchAllProperties(updatedRecordStatus);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<RecordStatus> recordStatusSearchList = Streamable.of(recordStatusSearchRepository.findAll()).toList();
                RecordStatus testRecordStatusSearch = recordStatusSearchList.get(searchDatabaseSizeAfter - 1);

                assertRecordStatusAllPropertiesEquals(testRecordStatusSearch, updatedRecordStatus);
            });
    }

    @Test
    @Transactional
    void putNonExistingRecordStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        recordStatus.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recordStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecordStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        recordStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recordStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecordStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        recordStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recordStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateRecordStatusWithPatch() throws Exception {
        // Initialize the database
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recordStatus using partial update
        RecordStatus partialUpdatedRecordStatus = new RecordStatus();
        partialUpdatedRecordStatus.setId(recordStatus.getId());

        partialUpdatedRecordStatus.fromDateTime(UPDATED_FROM_DATE_TIME).toDateTime(UPDATED_TO_DATE_TIME).isDeleted(UPDATED_IS_DELETED);

        restRecordStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecordStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecordStatus))
            )
            .andExpect(status().isOk());

        // Validate the RecordStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecordStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecordStatus, recordStatus),
            getPersistedRecordStatus(recordStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecordStatusWithPatch() throws Exception {
        // Initialize the database
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recordStatus using partial update
        RecordStatus partialUpdatedRecordStatus = new RecordStatus();
        partialUpdatedRecordStatus.setId(recordStatus.getId());

        partialUpdatedRecordStatus.fromDateTime(UPDATED_FROM_DATE_TIME).toDateTime(UPDATED_TO_DATE_TIME).isDeleted(UPDATED_IS_DELETED);

        restRecordStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecordStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecordStatus))
            )
            .andExpect(status().isOk());

        // Validate the RecordStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecordStatusUpdatableFieldsEquals(partialUpdatedRecordStatus, getPersistedRecordStatus(partialUpdatedRecordStatus));
    }

    @Test
    @Transactional
    void patchNonExistingRecordStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        recordStatus.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recordStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recordStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecordStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        recordStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recordStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecordStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        recordStatus.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recordStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecordStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteRecordStatus() throws Exception {
        // Initialize the database
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);
        recordStatusRepository.save(recordStatus);
        recordStatusSearchRepository.save(recordStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the recordStatus
        restRecordStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, recordStatus.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(recordStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchRecordStatus() throws Exception {
        // Initialize the database
        insertedRecordStatus = recordStatusRepository.saveAndFlush(recordStatus);
        recordStatusSearchRepository.save(recordStatus);

        // Search the recordStatus
        restRecordStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + recordStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recordStatus.getId().toString())))
            .andExpect(jsonPath("$.[*].fromDateTime").value(hasItem(sameInstant(DEFAULT_FROM_DATE_TIME))))
            .andExpect(jsonPath("$.[*].toDateTime").value(hasItem(sameInstant(DEFAULT_TO_DATE_TIME))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    protected long getRepositoryCount() {
        return recordStatusRepository.count();
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

    protected RecordStatus getPersistedRecordStatus(RecordStatus recordStatus) {
        return recordStatusRepository.findById(recordStatus.getId()).orElseThrow();
    }

    protected void assertPersistedRecordStatusToMatchAllProperties(RecordStatus expectedRecordStatus) {
        assertRecordStatusAllPropertiesEquals(expectedRecordStatus, getPersistedRecordStatus(expectedRecordStatus));
    }

    protected void assertPersistedRecordStatusToMatchUpdatableProperties(RecordStatus expectedRecordStatus) {
        assertRecordStatusAllUpdatablePropertiesEquals(expectedRecordStatus, getPersistedRecordStatus(expectedRecordStatus));
    }
}
