package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.AccProcStepAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.AccProcStep;
import com.bulut.attendance.repository.AccProcStepRepository;
import com.bulut.attendance.repository.search.AccProcStepSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AccProcStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccProcStepResourceIT {

    private static final String ENTITY_API_URL = "/api/acc-proc-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/acc-proc-steps/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccProcStepRepository accProcStepRepository;

    @Autowired
    private AccProcStepSearchRepository accProcStepSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccProcStepMockMvc;

    private AccProcStep accProcStep;

    private AccProcStep insertedAccProcStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccProcStep createEntity(EntityManager em) {
        AccProcStep accProcStep = new AccProcStep();
        return accProcStep;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccProcStep createUpdatedEntity(EntityManager em) {
        AccProcStep accProcStep = new AccProcStep();
        return accProcStep;
    }

    @BeforeEach
    public void initTest() {
        accProcStep = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccProcStep != null) {
            accProcStepRepository.delete(insertedAccProcStep);
            accProcStepSearchRepository.delete(insertedAccProcStep);
            insertedAccProcStep = null;
        }
    }

    @Test
    @Transactional
    void createAccProcStep() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        // Create the AccProcStep
        var returnedAccProcStep = om.readValue(
            restAccProcStepMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProcStep)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccProcStep.class
        );

        // Validate the AccProcStep in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccProcStepUpdatableFieldsEquals(returnedAccProcStep, getPersistedAccProcStep(returnedAccProcStep));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccProcStep = returnedAccProcStep;
    }

    @Test
    @Transactional
    void createAccProcStepWithExistingId() throws Exception {
        // Create the AccProcStep with an existing ID
        accProcStep.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccProcStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProcStep)))
            .andExpect(status().isBadRequest());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccProcSteps() throws Exception {
        // Initialize the database
        insertedAccProcStep = accProcStepRepository.saveAndFlush(accProcStep);

        // Get all the accProcStepList
        restAccProcStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accProcStep.getId().intValue())));
    }

    @Test
    @Transactional
    void getAccProcStep() throws Exception {
        // Initialize the database
        insertedAccProcStep = accProcStepRepository.saveAndFlush(accProcStep);

        // Get the accProcStep
        restAccProcStepMockMvc
            .perform(get(ENTITY_API_URL_ID, accProcStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accProcStep.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAccProcStep() throws Exception {
        // Get the accProcStep
        restAccProcStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccProcStep() throws Exception {
        // Initialize the database
        insertedAccProcStep = accProcStepRepository.saveAndFlush(accProcStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accProcStepSearchRepository.save(accProcStep);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());

        // Update the accProcStep
        AccProcStep updatedAccProcStep = accProcStepRepository.findById(accProcStep.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccProcStep are not directly saved in db
        em.detach(updatedAccProcStep);

        restAccProcStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccProcStep.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccProcStep))
            )
            .andExpect(status().isOk());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccProcStepToMatchAllProperties(updatedAccProcStep);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccProcStep> accProcStepSearchList = Streamable.of(accProcStepSearchRepository.findAll()).toList();
                AccProcStep testAccProcStepSearch = accProcStepSearchList.get(searchDatabaseSizeAfter - 1);

                assertAccProcStepAllPropertiesEquals(testAccProcStepSearch, updatedAccProcStep);
            });
    }

    @Test
    @Transactional
    void putNonExistingAccProcStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        accProcStep.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccProcStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accProcStep.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accProcStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccProcStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        accProcStep.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accProcStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccProcStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        accProcStep.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProcStep)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccProcStepWithPatch() throws Exception {
        // Initialize the database
        insertedAccProcStep = accProcStepRepository.saveAndFlush(accProcStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accProcStep using partial update
        AccProcStep partialUpdatedAccProcStep = new AccProcStep();
        partialUpdatedAccProcStep.setId(accProcStep.getId());

        restAccProcStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccProcStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccProcStep))
            )
            .andExpect(status().isOk());

        // Validate the AccProcStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccProcStepUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccProcStep, accProcStep),
            getPersistedAccProcStep(accProcStep)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccProcStepWithPatch() throws Exception {
        // Initialize the database
        insertedAccProcStep = accProcStepRepository.saveAndFlush(accProcStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accProcStep using partial update
        AccProcStep partialUpdatedAccProcStep = new AccProcStep();
        partialUpdatedAccProcStep.setId(accProcStep.getId());

        restAccProcStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccProcStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccProcStep))
            )
            .andExpect(status().isOk());

        // Validate the AccProcStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccProcStepUpdatableFieldsEquals(partialUpdatedAccProcStep, getPersistedAccProcStep(partialUpdatedAccProcStep));
    }

    @Test
    @Transactional
    void patchNonExistingAccProcStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        accProcStep.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccProcStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accProcStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accProcStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccProcStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        accProcStep.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accProcStep))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccProcStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        accProcStep.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProcStepMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accProcStep)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccProcStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccProcStep() throws Exception {
        // Initialize the database
        insertedAccProcStep = accProcStepRepository.saveAndFlush(accProcStep);
        accProcStepRepository.save(accProcStep);
        accProcStepSearchRepository.save(accProcStep);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accProcStep
        restAccProcStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, accProcStep.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProcStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccProcStep() throws Exception {
        // Initialize the database
        insertedAccProcStep = accProcStepRepository.saveAndFlush(accProcStep);
        accProcStepSearchRepository.save(accProcStep);

        // Search the accProcStep
        restAccProcStepMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accProcStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accProcStep.getId().intValue())));
    }

    protected long getRepositoryCount() {
        return accProcStepRepository.count();
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

    protected AccProcStep getPersistedAccProcStep(AccProcStep accProcStep) {
        return accProcStepRepository.findById(accProcStep.getId()).orElseThrow();
    }

    protected void assertPersistedAccProcStepToMatchAllProperties(AccProcStep expectedAccProcStep) {
        assertAccProcStepAllPropertiesEquals(expectedAccProcStep, getPersistedAccProcStep(expectedAccProcStep));
    }

    protected void assertPersistedAccProcStepToMatchUpdatableProperties(AccProcStep expectedAccProcStep) {
        assertAccProcStepAllUpdatablePropertiesEquals(expectedAccProcStep, getPersistedAccProcStep(expectedAccProcStep));
    }
}
