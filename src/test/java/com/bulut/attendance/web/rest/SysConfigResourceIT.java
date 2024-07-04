package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.SysConfigAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.SysConfig;
import com.bulut.attendance.repository.SysConfigRepository;
import com.bulut.attendance.repository.search.SysConfigSearchRepository;
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
 * Integration tests for the {@link SysConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysConfigResourceIT {

    private static final String DEFAULT_TAX_FORMULA = "AAAAAAAAAA";
    private static final String UPDATED_TAX_FORMULA = "BBBBBBBBBB";

    private static final String DEFAULT_SANAVAT_FORMULA = "AAAAAAAAAA";
    private static final String UPDATED_SANAVAT_FORMULA = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/sys-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/sys-configs/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Autowired
    private SysConfigSearchRepository sysConfigSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSysConfigMockMvc;

    private SysConfig sysConfig;

    private SysConfig insertedSysConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysConfig createEntity(EntityManager em) {
        SysConfig sysConfig = new SysConfig().taxFormula(DEFAULT_TAX_FORMULA).sanavatFormula(DEFAULT_SANAVAT_FORMULA).year(DEFAULT_YEAR);
        return sysConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysConfig createUpdatedEntity(EntityManager em) {
        SysConfig sysConfig = new SysConfig().taxFormula(UPDATED_TAX_FORMULA).sanavatFormula(UPDATED_SANAVAT_FORMULA).year(UPDATED_YEAR);
        return sysConfig;
    }

    @BeforeEach
    public void initTest() {
        sysConfig = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSysConfig != null) {
            sysConfigRepository.delete(insertedSysConfig);
            sysConfigSearchRepository.delete(insertedSysConfig);
            insertedSysConfig = null;
        }
    }

    @Test
    @Transactional
    void createSysConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        // Create the SysConfig
        var returnedSysConfig = om.readValue(
            restSysConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysConfig)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SysConfig.class
        );

        // Validate the SysConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSysConfigUpdatableFieldsEquals(returnedSysConfig, getPersistedSysConfig(returnedSysConfig));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSysConfig = returnedSysConfig;
    }

    @Test
    @Transactional
    void createSysConfigWithExistingId() throws Exception {
        // Create the SysConfig with an existing ID
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysConfig)))
            .andExpect(status().isBadRequest());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSysConfigs() throws Exception {
        // Initialize the database
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);

        // Get all the sysConfigList
        restSysConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysConfig.getId().toString())))
            .andExpect(jsonPath("$.[*].taxFormula").value(hasItem(DEFAULT_TAX_FORMULA)))
            .andExpect(jsonPath("$.[*].sanavatFormula").value(hasItem(DEFAULT_SANAVAT_FORMULA)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getSysConfig() throws Exception {
        // Initialize the database
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);

        // Get the sysConfig
        restSysConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, sysConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysConfig.getId().toString()))
            .andExpect(jsonPath("$.taxFormula").value(DEFAULT_TAX_FORMULA))
            .andExpect(jsonPath("$.sanavatFormula").value(DEFAULT_SANAVAT_FORMULA))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingSysConfig() throws Exception {
        // Get the sysConfig
        restSysConfigMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSysConfig() throws Exception {
        // Initialize the database
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysConfigSearchRepository.save(sysConfig);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());

        // Update the sysConfig
        SysConfig updatedSysConfig = sysConfigRepository.findById(sysConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSysConfig are not directly saved in db
        em.detach(updatedSysConfig);
        updatedSysConfig.taxFormula(UPDATED_TAX_FORMULA).sanavatFormula(UPDATED_SANAVAT_FORMULA).year(UPDATED_YEAR);

        restSysConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSysConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSysConfig))
            )
            .andExpect(status().isOk());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSysConfigToMatchAllProperties(updatedSysConfig);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SysConfig> sysConfigSearchList = Streamable.of(sysConfigSearchRepository.findAll()).toList();
                SysConfig testSysConfigSearch = sysConfigSearchList.get(searchDatabaseSizeAfter - 1);

                assertSysConfigAllPropertiesEquals(testSysConfigSearch, updatedSysConfig);
            });
    }

    @Test
    @Transactional
    void putNonExistingSysConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        sysConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysConfig.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        sysConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        sysConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSysConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysConfig using partial update
        SysConfig partialUpdatedSysConfig = new SysConfig();
        partialUpdatedSysConfig.setId(sysConfig.getId());

        partialUpdatedSysConfig.sanavatFormula(UPDATED_SANAVAT_FORMULA);

        restSysConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysConfig))
            )
            .andExpect(status().isOk());

        // Validate the SysConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSysConfig, sysConfig),
            getPersistedSysConfig(sysConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateSysConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysConfig using partial update
        SysConfig partialUpdatedSysConfig = new SysConfig();
        partialUpdatedSysConfig.setId(sysConfig.getId());

        partialUpdatedSysConfig.taxFormula(UPDATED_TAX_FORMULA).sanavatFormula(UPDATED_SANAVAT_FORMULA).year(UPDATED_YEAR);

        restSysConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysConfig))
            )
            .andExpect(status().isOk());

        // Validate the SysConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysConfigUpdatableFieldsEquals(partialUpdatedSysConfig, getPersistedSysConfig(partialUpdatedSysConfig));
    }

    @Test
    @Transactional
    void patchNonExistingSysConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        sysConfig.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        sysConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        sysConfig.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sysConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSysConfig() throws Exception {
        // Initialize the database
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);
        sysConfigRepository.save(sysConfig);
        sysConfigSearchRepository.save(sysConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the sysConfig
        restSysConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysConfig.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sysConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSysConfig() throws Exception {
        // Initialize the database
        insertedSysConfig = sysConfigRepository.saveAndFlush(sysConfig);
        sysConfigSearchRepository.save(sysConfig);

        // Search the sysConfig
        restSysConfigMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sysConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysConfig.getId().toString())))
            .andExpect(jsonPath("$.[*].taxFormula").value(hasItem(DEFAULT_TAX_FORMULA)))
            .andExpect(jsonPath("$.[*].sanavatFormula").value(hasItem(DEFAULT_SANAVAT_FORMULA)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    protected long getRepositoryCount() {
        return sysConfigRepository.count();
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

    protected SysConfig getPersistedSysConfig(SysConfig sysConfig) {
        return sysConfigRepository.findById(sysConfig.getId()).orElseThrow();
    }

    protected void assertPersistedSysConfigToMatchAllProperties(SysConfig expectedSysConfig) {
        assertSysConfigAllPropertiesEquals(expectedSysConfig, getPersistedSysConfig(expectedSysConfig));
    }

    protected void assertPersistedSysConfigToMatchUpdatableProperties(SysConfig expectedSysConfig) {
        assertSysConfigAllUpdatablePropertiesEquals(expectedSysConfig, getPersistedSysConfig(expectedSysConfig));
    }
}
