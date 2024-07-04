package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.WorkAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.Work;
import com.bulut.attendance.repository.WorkRepository;
import com.bulut.attendance.repository.search.WorkSearchRepository;
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
 * Integration tests for the {@link WorkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkResourceIT {

    private static final ZonedDateTime DEFAULT_ISSUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ISSUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final String ENTITY_API_URL = "/api/works";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/works/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private WorkSearchRepository workSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkMockMvc;

    private Work work;

    private Work insertedWork;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Work createEntity(EntityManager em) {
        Work work = new Work().issueDate(DEFAULT_ISSUE_DATE).desc(DEFAULT_DESC).year(DEFAULT_YEAR).month(DEFAULT_MONTH);
        return work;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Work createUpdatedEntity(EntityManager em) {
        Work work = new Work().issueDate(UPDATED_ISSUE_DATE).desc(UPDATED_DESC).year(UPDATED_YEAR).month(UPDATED_MONTH);
        return work;
    }

    @BeforeEach
    public void initTest() {
        work = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedWork != null) {
            workRepository.delete(insertedWork);
            workSearchRepository.delete(insertedWork);
            insertedWork = null;
        }
    }

    @Test
    @Transactional
    void createWork() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        // Create the Work
        var returnedWork = om.readValue(
            restWorkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(work)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Work.class
        );

        // Validate the Work in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWorkUpdatableFieldsEquals(returnedWork, getPersistedWork(returnedWork));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWork = returnedWork;
    }

    @Test
    @Transactional
    void createWorkWithExistingId() throws Exception {
        // Create the Work with an existing ID
        insertedWork = workRepository.saveAndFlush(work);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(work)))
            .andExpect(status().isBadRequest());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWorks() throws Exception {
        // Initialize the database
        insertedWork = workRepository.saveAndFlush(work);

        // Get all the workList
        restWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(work.getId().toString())))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(sameInstant(DEFAULT_ISSUE_DATE))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    void getWork() throws Exception {
        // Initialize the database
        insertedWork = workRepository.saveAndFlush(work);

        // Get the work
        restWorkMockMvc
            .perform(get(ENTITY_API_URL_ID, work.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(work.getId().toString()))
            .andExpect(jsonPath("$.issueDate").value(sameInstant(DEFAULT_ISSUE_DATE)))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    void getNonExistingWork() throws Exception {
        // Get the work
        restWorkMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWork() throws Exception {
        // Initialize the database
        insertedWork = workRepository.saveAndFlush(work);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        workSearchRepository.save(work);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());

        // Update the work
        Work updatedWork = workRepository.findById(work.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWork are not directly saved in db
        em.detach(updatedWork);
        updatedWork.issueDate(UPDATED_ISSUE_DATE).desc(UPDATED_DESC).year(UPDATED_YEAR).month(UPDATED_MONTH);

        restWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWork.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWork))
            )
            .andExpect(status().isOk());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkToMatchAllProperties(updatedWork);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Work> workSearchList = Streamable.of(workSearchRepository.findAll()).toList();
                Work testWorkSearch = workSearchList.get(searchDatabaseSizeAfter - 1);

                assertWorkAllPropertiesEquals(testWorkSearch, updatedWork);
            });
    }

    @Test
    @Transactional
    void putNonExistingWork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        work.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkMockMvc
            .perform(put(ENTITY_API_URL_ID, work.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(work)))
            .andExpect(status().isBadRequest());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        work.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkMockMvc
            .perform(put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(work)))
            .andExpect(status().isBadRequest());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        work.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(work)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWorkWithPatch() throws Exception {
        // Initialize the database
        insertedWork = workRepository.saveAndFlush(work);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the work using partial update
        Work partialUpdatedWork = new Work();
        partialUpdatedWork.setId(work.getId());

        partialUpdatedWork.issueDate(UPDATED_ISSUE_DATE);

        restWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWork))
            )
            .andExpect(status().isOk());

        // Validate the Work in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWork, work), getPersistedWork(work));
    }

    @Test
    @Transactional
    void fullUpdateWorkWithPatch() throws Exception {
        // Initialize the database
        insertedWork = workRepository.saveAndFlush(work);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the work using partial update
        Work partialUpdatedWork = new Work();
        partialUpdatedWork.setId(work.getId());

        partialUpdatedWork.issueDate(UPDATED_ISSUE_DATE).desc(UPDATED_DESC).year(UPDATED_YEAR).month(UPDATED_MONTH);

        restWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWork))
            )
            .andExpect(status().isOk());

        // Validate the Work in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkUpdatableFieldsEquals(partialUpdatedWork, getPersistedWork(partialUpdatedWork));
    }

    @Test
    @Transactional
    void patchNonExistingWork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        work.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkMockMvc
            .perform(patch(ENTITY_API_URL_ID, work.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(work)))
            .andExpect(status().isBadRequest());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        work.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(work))
            )
            .andExpect(status().isBadRequest());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        work.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(work)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Work in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWork() throws Exception {
        // Initialize the database
        insertedWork = workRepository.saveAndFlush(work);
        workRepository.save(work);
        workSearchRepository.save(work);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the work
        restWorkMockMvc
            .perform(delete(ENTITY_API_URL_ID, work.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWork() throws Exception {
        // Initialize the database
        insertedWork = workRepository.saveAndFlush(work);
        workSearchRepository.save(work);

        // Search the work
        restWorkMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + work.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(work.getId().toString())))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(sameInstant(DEFAULT_ISSUE_DATE))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    protected long getRepositoryCount() {
        return workRepository.count();
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

    protected Work getPersistedWork(Work work) {
        return workRepository.findById(work.getId()).orElseThrow();
    }

    protected void assertPersistedWorkToMatchAllProperties(Work expectedWork) {
        assertWorkAllPropertiesEquals(expectedWork, getPersistedWork(expectedWork));
    }

    protected void assertPersistedWorkToMatchUpdatableProperties(Work expectedWork) {
        assertWorkAllUpdatablePropertiesEquals(expectedWork, getPersistedWork(expectedWork));
    }
}
