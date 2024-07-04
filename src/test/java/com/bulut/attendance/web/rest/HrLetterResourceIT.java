package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.HrLetterAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.HrLetter;
import com.bulut.attendance.repository.HrLetterRepository;
import com.bulut.attendance.repository.search.HrLetterSearchRepository;
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
 * Integration tests for the {@link HrLetterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HrLetterResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIQUE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_UNIQUE_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ISSUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ISSUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXECUTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXECUTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_BPMS_APPROVE_STATUS = 1;
    private static final Integer UPDATED_BPMS_APPROVE_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/hr-letters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/hr-letters/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HrLetterRepository hrLetterRepository;

    @Autowired
    private HrLetterSearchRepository hrLetterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHrLetterMockMvc;

    private HrLetter hrLetter;

    private HrLetter insertedHrLetter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrLetter createEntity(EntityManager em) {
        HrLetter hrLetter = new HrLetter()
            .title(DEFAULT_TITLE)
            .uniqueNumber(DEFAULT_UNIQUE_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .executionDate(DEFAULT_EXECUTION_DATE)
            .bpmsApproveStatus(DEFAULT_BPMS_APPROVE_STATUS);
        return hrLetter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrLetter createUpdatedEntity(EntityManager em) {
        HrLetter hrLetter = new HrLetter()
            .title(UPDATED_TITLE)
            .uniqueNumber(UPDATED_UNIQUE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .executionDate(UPDATED_EXECUTION_DATE)
            .bpmsApproveStatus(UPDATED_BPMS_APPROVE_STATUS);
        return hrLetter;
    }

    @BeforeEach
    public void initTest() {
        hrLetter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHrLetter != null) {
            hrLetterRepository.delete(insertedHrLetter);
            hrLetterSearchRepository.delete(insertedHrLetter);
            insertedHrLetter = null;
        }
    }

    @Test
    @Transactional
    void createHrLetter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        // Create the HrLetter
        var returnedHrLetter = om.readValue(
            restHrLetterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetter)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HrLetter.class
        );

        // Validate the HrLetter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHrLetterUpdatableFieldsEquals(returnedHrLetter, getPersistedHrLetter(returnedHrLetter));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedHrLetter = returnedHrLetter;
    }

    @Test
    @Transactional
    void createHrLetterWithExistingId() throws Exception {
        // Create the HrLetter with an existing ID
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHrLetterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetter)))
            .andExpect(status().isBadRequest());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllHrLetters() throws Exception {
        // Initialize the database
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);

        // Get all the hrLetterList
        restHrLetterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hrLetter.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].uniqueNumber").value(hasItem(DEFAULT_UNIQUE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(sameInstant(DEFAULT_ISSUE_DATE))))
            .andExpect(jsonPath("$.[*].executionDate").value(hasItem(sameInstant(DEFAULT_EXECUTION_DATE))))
            .andExpect(jsonPath("$.[*].bpmsApproveStatus").value(hasItem(DEFAULT_BPMS_APPROVE_STATUS)));
    }

    @Test
    @Transactional
    void getHrLetter() throws Exception {
        // Initialize the database
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);

        // Get the hrLetter
        restHrLetterMockMvc
            .perform(get(ENTITY_API_URL_ID, hrLetter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hrLetter.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.uniqueNumber").value(DEFAULT_UNIQUE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(sameInstant(DEFAULT_ISSUE_DATE)))
            .andExpect(jsonPath("$.executionDate").value(sameInstant(DEFAULT_EXECUTION_DATE)))
            .andExpect(jsonPath("$.bpmsApproveStatus").value(DEFAULT_BPMS_APPROVE_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingHrLetter() throws Exception {
        // Get the hrLetter
        restHrLetterMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHrLetter() throws Exception {
        // Initialize the database
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        hrLetterSearchRepository.save(hrLetter);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());

        // Update the hrLetter
        HrLetter updatedHrLetter = hrLetterRepository.findById(hrLetter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHrLetter are not directly saved in db
        em.detach(updatedHrLetter);
        updatedHrLetter
            .title(UPDATED_TITLE)
            .uniqueNumber(UPDATED_UNIQUE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .executionDate(UPDATED_EXECUTION_DATE)
            .bpmsApproveStatus(UPDATED_BPMS_APPROVE_STATUS);

        restHrLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHrLetter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHrLetter))
            )
            .andExpect(status().isOk());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHrLetterToMatchAllProperties(updatedHrLetter);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HrLetter> hrLetterSearchList = Streamable.of(hrLetterSearchRepository.findAll()).toList();
                HrLetter testHrLetterSearch = hrLetterSearchList.get(searchDatabaseSizeAfter - 1);

                assertHrLetterAllPropertiesEquals(testHrLetterSearch, updatedHrLetter);
            });
    }

    @Test
    @Transactional
    void putNonExistingHrLetter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        hrLetter.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHrLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hrLetter.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchHrLetter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        hrLetter.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHrLetter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        hrLetter.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateHrLetterWithPatch() throws Exception {
        // Initialize the database
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hrLetter using partial update
        HrLetter partialUpdatedHrLetter = new HrLetter();
        partialUpdatedHrLetter.setId(hrLetter.getId());

        partialUpdatedHrLetter.executionDate(UPDATED_EXECUTION_DATE);

        restHrLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHrLetter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHrLetter))
            )
            .andExpect(status().isOk());

        // Validate the HrLetter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHrLetterUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHrLetter, hrLetter), getPersistedHrLetter(hrLetter));
    }

    @Test
    @Transactional
    void fullUpdateHrLetterWithPatch() throws Exception {
        // Initialize the database
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hrLetter using partial update
        HrLetter partialUpdatedHrLetter = new HrLetter();
        partialUpdatedHrLetter.setId(hrLetter.getId());

        partialUpdatedHrLetter
            .title(UPDATED_TITLE)
            .uniqueNumber(UPDATED_UNIQUE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .executionDate(UPDATED_EXECUTION_DATE)
            .bpmsApproveStatus(UPDATED_BPMS_APPROVE_STATUS);

        restHrLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHrLetter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHrLetter))
            )
            .andExpect(status().isOk());

        // Validate the HrLetter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHrLetterUpdatableFieldsEquals(partialUpdatedHrLetter, getPersistedHrLetter(partialUpdatedHrLetter));
    }

    @Test
    @Transactional
    void patchNonExistingHrLetter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        hrLetter.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHrLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hrLetter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hrLetter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHrLetter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        hrLetter.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hrLetter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHrLetter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        hrLetter.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hrLetter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HrLetter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteHrLetter() throws Exception {
        // Initialize the database
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);
        hrLetterRepository.save(hrLetter);
        hrLetterSearchRepository.save(hrLetter);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the hrLetter
        restHrLetterMockMvc
            .perform(delete(ENTITY_API_URL_ID, hrLetter.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchHrLetter() throws Exception {
        // Initialize the database
        insertedHrLetter = hrLetterRepository.saveAndFlush(hrLetter);
        hrLetterSearchRepository.save(hrLetter);

        // Search the hrLetter
        restHrLetterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + hrLetter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hrLetter.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].uniqueNumber").value(hasItem(DEFAULT_UNIQUE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(sameInstant(DEFAULT_ISSUE_DATE))))
            .andExpect(jsonPath("$.[*].executionDate").value(hasItem(sameInstant(DEFAULT_EXECUTION_DATE))))
            .andExpect(jsonPath("$.[*].bpmsApproveStatus").value(hasItem(DEFAULT_BPMS_APPROVE_STATUS)));
    }

    protected long getRepositoryCount() {
        return hrLetterRepository.count();
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

    protected HrLetter getPersistedHrLetter(HrLetter hrLetter) {
        return hrLetterRepository.findById(hrLetter.getId()).orElseThrow();
    }

    protected void assertPersistedHrLetterToMatchAllProperties(HrLetter expectedHrLetter) {
        assertHrLetterAllPropertiesEquals(expectedHrLetter, getPersistedHrLetter(expectedHrLetter));
    }

    protected void assertPersistedHrLetterToMatchUpdatableProperties(HrLetter expectedHrLetter) {
        assertHrLetterAllUpdatablePropertiesEquals(expectedHrLetter, getPersistedHrLetter(expectedHrLetter));
    }
}
