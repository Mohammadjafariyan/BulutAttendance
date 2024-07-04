package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.OrgUnitAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.OrgUnit;
import com.bulut.attendance.repository.OrgUnitRepository;
import com.bulut.attendance.repository.search.OrgUnitSearchRepository;
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
 * Integration tests for the {@link OrgUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgUnitResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/org-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/org-units/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrgUnitRepository orgUnitRepository;

    @Autowired
    private OrgUnitSearchRepository orgUnitSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgUnitMockMvc;

    private OrgUnit orgUnit;

    private OrgUnit insertedOrgUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUnit createEntity(EntityManager em) {
        OrgUnit orgUnit = new OrgUnit().title(DEFAULT_TITLE);
        return orgUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgUnit createUpdatedEntity(EntityManager em) {
        OrgUnit orgUnit = new OrgUnit().title(UPDATED_TITLE);
        return orgUnit;
    }

    @BeforeEach
    public void initTest() {
        orgUnit = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrgUnit != null) {
            orgUnitRepository.delete(insertedOrgUnit);
            orgUnitSearchRepository.delete(insertedOrgUnit);
            insertedOrgUnit = null;
        }
    }

    @Test
    @Transactional
    void createOrgUnit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        // Create the OrgUnit
        var returnedOrgUnit = om.readValue(
            restOrgUnitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgUnit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrgUnit.class
        );

        // Validate the OrgUnit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrgUnitUpdatableFieldsEquals(returnedOrgUnit, getPersistedOrgUnit(returnedOrgUnit));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedOrgUnit = returnedOrgUnit;
    }

    @Test
    @Transactional
    void createOrgUnitWithExistingId() throws Exception {
        // Create the OrgUnit with an existing ID
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllOrgUnits() throws Exception {
        // Initialize the database
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);

        // Get all the orgUnitList
        restOrgUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnit.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getOrgUnit() throws Exception {
        // Initialize the database
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);

        // Get the orgUnit
        restOrgUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, orgUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgUnit.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingOrgUnit() throws Exception {
        // Get the orgUnit
        restOrgUnitMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrgUnit() throws Exception {
        // Initialize the database
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        orgUnitSearchRepository.save(orgUnit);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());

        // Update the orgUnit
        OrgUnit updatedOrgUnit = orgUnitRepository.findById(orgUnit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrgUnit are not directly saved in db
        em.detach(updatedOrgUnit);
        updatedOrgUnit.title(UPDATED_TITLE);

        restOrgUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrgUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrgUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrgUnitToMatchAllProperties(updatedOrgUnit);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OrgUnit> orgUnitSearchList = Streamable.of(orgUnitSearchRepository.findAll()).toList();
                OrgUnit testOrgUnitSearch = orgUnitSearchList.get(searchDatabaseSizeAfter - 1);

                assertOrgUnitAllPropertiesEquals(testOrgUnitSearch, updatedOrgUnit);
            });
    }

    @Test
    @Transactional
    void putNonExistingOrgUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        orgUnit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(put(ENTITY_API_URL_ID, orgUnit.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        orgUnit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        orgUnit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orgUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateOrgUnitWithPatch() throws Exception {
        // Initialize the database
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orgUnit using partial update
        OrgUnit partialUpdatedOrgUnit = new OrgUnit();
        partialUpdatedOrgUnit.setId(orgUnit.getId());

        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrgUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrgUnit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrgUnitUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOrgUnit, orgUnit), getPersistedOrgUnit(orgUnit));
    }

    @Test
    @Transactional
    void fullUpdateOrgUnitWithPatch() throws Exception {
        // Initialize the database
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orgUnit using partial update
        OrgUnit partialUpdatedOrgUnit = new OrgUnit();
        partialUpdatedOrgUnit.setId(orgUnit.getId());

        partialUpdatedOrgUnit.title(UPDATED_TITLE);

        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrgUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrgUnit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrgUnitUpdatableFieldsEquals(partialUpdatedOrgUnit, getPersistedOrgUnit(partialUpdatedOrgUnit));
    }

    @Test
    @Transactional
    void patchNonExistingOrgUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        orgUnit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgUnit.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orgUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        orgUnit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orgUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        orgUnit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgUnitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orgUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteOrgUnit() throws Exception {
        // Initialize the database
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);
        orgUnitRepository.save(orgUnit);
        orgUnitSearchRepository.save(orgUnit);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the orgUnit
        restOrgUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgUnit.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orgUnitSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchOrgUnit() throws Exception {
        // Initialize the database
        insertedOrgUnit = orgUnitRepository.saveAndFlush(orgUnit);
        orgUnitSearchRepository.save(orgUnit);

        // Search the orgUnit
        restOrgUnitMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + orgUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgUnit.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    protected long getRepositoryCount() {
        return orgUnitRepository.count();
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

    protected OrgUnit getPersistedOrgUnit(OrgUnit orgUnit) {
        return orgUnitRepository.findById(orgUnit.getId()).orElseThrow();
    }

    protected void assertPersistedOrgUnitToMatchAllProperties(OrgUnit expectedOrgUnit) {
        assertOrgUnitAllPropertiesEquals(expectedOrgUnit, getPersistedOrgUnit(expectedOrgUnit));
    }

    protected void assertPersistedOrgUnitToMatchUpdatableProperties(OrgUnit expectedOrgUnit) {
        assertOrgUnitAllUpdatablePropertiesEquals(expectedOrgUnit, getPersistedOrgUnit(expectedOrgUnit));
    }
}
