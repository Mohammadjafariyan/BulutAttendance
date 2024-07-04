package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.WorkItemAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.WorkItem;
import com.bulut.attendance.repository.WorkItemRepository;
import com.bulut.attendance.repository.search.WorkItemSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link WorkItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkItemResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/work-items/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkItemRepository workItemRepository;

    @Autowired
    private WorkItemSearchRepository workItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkItemMockMvc;

    private WorkItem workItem;

    private WorkItem insertedWorkItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkItem createEntity(EntityManager em) {
        WorkItem workItem = new WorkItem().amount(DEFAULT_AMOUNT);
        return workItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkItem createUpdatedEntity(EntityManager em) {
        WorkItem workItem = new WorkItem().amount(UPDATED_AMOUNT);
        return workItem;
    }

    @BeforeEach
    public void initTest() {
        workItem = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedWorkItem != null) {
            workItemRepository.delete(insertedWorkItem);
            workItemSearchRepository.delete(insertedWorkItem);
            insertedWorkItem = null;
        }
    }

    @Test
    @Transactional
    void createWorkItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        // Create the WorkItem
        var returnedWorkItem = om.readValue(
            restWorkItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkItem.class
        );

        // Validate the WorkItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWorkItemUpdatableFieldsEquals(returnedWorkItem, getPersistedWorkItem(returnedWorkItem));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWorkItem = returnedWorkItem;
    }

    @Test
    @Transactional
    void createWorkItemWithExistingId() throws Exception {
        // Create the WorkItem with an existing ID
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workItem)))
            .andExpect(status().isBadRequest());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWorkItems() throws Exception {
        // Initialize the database
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);

        // Get all the workItemList
        restWorkItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workItem.getId().toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @Test
    @Transactional
    void getWorkItem() throws Exception {
        // Initialize the database
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);

        // Get the workItem
        restWorkItemMockMvc
            .perform(get(ENTITY_API_URL_ID, workItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workItem.getId().toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingWorkItem() throws Exception {
        // Get the workItem
        restWorkItemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkItem() throws Exception {
        // Initialize the database
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        workItemSearchRepository.save(workItem);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());

        // Update the workItem
        WorkItem updatedWorkItem = workItemRepository.findById(workItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkItem are not directly saved in db
        em.detach(updatedWorkItem);
        updatedWorkItem.amount(UPDATED_AMOUNT);

        restWorkItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWorkItem))
            )
            .andExpect(status().isOk());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkItemToMatchAllProperties(updatedWorkItem);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<WorkItem> workItemSearchList = Streamable.of(workItemSearchRepository.findAll()).toList();
                WorkItem testWorkItemSearch = workItemSearchList.get(searchDatabaseSizeAfter - 1);

                assertWorkItemAllPropertiesEquals(testWorkItemSearch, updatedWorkItem);
            });
    }

    @Test
    @Transactional
    void putNonExistingWorkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        workItem.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        workItem.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        workItem.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWorkItemWithPatch() throws Exception {
        // Initialize the database
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workItem using partial update
        WorkItem partialUpdatedWorkItem = new WorkItem();
        partialUpdatedWorkItem.setId(workItem.getId());

        partialUpdatedWorkItem.amount(UPDATED_AMOUNT);

        restWorkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkItem))
            )
            .andExpect(status().isOk());

        // Validate the WorkItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWorkItem, workItem), getPersistedWorkItem(workItem));
    }

    @Test
    @Transactional
    void fullUpdateWorkItemWithPatch() throws Exception {
        // Initialize the database
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workItem using partial update
        WorkItem partialUpdatedWorkItem = new WorkItem();
        partialUpdatedWorkItem.setId(workItem.getId());

        partialUpdatedWorkItem.amount(UPDATED_AMOUNT);

        restWorkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkItem))
            )
            .andExpect(status().isOk());

        // Validate the WorkItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkItemUpdatableFieldsEquals(partialUpdatedWorkItem, getPersistedWorkItem(partialUpdatedWorkItem));
    }

    @Test
    @Transactional
    void patchNonExistingWorkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        workItem.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        workItem.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        workItem.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWorkItem() throws Exception {
        // Initialize the database
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);
        workItemRepository.save(workItem);
        workItemSearchRepository.save(workItem);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the workItem
        restWorkItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, workItem.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWorkItem() throws Exception {
        // Initialize the database
        insertedWorkItem = workItemRepository.saveAndFlush(workItem);
        workItemSearchRepository.save(workItem);

        // Search the workItem
        restWorkItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workItem.getId().toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    protected long getRepositoryCount() {
        return workItemRepository.count();
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

    protected WorkItem getPersistedWorkItem(WorkItem workItem) {
        return workItemRepository.findById(workItem.getId()).orElseThrow();
    }

    protected void assertPersistedWorkItemToMatchAllProperties(WorkItem expectedWorkItem) {
        assertWorkItemAllPropertiesEquals(expectedWorkItem, getPersistedWorkItem(expectedWorkItem));
    }

    protected void assertPersistedWorkItemToMatchUpdatableProperties(WorkItem expectedWorkItem) {
        assertWorkItemAllUpdatablePropertiesEquals(expectedWorkItem, getPersistedWorkItem(expectedWorkItem));
    }
}
