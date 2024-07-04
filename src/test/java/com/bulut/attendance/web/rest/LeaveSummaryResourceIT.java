package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.LeaveSummaryAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.LeaveSummary;
import com.bulut.attendance.repository.LeaveSummaryRepository;
import com.bulut.attendance.repository.search.LeaveSummarySearchRepository;
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
 * Integration tests for the {@link LeaveSummaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeaveSummaryResourceIT {

    private static final Integer DEFAULT_REMAIN_HOURS = 1;
    private static final Integer UPDATED_REMAIN_HOURS = 2;

    private static final Integer DEFAULT_REMAIN_DAYS = 1;
    private static final Integer UPDATED_REMAIN_DAYS = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/leave-summaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leave-summaries/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LeaveSummaryRepository leaveSummaryRepository;

    @Autowired
    private LeaveSummarySearchRepository leaveSummarySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveSummaryMockMvc;

    private LeaveSummary leaveSummary;

    private LeaveSummary insertedLeaveSummary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveSummary createEntity(EntityManager em) {
        LeaveSummary leaveSummary = new LeaveSummary().remainHours(DEFAULT_REMAIN_HOURS).remainDays(DEFAULT_REMAIN_DAYS).year(DEFAULT_YEAR);
        return leaveSummary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveSummary createUpdatedEntity(EntityManager em) {
        LeaveSummary leaveSummary = new LeaveSummary().remainHours(UPDATED_REMAIN_HOURS).remainDays(UPDATED_REMAIN_DAYS).year(UPDATED_YEAR);
        return leaveSummary;
    }

    @BeforeEach
    public void initTest() {
        leaveSummary = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLeaveSummary != null) {
            leaveSummaryRepository.delete(insertedLeaveSummary);
            leaveSummarySearchRepository.delete(insertedLeaveSummary);
            insertedLeaveSummary = null;
        }
    }

    @Test
    @Transactional
    void createLeaveSummary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        // Create the LeaveSummary
        var returnedLeaveSummary = om.readValue(
            restLeaveSummaryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leaveSummary)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LeaveSummary.class
        );

        // Validate the LeaveSummary in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLeaveSummaryUpdatableFieldsEquals(returnedLeaveSummary, getPersistedLeaveSummary(returnedLeaveSummary));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedLeaveSummary = returnedLeaveSummary;
    }

    @Test
    @Transactional
    void createLeaveSummaryWithExistingId() throws Exception {
        // Create the LeaveSummary with an existing ID
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveSummaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leaveSummary)))
            .andExpect(status().isBadRequest());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllLeaveSummaries() throws Exception {
        // Initialize the database
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);

        // Get all the leaveSummaryList
        restLeaveSummaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveSummary.getId().toString())))
            .andExpect(jsonPath("$.[*].remainHours").value(hasItem(DEFAULT_REMAIN_HOURS)))
            .andExpect(jsonPath("$.[*].remainDays").value(hasItem(DEFAULT_REMAIN_DAYS)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getLeaveSummary() throws Exception {
        // Initialize the database
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);

        // Get the leaveSummary
        restLeaveSummaryMockMvc
            .perform(get(ENTITY_API_URL_ID, leaveSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveSummary.getId().toString()))
            .andExpect(jsonPath("$.remainHours").value(DEFAULT_REMAIN_HOURS))
            .andExpect(jsonPath("$.remainDays").value(DEFAULT_REMAIN_DAYS))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingLeaveSummary() throws Exception {
        // Get the leaveSummary
        restLeaveSummaryMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeaveSummary() throws Exception {
        // Initialize the database
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        leaveSummarySearchRepository.save(leaveSummary);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());

        // Update the leaveSummary
        LeaveSummary updatedLeaveSummary = leaveSummaryRepository.findById(leaveSummary.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLeaveSummary are not directly saved in db
        em.detach(updatedLeaveSummary);
        updatedLeaveSummary.remainHours(UPDATED_REMAIN_HOURS).remainDays(UPDATED_REMAIN_DAYS).year(UPDATED_YEAR);

        restLeaveSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLeaveSummary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLeaveSummary))
            )
            .andExpect(status().isOk());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLeaveSummaryToMatchAllProperties(updatedLeaveSummary);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<LeaveSummary> leaveSummarySearchList = Streamable.of(leaveSummarySearchRepository.findAll()).toList();
                LeaveSummary testLeaveSummarySearch = leaveSummarySearchList.get(searchDatabaseSizeAfter - 1);

                assertLeaveSummaryAllPropertiesEquals(testLeaveSummarySearch, updatedLeaveSummary);
            });
    }

    @Test
    @Transactional
    void putNonExistingLeaveSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        leaveSummary.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveSummary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(leaveSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaveSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        leaveSummary.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(leaveSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaveSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        leaveSummary.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveSummaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leaveSummary)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateLeaveSummaryWithPatch() throws Exception {
        // Initialize the database
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the leaveSummary using partial update
        LeaveSummary partialUpdatedLeaveSummary = new LeaveSummary();
        partialUpdatedLeaveSummary.setId(leaveSummary.getId());

        partialUpdatedLeaveSummary.year(UPDATED_YEAR);

        restLeaveSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLeaveSummary))
            )
            .andExpect(status().isOk());

        // Validate the LeaveSummary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeaveSummaryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLeaveSummary, leaveSummary),
            getPersistedLeaveSummary(leaveSummary)
        );
    }

    @Test
    @Transactional
    void fullUpdateLeaveSummaryWithPatch() throws Exception {
        // Initialize the database
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the leaveSummary using partial update
        LeaveSummary partialUpdatedLeaveSummary = new LeaveSummary();
        partialUpdatedLeaveSummary.setId(leaveSummary.getId());

        partialUpdatedLeaveSummary.remainHours(UPDATED_REMAIN_HOURS).remainDays(UPDATED_REMAIN_DAYS).year(UPDATED_YEAR);

        restLeaveSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLeaveSummary))
            )
            .andExpect(status().isOk());

        // Validate the LeaveSummary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeaveSummaryUpdatableFieldsEquals(partialUpdatedLeaveSummary, getPersistedLeaveSummary(partialUpdatedLeaveSummary));
    }

    @Test
    @Transactional
    void patchNonExistingLeaveSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        leaveSummary.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaveSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(leaveSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaveSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        leaveSummary.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(leaveSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaveSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        leaveSummary.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveSummaryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(leaveSummary)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteLeaveSummary() throws Exception {
        // Initialize the database
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);
        leaveSummaryRepository.save(leaveSummary);
        leaveSummarySearchRepository.save(leaveSummary);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the leaveSummary
        restLeaveSummaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaveSummary.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSummarySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchLeaveSummary() throws Exception {
        // Initialize the database
        insertedLeaveSummary = leaveSummaryRepository.saveAndFlush(leaveSummary);
        leaveSummarySearchRepository.save(leaveSummary);

        // Search the leaveSummary
        restLeaveSummaryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaveSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveSummary.getId().toString())))
            .andExpect(jsonPath("$.[*].remainHours").value(hasItem(DEFAULT_REMAIN_HOURS)))
            .andExpect(jsonPath("$.[*].remainDays").value(hasItem(DEFAULT_REMAIN_DAYS)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    protected long getRepositoryCount() {
        return leaveSummaryRepository.count();
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

    protected LeaveSummary getPersistedLeaveSummary(LeaveSummary leaveSummary) {
        return leaveSummaryRepository.findById(leaveSummary.getId()).orElseThrow();
    }

    protected void assertPersistedLeaveSummaryToMatchAllProperties(LeaveSummary expectedLeaveSummary) {
        assertLeaveSummaryAllPropertiesEquals(expectedLeaveSummary, getPersistedLeaveSummary(expectedLeaveSummary));
    }

    protected void assertPersistedLeaveSummaryToMatchUpdatableProperties(LeaveSummary expectedLeaveSummary) {
        assertLeaveSummaryAllUpdatablePropertiesEquals(expectedLeaveSummary, getPersistedLeaveSummary(expectedLeaveSummary));
    }
}
