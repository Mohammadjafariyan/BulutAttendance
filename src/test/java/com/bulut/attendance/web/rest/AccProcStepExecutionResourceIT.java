package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.AccProcStepExecutionAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.AccProcStepExecution;
import com.bulut.attendance.repository.AccProcStepExecutionRepository;
import com.bulut.attendance.repository.search.AccProcStepExecutionSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link AccProcStepExecutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccProcStepExecutionResourceIT {

    private static final BigDecimal DEFAULT_DEBIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREDIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acc-proc-step-executions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/acc-proc-step-executions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccProcStepExecutionRepository accProcStepExecutionRepository;

    @Autowired
    private AccProcStepExecutionSearchRepository accProcStepExecutionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccProcStepExecutionMockMvc;

    private AccProcStepExecution accProcStepExecution;

    private AccProcStepExecution insertedAccProcStepExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccProcStepExecution createEntity(EntityManager em) {
        AccProcStepExecution accProcStepExecution = new AccProcStepExecution()
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .desc(DEFAULT_DESC);
        return accProcStepExecution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccProcStepExecution createUpdatedEntity(EntityManager em) {
        AccProcStepExecution accProcStepExecution = new AccProcStepExecution()
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .desc(UPDATED_DESC);
        return accProcStepExecution;
    }

    @BeforeEach
    public void initTest() {
        accProcStepExecution = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccProcStepExecution != null) {
            accProcStepExecutionRepository.delete(insertedAccProcStepExecution);
            accProcStepExecutionSearchRepository.delete(insertedAccProcStepExecution);
            insertedAccProcStepExecution = null;
        }
    }

    @Test
    @Transactional
    void createAccProcStepExecution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        // Create the AccProcStepExecution
        var returnedAccProcStepExecution = om.readValue(
            restAccProcStepExecutionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProcStepExecution)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccProcStepExecution.class
        );

        // Validate the AccProcStepExecution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccProcStepExecutionUpdatableFieldsEquals(
            returnedAccProcStepExecution,
            getPersistedAccProcStepExecution(returnedAccProcStepExecution)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccProcStepExecution = returnedAccProcStepExecution;
    }

    @Test
    @Transactional
    void createAccProcStepExecutionWithExistingId() throws Exception {
        // Create the AccProcStepExecution with an existing ID
        accProcStepExecution.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccProcStepExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProcStepExecution)))
            .andExpect(status().isBadRequest());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccProcStepExecutions() throws Exception {
        // Initialize the database
        insertedAccProcStepExecution = accProcStepExecutionRepository.saveAndFlush(accProcStepExecution);

        // Get all the accProcStepExecutionList
        restAccProcStepExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accProcStepExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(sameNumber(DEFAULT_DEBIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)));
    }

    @Test
    @Transactional
    void getAccProcStepExecution() throws Exception {
        // Initialize the database
        insertedAccProcStepExecution = accProcStepExecutionRepository.saveAndFlush(accProcStepExecution);

        // Get the accProcStepExecution
        restAccProcStepExecutionMockMvc
            .perform(get(ENTITY_API_URL_ID, accProcStepExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accProcStepExecution.getId().intValue()))
            .andExpect(jsonPath("$.debitAmount").value(sameNumber(DEFAULT_DEBIT_AMOUNT)))
            .andExpect(jsonPath("$.creditAmount").value(sameNumber(DEFAULT_CREDIT_AMOUNT)))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC));
    }

    @Test
    @Transactional
    void getNonExistingAccProcStepExecution() throws Exception {
        // Get the accProcStepExecution
        restAccProcStepExecutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccProcStepExecution() throws Exception {
        // Initialize the database
        insertedAccProcStepExecution = accProcStepExecutionRepository.saveAndFlush(accProcStepExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accProcStepExecutionSearchRepository.save(accProcStepExecution);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());

        // Update the accProcStepExecution
        AccProcStepExecution updatedAccProcStepExecution = accProcStepExecutionRepository
            .findById(accProcStepExecution.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAccProcStepExecution are not directly saved in db
        em.detach(updatedAccProcStepExecution);
        updatedAccProcStepExecution.debitAmount(UPDATED_DEBIT_AMOUNT).creditAmount(UPDATED_CREDIT_AMOUNT).desc(UPDATED_DESC);

        restAccProcStepExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccProcStepExecution.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccProcStepExecution))
            )
            .andExpect(status().isOk());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccProcStepExecutionToMatchAllProperties(updatedAccProcStepExecution);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccProcStepExecution> accProcStepExecutionSearchList = Streamable.of(
                    accProcStepExecutionSearchRepository.findAll()
                ).toList();
                AccProcStepExecution testAccProcStepExecutionSearch = accProcStepExecutionSearchList.get(searchDatabaseSizeAfter - 1);

                assertAccProcStepExecutionAllPropertiesEquals(testAccProcStepExecutionSearch, updatedAccProcStepExecution);
            });
    }

    @Test
    @Transactional
    void putNonExistingAccProcStepExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        accProcStepExecution.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccProcStepExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accProcStepExecution.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accProcStepExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccProcStepExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        accProcStepExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accProcStepExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccProcStepExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        accProcStepExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepExecutionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProcStepExecution)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccProcStepExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedAccProcStepExecution = accProcStepExecutionRepository.saveAndFlush(accProcStepExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accProcStepExecution using partial update
        AccProcStepExecution partialUpdatedAccProcStepExecution = new AccProcStepExecution();
        partialUpdatedAccProcStepExecution.setId(accProcStepExecution.getId());

        partialUpdatedAccProcStepExecution.creditAmount(UPDATED_CREDIT_AMOUNT);

        restAccProcStepExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccProcStepExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccProcStepExecution))
            )
            .andExpect(status().isOk());

        // Validate the AccProcStepExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccProcStepExecutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccProcStepExecution, accProcStepExecution),
            getPersistedAccProcStepExecution(accProcStepExecution)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccProcStepExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedAccProcStepExecution = accProcStepExecutionRepository.saveAndFlush(accProcStepExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accProcStepExecution using partial update
        AccProcStepExecution partialUpdatedAccProcStepExecution = new AccProcStepExecution();
        partialUpdatedAccProcStepExecution.setId(accProcStepExecution.getId());

        partialUpdatedAccProcStepExecution.debitAmount(UPDATED_DEBIT_AMOUNT).creditAmount(UPDATED_CREDIT_AMOUNT).desc(UPDATED_DESC);

        restAccProcStepExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccProcStepExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccProcStepExecution))
            )
            .andExpect(status().isOk());

        // Validate the AccProcStepExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccProcStepExecutionUpdatableFieldsEquals(
            partialUpdatedAccProcStepExecution,
            getPersistedAccProcStepExecution(partialUpdatedAccProcStepExecution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccProcStepExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        accProcStepExecution.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccProcStepExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accProcStepExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accProcStepExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccProcStepExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        accProcStepExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accProcStepExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccProcStepExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        accProcStepExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepExecutionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accProcStepExecution)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccProcStepExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccProcStepExecution() throws Exception {
        // Initialize the database
        insertedAccProcStepExecution = accProcStepExecutionRepository.saveAndFlush(accProcStepExecution);
        accProcStepExecutionRepository.save(accProcStepExecution);
        accProcStepExecutionSearchRepository.save(accProcStepExecution);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accProcStepExecution
        restAccProcStepExecutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, accProcStepExecution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccProcStepExecution() throws Exception {
        // Initialize the database
        insertedAccProcStepExecution = accProcStepExecutionRepository.saveAndFlush(accProcStepExecution);
        accProcStepExecutionSearchRepository.save(accProcStepExecution);

        // Search the accProcStepExecution
        restAccProcStepExecutionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accProcStepExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accProcStepExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(sameNumber(DEFAULT_DEBIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)));
    }

    protected long getRepositoryCount() {
        return accProcStepExecutionRepository.count();
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

    protected AccProcStepExecution getPersistedAccProcStepExecution(AccProcStepExecution accProcStepExecution) {
        return accProcStepExecutionRepository.findById(accProcStepExecution.getId()).orElseThrow();
    }

    protected void assertPersistedAccProcStepExecutionToMatchAllProperties(AccProcStepExecution expectedAccProcStepExecution) {
        assertAccProcStepExecutionAllPropertiesEquals(
            expectedAccProcStepExecution,
            getPersistedAccProcStepExecution(expectedAccProcStepExecution)
        );
    }

    protected void assertPersistedAccProcStepExecutionToMatchUpdatableProperties(AccProcStepExecution expectedAccProcStepExecution) {
        assertAccProcStepExecutionAllUpdatablePropertiesEquals(
            expectedAccProcStepExecution,
            getPersistedAccProcStepExecution(expectedAccProcStepExecution)
        );
    }
}
