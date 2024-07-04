package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.LeaveAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.Leave;
import com.bulut.attendance.repository.LeaveRepository;
import com.bulut.attendance.repository.search.LeaveSearchRepository;
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
 * Integration tests for the {@link LeaveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeaveResourceIT {

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_BPMS_APPROVE_STATUS = 1;
    private static final Integer UPDATED_BPMS_APPROVE_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/leaves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leaves/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private LeaveSearchRepository leaveSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveMockMvc;

    private Leave leave;

    private Leave insertedLeave;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leave createEntity(EntityManager em) {
        Leave leave = new Leave().start(DEFAULT_START).end(DEFAULT_END).bpmsApproveStatus(DEFAULT_BPMS_APPROVE_STATUS);
        return leave;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leave createUpdatedEntity(EntityManager em) {
        Leave leave = new Leave().start(UPDATED_START).end(UPDATED_END).bpmsApproveStatus(UPDATED_BPMS_APPROVE_STATUS);
        return leave;
    }

    @BeforeEach
    public void initTest() {
        leave = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLeave != null) {
            leaveRepository.delete(insertedLeave);
            leaveSearchRepository.delete(insertedLeave);
            insertedLeave = null;
        }
    }

    @Test
    @Transactional
    void createLeave() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        // Create the Leave
        var returnedLeave = om.readValue(
            restLeaveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leave)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Leave.class
        );

        // Validate the Leave in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLeaveUpdatableFieldsEquals(returnedLeave, getPersistedLeave(returnedLeave));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedLeave = returnedLeave;
    }

    @Test
    @Transactional
    void createLeaveWithExistingId() throws Exception {
        // Create the Leave with an existing ID
        insertedLeave = leaveRepository.saveAndFlush(leave);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leave)))
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllLeaves() throws Exception {
        // Initialize the database
        insertedLeave = leaveRepository.saveAndFlush(leave);

        // Get all the leaveList
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leave.getId().toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(sameInstant(DEFAULT_START))))
            .andExpect(jsonPath("$.[*].end").value(hasItem(sameInstant(DEFAULT_END))))
            .andExpect(jsonPath("$.[*].bpmsApproveStatus").value(hasItem(DEFAULT_BPMS_APPROVE_STATUS)));
    }

    @Test
    @Transactional
    void getLeave() throws Exception {
        // Initialize the database
        insertedLeave = leaveRepository.saveAndFlush(leave);

        // Get the leave
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL_ID, leave.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leave.getId().toString()))
            .andExpect(jsonPath("$.start").value(sameInstant(DEFAULT_START)))
            .andExpect(jsonPath("$.end").value(sameInstant(DEFAULT_END)))
            .andExpect(jsonPath("$.bpmsApproveStatus").value(DEFAULT_BPMS_APPROVE_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingLeave() throws Exception {
        // Get the leave
        restLeaveMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeave() throws Exception {
        // Initialize the database
        insertedLeave = leaveRepository.saveAndFlush(leave);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        leaveSearchRepository.save(leave);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());

        // Update the leave
        Leave updatedLeave = leaveRepository.findById(leave.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLeave are not directly saved in db
        em.detach(updatedLeave);
        updatedLeave.start(UPDATED_START).end(UPDATED_END).bpmsApproveStatus(UPDATED_BPMS_APPROVE_STATUS);

        restLeaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLeave.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLeave))
            )
            .andExpect(status().isOk());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLeaveToMatchAllProperties(updatedLeave);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Leave> leaveSearchList = Streamable.of(leaveSearchRepository.findAll()).toList();
                Leave testLeaveSearch = leaveSearchList.get(searchDatabaseSizeAfter - 1);

                assertLeaveAllPropertiesEquals(testLeaveSearch, updatedLeave);
            });
    }

    @Test
    @Transactional
    void putNonExistingLeave() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        leave.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(put(ENTITY_API_URL_ID, leave.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leave)))
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeave() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        leave.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leave)))
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeave() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        leave.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(leave)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateLeaveWithPatch() throws Exception {
        // Initialize the database
        insertedLeave = leaveRepository.saveAndFlush(leave);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the leave using partial update
        Leave partialUpdatedLeave = new Leave();
        partialUpdatedLeave.setId(leave.getId());

        partialUpdatedLeave.end(UPDATED_END).bpmsApproveStatus(UPDATED_BPMS_APPROVE_STATUS);

        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeave.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLeave))
            )
            .andExpect(status().isOk());

        // Validate the Leave in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeaveUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLeave, leave), getPersistedLeave(leave));
    }

    @Test
    @Transactional
    void fullUpdateLeaveWithPatch() throws Exception {
        // Initialize the database
        insertedLeave = leaveRepository.saveAndFlush(leave);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the leave using partial update
        Leave partialUpdatedLeave = new Leave();
        partialUpdatedLeave.setId(leave.getId());

        partialUpdatedLeave.start(UPDATED_START).end(UPDATED_END).bpmsApproveStatus(UPDATED_BPMS_APPROVE_STATUS);

        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeave.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLeave))
            )
            .andExpect(status().isOk());

        // Validate the Leave in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeaveUpdatableFieldsEquals(partialUpdatedLeave, getPersistedLeave(partialUpdatedLeave));
    }

    @Test
    @Transactional
    void patchNonExistingLeave() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        leave.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leave.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(leave))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeave() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        leave.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(leave))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeave() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        leave.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(leave)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Leave in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteLeave() throws Exception {
        // Initialize the database
        insertedLeave = leaveRepository.saveAndFlush(leave);
        leaveRepository.save(leave);
        leaveSearchRepository.save(leave);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the leave
        restLeaveMockMvc
            .perform(delete(ENTITY_API_URL_ID, leave.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(leaveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchLeave() throws Exception {
        // Initialize the database
        insertedLeave = leaveRepository.saveAndFlush(leave);
        leaveSearchRepository.save(leave);

        // Search the leave
        restLeaveMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leave.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leave.getId().toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(sameInstant(DEFAULT_START))))
            .andExpect(jsonPath("$.[*].end").value(hasItem(sameInstant(DEFAULT_END))))
            .andExpect(jsonPath("$.[*].bpmsApproveStatus").value(hasItem(DEFAULT_BPMS_APPROVE_STATUS)));
    }

    protected long getRepositoryCount() {
        return leaveRepository.count();
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

    protected Leave getPersistedLeave(Leave leave) {
        return leaveRepository.findById(leave.getId()).orElseThrow();
    }

    protected void assertPersistedLeaveToMatchAllProperties(Leave expectedLeave) {
        assertLeaveAllPropertiesEquals(expectedLeave, getPersistedLeave(expectedLeave));
    }

    protected void assertPersistedLeaveToMatchUpdatableProperties(Leave expectedLeave) {
        assertLeaveAllUpdatablePropertiesEquals(expectedLeave, getPersistedLeave(expectedLeave));
    }
}
