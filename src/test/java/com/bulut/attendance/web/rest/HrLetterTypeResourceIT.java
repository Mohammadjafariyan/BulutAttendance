package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.HrLetterTypeAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.HrLetterType;
import com.bulut.attendance.repository.HrLetterTypeRepository;
import com.bulut.attendance.repository.search.HrLetterTypeSearchRepository;
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
 * Integration tests for the {@link HrLetterTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HrLetterTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hr-letter-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/hr-letter-types/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HrLetterTypeRepository hrLetterTypeRepository;

    @Autowired
    private HrLetterTypeSearchRepository hrLetterTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHrLetterTypeMockMvc;

    private HrLetterType hrLetterType;

    private HrLetterType insertedHrLetterType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrLetterType createEntity(EntityManager em) {
        HrLetterType hrLetterType = new HrLetterType().title(DEFAULT_TITLE);
        return hrLetterType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrLetterType createUpdatedEntity(EntityManager em) {
        HrLetterType hrLetterType = new HrLetterType().title(UPDATED_TITLE);
        return hrLetterType;
    }

    @BeforeEach
    public void initTest() {
        hrLetterType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHrLetterType != null) {
            hrLetterTypeRepository.delete(insertedHrLetterType);
            hrLetterTypeSearchRepository.delete(insertedHrLetterType);
            insertedHrLetterType = null;
        }
    }

    @Test
    @Transactional
    void createHrLetterType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        // Create the HrLetterType
        var returnedHrLetterType = om.readValue(
            restHrLetterTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetterType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HrLetterType.class
        );

        // Validate the HrLetterType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHrLetterTypeUpdatableFieldsEquals(returnedHrLetterType, getPersistedHrLetterType(returnedHrLetterType));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedHrLetterType = returnedHrLetterType;
    }

    @Test
    @Transactional
    void createHrLetterTypeWithExistingId() throws Exception {
        // Create the HrLetterType with an existing ID
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHrLetterTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetterType)))
            .andExpect(status().isBadRequest());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllHrLetterTypes() throws Exception {
        // Initialize the database
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);

        // Get all the hrLetterTypeList
        restHrLetterTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hrLetterType.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getHrLetterType() throws Exception {
        // Initialize the database
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);

        // Get the hrLetterType
        restHrLetterTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, hrLetterType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hrLetterType.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingHrLetterType() throws Exception {
        // Get the hrLetterType
        restHrLetterTypeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHrLetterType() throws Exception {
        // Initialize the database
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        hrLetterTypeSearchRepository.save(hrLetterType);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());

        // Update the hrLetterType
        HrLetterType updatedHrLetterType = hrLetterTypeRepository.findById(hrLetterType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHrLetterType are not directly saved in db
        em.detach(updatedHrLetterType);
        updatedHrLetterType.title(UPDATED_TITLE);

        restHrLetterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHrLetterType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHrLetterType))
            )
            .andExpect(status().isOk());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHrLetterTypeToMatchAllProperties(updatedHrLetterType);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HrLetterType> hrLetterTypeSearchList = Streamable.of(hrLetterTypeSearchRepository.findAll()).toList();
                HrLetterType testHrLetterTypeSearch = hrLetterTypeSearchList.get(searchDatabaseSizeAfter - 1);

                assertHrLetterTypeAllPropertiesEquals(testHrLetterTypeSearch, updatedHrLetterType);
            });
    }

    @Test
    @Transactional
    void putNonExistingHrLetterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        hrLetterType.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHrLetterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hrLetterType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hrLetterType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchHrLetterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        hrLetterType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hrLetterType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHrLetterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        hrLetterType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetterType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateHrLetterTypeWithPatch() throws Exception {
        // Initialize the database
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hrLetterType using partial update
        HrLetterType partialUpdatedHrLetterType = new HrLetterType();
        partialUpdatedHrLetterType.setId(hrLetterType.getId());

        partialUpdatedHrLetterType.title(UPDATED_TITLE);

        restHrLetterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHrLetterType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHrLetterType))
            )
            .andExpect(status().isOk());

        // Validate the HrLetterType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHrLetterTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHrLetterType, hrLetterType),
            getPersistedHrLetterType(hrLetterType)
        );
    }

    @Test
    @Transactional
    void fullUpdateHrLetterTypeWithPatch() throws Exception {
        // Initialize the database
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hrLetterType using partial update
        HrLetterType partialUpdatedHrLetterType = new HrLetterType();
        partialUpdatedHrLetterType.setId(hrLetterType.getId());

        partialUpdatedHrLetterType.title(UPDATED_TITLE);

        restHrLetterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHrLetterType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHrLetterType))
            )
            .andExpect(status().isOk());

        // Validate the HrLetterType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHrLetterTypeUpdatableFieldsEquals(partialUpdatedHrLetterType, getPersistedHrLetterType(partialUpdatedHrLetterType));
    }

    @Test
    @Transactional
    void patchNonExistingHrLetterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        hrLetterType.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHrLetterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hrLetterType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hrLetterType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHrLetterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        hrLetterType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hrLetterType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHrLetterType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        hrLetterType.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hrLetterType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HrLetterType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteHrLetterType() throws Exception {
        // Initialize the database
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);
        hrLetterTypeRepository.save(hrLetterType);
        hrLetterTypeSearchRepository.save(hrLetterType);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the hrLetterType
        restHrLetterTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, hrLetterType.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchHrLetterType() throws Exception {
        // Initialize the database
        insertedHrLetterType = hrLetterTypeRepository.saveAndFlush(hrLetterType);
        hrLetterTypeSearchRepository.save(hrLetterType);

        // Search the hrLetterType
        restHrLetterTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + hrLetterType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hrLetterType.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    protected long getRepositoryCount() {
        return hrLetterTypeRepository.count();
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

    protected HrLetterType getPersistedHrLetterType(HrLetterType hrLetterType) {
        return hrLetterTypeRepository.findById(hrLetterType.getId()).orElseThrow();
    }

    protected void assertPersistedHrLetterTypeToMatchAllProperties(HrLetterType expectedHrLetterType) {
        assertHrLetterTypeAllPropertiesEquals(expectedHrLetterType, getPersistedHrLetterType(expectedHrLetterType));
    }

    protected void assertPersistedHrLetterTypeToMatchUpdatableProperties(HrLetterType expectedHrLetterType) {
        assertHrLetterTypeAllUpdatablePropertiesEquals(expectedHrLetterType, getPersistedHrLetterType(expectedHrLetterType));
    }
}
