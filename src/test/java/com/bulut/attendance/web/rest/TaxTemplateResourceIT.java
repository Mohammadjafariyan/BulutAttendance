package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.TaxTemplateAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.TaxTemplate;
import com.bulut.attendance.repository.TaxTemplateRepository;
import com.bulut.attendance.repository.search.TaxTemplateSearchRepository;
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
 * Integration tests for the {@link TaxTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxTemplateResourceIT {

    private static final BigDecimal DEFAULT_RANGE_FROM = new BigDecimal(1);
    private static final BigDecimal UPDATED_RANGE_FROM = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RANGE_TO = new BigDecimal(1);
    private static final BigDecimal UPDATED_RANGE_TO = new BigDecimal(2);

    private static final Integer DEFAULT_PERCENT = 1;
    private static final Integer UPDATED_PERCENT = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/tax-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tax-templates/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaxTemplateRepository taxTemplateRepository;

    @Autowired
    private TaxTemplateSearchRepository taxTemplateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxTemplateMockMvc;

    private TaxTemplate taxTemplate;

    private TaxTemplate insertedTaxTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxTemplate createEntity(EntityManager em) {
        TaxTemplate taxTemplate = new TaxTemplate()
            .rangeFrom(DEFAULT_RANGE_FROM)
            .rangeTo(DEFAULT_RANGE_TO)
            .percent(DEFAULT_PERCENT)
            .year(DEFAULT_YEAR);
        return taxTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxTemplate createUpdatedEntity(EntityManager em) {
        TaxTemplate taxTemplate = new TaxTemplate()
            .rangeFrom(UPDATED_RANGE_FROM)
            .rangeTo(UPDATED_RANGE_TO)
            .percent(UPDATED_PERCENT)
            .year(UPDATED_YEAR);
        return taxTemplate;
    }

    @BeforeEach
    public void initTest() {
        taxTemplate = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTaxTemplate != null) {
            taxTemplateRepository.delete(insertedTaxTemplate);
            taxTemplateSearchRepository.delete(insertedTaxTemplate);
            insertedTaxTemplate = null;
        }
    }

    @Test
    @Transactional
    void createTaxTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        // Create the TaxTemplate
        var returnedTaxTemplate = om.readValue(
            restTaxTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxTemplate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaxTemplate.class
        );

        // Validate the TaxTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTaxTemplateUpdatableFieldsEquals(returnedTaxTemplate, getPersistedTaxTemplate(returnedTaxTemplate));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTaxTemplate = returnedTaxTemplate;
    }

    @Test
    @Transactional
    void createTaxTemplateWithExistingId() throws Exception {
        // Create the TaxTemplate with an existing ID
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTaxTemplates() throws Exception {
        // Initialize the database
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);

        // Get all the taxTemplateList
        restTaxTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxTemplate.getId().toString())))
            .andExpect(jsonPath("$.[*].rangeFrom").value(hasItem(sameNumber(DEFAULT_RANGE_FROM))))
            .andExpect(jsonPath("$.[*].rangeTo").value(hasItem(sameNumber(DEFAULT_RANGE_TO))))
            .andExpect(jsonPath("$.[*].percent").value(hasItem(DEFAULT_PERCENT)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getTaxTemplate() throws Exception {
        // Initialize the database
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);

        // Get the taxTemplate
        restTaxTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, taxTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxTemplate.getId().toString()))
            .andExpect(jsonPath("$.rangeFrom").value(sameNumber(DEFAULT_RANGE_FROM)))
            .andExpect(jsonPath("$.rangeTo").value(sameNumber(DEFAULT_RANGE_TO)))
            .andExpect(jsonPath("$.percent").value(DEFAULT_PERCENT))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingTaxTemplate() throws Exception {
        // Get the taxTemplate
        restTaxTemplateMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxTemplate() throws Exception {
        // Initialize the database
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxTemplateSearchRepository.save(taxTemplate);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());

        // Update the taxTemplate
        TaxTemplate updatedTaxTemplate = taxTemplateRepository.findById(taxTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaxTemplate are not directly saved in db
        em.detach(updatedTaxTemplate);
        updatedTaxTemplate.rangeFrom(UPDATED_RANGE_FROM).rangeTo(UPDATED_RANGE_TO).percent(UPDATED_PERCENT).year(UPDATED_YEAR);

        restTaxTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTaxTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTaxTemplate))
            )
            .andExpect(status().isOk());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaxTemplateToMatchAllProperties(updatedTaxTemplate);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TaxTemplate> taxTemplateSearchList = Streamable.of(taxTemplateSearchRepository.findAll()).toList();
                TaxTemplate testTaxTemplateSearch = taxTemplateSearchList.get(searchDatabaseSizeAfter - 1);

                assertTaxTemplateAllPropertiesEquals(testTaxTemplateSearch, updatedTaxTemplate);
            });
    }

    @Test
    @Transactional
    void putNonExistingTaxTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        taxTemplate.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        taxTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        taxTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTaxTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxTemplate using partial update
        TaxTemplate partialUpdatedTaxTemplate = new TaxTemplate();
        partialUpdatedTaxTemplate.setId(taxTemplate.getId());

        partialUpdatedTaxTemplate.rangeFrom(UPDATED_RANGE_FROM);

        restTaxTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxTemplate))
            )
            .andExpect(status().isOk());

        // Validate the TaxTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTaxTemplate, taxTemplate),
            getPersistedTaxTemplate(taxTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateTaxTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxTemplate using partial update
        TaxTemplate partialUpdatedTaxTemplate = new TaxTemplate();
        partialUpdatedTaxTemplate.setId(taxTemplate.getId());

        partialUpdatedTaxTemplate.rangeFrom(UPDATED_RANGE_FROM).rangeTo(UPDATED_RANGE_TO).percent(UPDATED_PERCENT).year(UPDATED_YEAR);

        restTaxTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxTemplate))
            )
            .andExpect(status().isOk());

        // Validate the TaxTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxTemplateUpdatableFieldsEquals(partialUpdatedTaxTemplate, getPersistedTaxTemplate(partialUpdatedTaxTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingTaxTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        taxTemplate.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        taxTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        taxTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTaxTemplate() throws Exception {
        // Initialize the database
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);
        taxTemplateRepository.save(taxTemplate);
        taxTemplateSearchRepository.save(taxTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the taxTemplate
        restTaxTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxTemplate.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTaxTemplate() throws Exception {
        // Initialize the database
        insertedTaxTemplate = taxTemplateRepository.saveAndFlush(taxTemplate);
        taxTemplateSearchRepository.save(taxTemplate);

        // Search the taxTemplate
        restTaxTemplateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + taxTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxTemplate.getId().toString())))
            .andExpect(jsonPath("$.[*].rangeFrom").value(hasItem(sameNumber(DEFAULT_RANGE_FROM))))
            .andExpect(jsonPath("$.[*].rangeTo").value(hasItem(sameNumber(DEFAULT_RANGE_TO))))
            .andExpect(jsonPath("$.[*].percent").value(hasItem(DEFAULT_PERCENT)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    protected long getRepositoryCount() {
        return taxTemplateRepository.count();
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

    protected TaxTemplate getPersistedTaxTemplate(TaxTemplate taxTemplate) {
        return taxTemplateRepository.findById(taxTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedTaxTemplateToMatchAllProperties(TaxTemplate expectedTaxTemplate) {
        assertTaxTemplateAllPropertiesEquals(expectedTaxTemplate, getPersistedTaxTemplate(expectedTaxTemplate));
    }

    protected void assertPersistedTaxTemplateToMatchUpdatableProperties(TaxTemplate expectedTaxTemplate) {
        assertTaxTemplateAllUpdatablePropertiesEquals(expectedTaxTemplate, getPersistedTaxTemplate(expectedTaxTemplate));
    }
}
