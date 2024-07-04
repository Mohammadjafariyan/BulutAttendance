package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.OrgPositionAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.OrgPosition;
import com.bulut.attendance.repository.OrgPositionRepository;
import com.bulut.attendance.repository.search.OrgPositionSearchRepository;
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
 * Integration tests for the {@link OrgPositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgPositionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/org-positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/org-positions/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrgPositionRepository orgPositionRepository;

    @Autowired
    private OrgPositionSearchRepository orgPositionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgPositionMockMvc;

    private OrgPosition orgPosition;

    private OrgPosition insertedOrgPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgPosition createEntity(EntityManager em) {
        OrgPosition orgPosition = new OrgPosition().title(DEFAULT_TITLE);
        return orgPosition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgPosition createUpdatedEntity(EntityManager em) {
        OrgPosition orgPosition = new OrgPosition().title(UPDATED_TITLE);
        return orgPosition;
    }

    @BeforeEach
    public void initTest() {
        orgPosition = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrgPosition != null) {
            orgPositionRepository.delete(insertedOrgPosition);
            orgPositionSearchRepository.delete(insertedOrgPosition);
            insertedOrgPosition = null;
        }
    }

    @Test
    @Transactional
    void createOrgPosition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        // Create the OrgPosition
        var returnedOrgPosition = om.readValue(
            restOrgPositionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgPosition)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrgPosition.class
        );

        // Validate the OrgPosition in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrgPositionUpdatableFieldsEquals(returnedOrgPosition, getPersistedOrgPosition(returnedOrgPosition));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedOrgPosition = returnedOrgPosition;
    }

    @Test
    @Transactional
    void createOrgPositionWithExistingId() throws Exception {
        // Create the OrgPosition with an existing ID
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgPosition)))
            .andExpect(status().isBadRequest());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllOrgPositions() throws Exception {
        // Initialize the database
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);

        // Get all the orgPositionList
        restOrgPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgPosition.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getOrgPosition() throws Exception {
        // Initialize the database
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);

        // Get the orgPosition
        restOrgPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, orgPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgPosition.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingOrgPosition() throws Exception {
        // Get the orgPosition
        restOrgPositionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrgPosition() throws Exception {
        // Initialize the database
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        orgPositionSearchRepository.save(orgPosition);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());

        // Update the orgPosition
        OrgPosition updatedOrgPosition = orgPositionRepository.findById(orgPosition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrgPosition are not directly saved in db
        em.detach(updatedOrgPosition);
        updatedOrgPosition.title(UPDATED_TITLE);

        restOrgPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrgPosition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrgPosition))
            )
            .andExpect(status().isOk());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrgPositionToMatchAllProperties(updatedOrgPosition);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OrgPosition> orgPositionSearchList = Streamable.of(orgPositionSearchRepository.findAll()).toList();
                OrgPosition testOrgPositionSearch = orgPositionSearchList.get(searchDatabaseSizeAfter - 1);

                assertOrgPositionAllPropertiesEquals(testOrgPositionSearch, updatedOrgPosition);
            });
    }

    @Test
    @Transactional
    void putNonExistingOrgPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        orgPosition.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgPosition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orgPosition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        orgPosition.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgPosition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        orgPosition.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPositionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgPosition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateOrgPositionWithPatch() throws Exception {
        // Initialize the database
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orgPosition using partial update
        OrgPosition partialUpdatedOrgPosition = new OrgPosition();
        partialUpdatedOrgPosition.setId(orgPosition.getId());

        restOrgPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrgPosition))
            )
            .andExpect(status().isOk());

        // Validate the OrgPosition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrgPositionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrgPosition, orgPosition),
            getPersistedOrgPosition(orgPosition)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrgPositionWithPatch() throws Exception {
        // Initialize the database
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orgPosition using partial update
        OrgPosition partialUpdatedOrgPosition = new OrgPosition();
        partialUpdatedOrgPosition.setId(orgPosition.getId());

        partialUpdatedOrgPosition.title(UPDATED_TITLE);

        restOrgPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrgPosition))
            )
            .andExpect(status().isOk());

        // Validate the OrgPosition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrgPositionUpdatableFieldsEquals(partialUpdatedOrgPosition, getPersistedOrgPosition(partialUpdatedOrgPosition));
    }

    @Test
    @Transactional
    void patchNonExistingOrgPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        orgPosition.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orgPosition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        orgPosition.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orgPosition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        orgPosition.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPositionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orgPosition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteOrgPosition() throws Exception {
        // Initialize the database
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);
        orgPositionRepository.save(orgPosition);
        orgPositionSearchRepository.save(orgPosition);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the orgPosition
        restOrgPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgPosition.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgPositionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchOrgPosition() throws Exception {
        // Initialize the database
        insertedOrgPosition = orgPositionRepository.saveAndFlush(orgPosition);
        orgPositionSearchRepository.save(orgPosition);

        // Search the orgPosition
        restOrgPositionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + orgPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgPosition.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    protected long getRepositoryCount() {
        return orgPositionRepository.count();
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

    protected OrgPosition getPersistedOrgPosition(OrgPosition orgPosition) {
        return orgPositionRepository.findById(orgPosition.getId()).orElseThrow();
    }

    protected void assertPersistedOrgPositionToMatchAllProperties(OrgPosition expectedOrgPosition) {
        assertOrgPositionAllPropertiesEquals(expectedOrgPosition, getPersistedOrgPosition(expectedOrgPosition));
    }

    protected void assertPersistedOrgPositionToMatchUpdatableProperties(OrgPosition expectedOrgPosition) {
        assertOrgPositionAllUpdatablePropertiesEquals(expectedOrgPosition, getPersistedOrgPosition(expectedOrgPosition));
    }
}
