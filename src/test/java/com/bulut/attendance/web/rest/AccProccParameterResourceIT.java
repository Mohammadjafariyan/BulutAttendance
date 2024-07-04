package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.AccProccParameterAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.AccProccParameter;
import com.bulut.attendance.domain.enumeration.CalcType;
import com.bulut.attendance.domain.enumeration.CalcUnit;
import com.bulut.attendance.domain.enumeration.Deduction;
import com.bulut.attendance.domain.enumeration.Deduction;
import com.bulut.attendance.domain.enumeration.Earning;
import com.bulut.attendance.domain.enumeration.Hokm;
import com.bulut.attendance.domain.enumeration.LaborTime;
import com.bulut.attendance.repository.AccProccParameterRepository;
import com.bulut.attendance.repository.search.AccProccParameterSearchRepository;
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
 * Integration tests for the {@link AccProccParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccProccParameterResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final CalcType DEFAULT_MANUAL_OR_AUTO = CalcType.ByHand;
    private static final CalcType UPDATED_MANUAL_OR_AUTO = CalcType.BySystem;

    private static final String DEFAULT_FORMULA = "AAAAAAAAAA";
    private static final String UPDATED_FORMULA = "BBBBBBBBBB";

    private static final CalcUnit DEFAULT_UNIT = CalcUnit.Daily;
    private static final CalcUnit UPDATED_UNIT = CalcUnit.Hourly;

    private static final Boolean DEFAULT_IS_DEDUC_TAX = false;
    private static final Boolean UPDATED_IS_DEDUC_TAX = true;

    private static final Boolean DEFAULT_IS_DEDUC_INSURANCE = false;
    private static final Boolean UPDATED_IS_DEDUC_INSURANCE = true;

    private static final LaborTime DEFAULT_LABOR_TIME = LaborTime.STANDARD;
    private static final LaborTime UPDATED_LABOR_TIME = LaborTime.EFFECTIVE;

    private static final Hokm DEFAULT_HOKM = Hokm.Other;
    private static final Hokm UPDATED_HOKM = Hokm.BASE;

    private static final Earning DEFAULT_EARNINGS = Earning.Other;
    private static final Earning UPDATED_EARNINGS = Earning.OverTime;

    private static final Deduction DEFAULT_DEDUCTION = Deduction.Other;
    private static final Deduction UPDATED_DEDUCTION = Deduction.Insurance_Labor;

    private static final Deduction DEFAULT_OTHER = Deduction.Other;
    private static final Deduction UPDATED_OTHER = Deduction.Insurance_Labor;

    private static final String ENTITY_API_URL = "/api/acc-procc-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/acc-procc-parameters/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccProccParameterRepository accProccParameterRepository;

    @Autowired
    private AccProccParameterSearchRepository accProccParameterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccProccParameterMockMvc;

    private AccProccParameter accProccParameter;

    private AccProccParameter insertedAccProccParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccProccParameter createEntity(EntityManager em) {
        AccProccParameter accProccParameter = new AccProccParameter()
            .title(DEFAULT_TITLE)
            .manualOrAuto(DEFAULT_MANUAL_OR_AUTO)
            .formula(DEFAULT_FORMULA)
            .unit(DEFAULT_UNIT)
            .isDeducTax(DEFAULT_IS_DEDUC_TAX)
            .isDeducInsurance(DEFAULT_IS_DEDUC_INSURANCE)
            .laborTime(DEFAULT_LABOR_TIME)
            .hokm(DEFAULT_HOKM)
            .earnings(DEFAULT_EARNINGS)
            .deduction(DEFAULT_DEDUCTION)
            .other(DEFAULT_OTHER);
        return accProccParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccProccParameter createUpdatedEntity(EntityManager em) {
        AccProccParameter accProccParameter = new AccProccParameter()
            .title(UPDATED_TITLE)
            .manualOrAuto(UPDATED_MANUAL_OR_AUTO)
            .formula(UPDATED_FORMULA)
            .unit(UPDATED_UNIT)
            .isDeducTax(UPDATED_IS_DEDUC_TAX)
            .isDeducInsurance(UPDATED_IS_DEDUC_INSURANCE)
            .laborTime(UPDATED_LABOR_TIME)
            .hokm(UPDATED_HOKM)
            .earnings(UPDATED_EARNINGS)
            .deduction(UPDATED_DEDUCTION)
            .other(UPDATED_OTHER);
        return accProccParameter;
    }

    @BeforeEach
    public void initTest() {
        accProccParameter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccProccParameter != null) {
            accProccParameterRepository.delete(insertedAccProccParameter);
            accProccParameterSearchRepository.delete(insertedAccProccParameter);
            insertedAccProccParameter = null;
        }
    }

    @Test
    @Transactional
    void createAccProccParameter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        // Create the AccProccParameter
        var returnedAccProccParameter = om.readValue(
            restAccProccParameterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProccParameter)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccProccParameter.class
        );

        // Validate the AccProccParameter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccProccParameterUpdatableFieldsEquals(returnedAccProccParameter, getPersistedAccProccParameter(returnedAccProccParameter));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccProccParameter = returnedAccProccParameter;
    }

    @Test
    @Transactional
    void createAccProccParameterWithExistingId() throws Exception {
        // Create the AccProccParameter with an existing ID
        accProccParameter.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccProccParameterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProccParameter)))
            .andExpect(status().isBadRequest());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccProccParameters() throws Exception {
        // Initialize the database
        insertedAccProccParameter = accProccParameterRepository.saveAndFlush(accProccParameter);

        // Get all the accProccParameterList
        restAccProccParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accProccParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].manualOrAuto").value(hasItem(DEFAULT_MANUAL_OR_AUTO.toString())))
            .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].isDeducTax").value(hasItem(DEFAULT_IS_DEDUC_TAX.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeducInsurance").value(hasItem(DEFAULT_IS_DEDUC_INSURANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].laborTime").value(hasItem(DEFAULT_LABOR_TIME.toString())))
            .andExpect(jsonPath("$.[*].hokm").value(hasItem(DEFAULT_HOKM.toString())))
            .andExpect(jsonPath("$.[*].earnings").value(hasItem(DEFAULT_EARNINGS.toString())))
            .andExpect(jsonPath("$.[*].deduction").value(hasItem(DEFAULT_DEDUCTION.toString())))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER.toString())));
    }

    @Test
    @Transactional
    void getAccProccParameter() throws Exception {
        // Initialize the database
        insertedAccProccParameter = accProccParameterRepository.saveAndFlush(accProccParameter);

        // Get the accProccParameter
        restAccProccParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, accProccParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accProccParameter.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.manualOrAuto").value(DEFAULT_MANUAL_OR_AUTO.toString()))
            .andExpect(jsonPath("$.formula").value(DEFAULT_FORMULA))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.isDeducTax").value(DEFAULT_IS_DEDUC_TAX.booleanValue()))
            .andExpect(jsonPath("$.isDeducInsurance").value(DEFAULT_IS_DEDUC_INSURANCE.booleanValue()))
            .andExpect(jsonPath("$.laborTime").value(DEFAULT_LABOR_TIME.toString()))
            .andExpect(jsonPath("$.hokm").value(DEFAULT_HOKM.toString()))
            .andExpect(jsonPath("$.earnings").value(DEFAULT_EARNINGS.toString()))
            .andExpect(jsonPath("$.deduction").value(DEFAULT_DEDUCTION.toString()))
            .andExpect(jsonPath("$.other").value(DEFAULT_OTHER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccProccParameter() throws Exception {
        // Get the accProccParameter
        restAccProccParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccProccParameter() throws Exception {
        // Initialize the database
        insertedAccProccParameter = accProccParameterRepository.saveAndFlush(accProccParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accProccParameterSearchRepository.save(accProccParameter);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());

        // Update the accProccParameter
        AccProccParameter updatedAccProccParameter = accProccParameterRepository.findById(accProccParameter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccProccParameter are not directly saved in db
        em.detach(updatedAccProccParameter);
        updatedAccProccParameter
            .title(UPDATED_TITLE)
            .manualOrAuto(UPDATED_MANUAL_OR_AUTO)
            .formula(UPDATED_FORMULA)
            .unit(UPDATED_UNIT)
            .isDeducTax(UPDATED_IS_DEDUC_TAX)
            .isDeducInsurance(UPDATED_IS_DEDUC_INSURANCE)
            .laborTime(UPDATED_LABOR_TIME)
            .hokm(UPDATED_HOKM)
            .earnings(UPDATED_EARNINGS)
            .deduction(UPDATED_DEDUCTION)
            .other(UPDATED_OTHER);

        restAccProccParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccProccParameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccProccParameter))
            )
            .andExpect(status().isOk());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccProccParameterToMatchAllProperties(updatedAccProccParameter);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccProccParameter> accProccParameterSearchList = Streamable.of(accProccParameterSearchRepository.findAll()).toList();
                AccProccParameter testAccProccParameterSearch = accProccParameterSearchList.get(searchDatabaseSizeAfter - 1);

                assertAccProccParameterAllPropertiesEquals(testAccProccParameterSearch, updatedAccProccParameter);
            });
    }

    @Test
    @Transactional
    void putNonExistingAccProccParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        accProccParameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccProccParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accProccParameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accProccParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccProccParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        accProccParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProccParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accProccParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccProccParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        accProccParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProccParameterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accProccParameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccProccParameterWithPatch() throws Exception {
        // Initialize the database
        insertedAccProccParameter = accProccParameterRepository.saveAndFlush(accProccParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accProccParameter using partial update
        AccProccParameter partialUpdatedAccProccParameter = new AccProccParameter();
        partialUpdatedAccProccParameter.setId(accProccParameter.getId());

        partialUpdatedAccProccParameter
            .formula(UPDATED_FORMULA)
            .isDeducInsurance(UPDATED_IS_DEDUC_INSURANCE)
            .laborTime(UPDATED_LABOR_TIME)
            .hokm(UPDATED_HOKM)
            .earnings(UPDATED_EARNINGS)
            .deduction(UPDATED_DEDUCTION);

        restAccProccParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccProccParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccProccParameter))
            )
            .andExpect(status().isOk());

        // Validate the AccProccParameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccProccParameterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccProccParameter, accProccParameter),
            getPersistedAccProccParameter(accProccParameter)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccProccParameterWithPatch() throws Exception {
        // Initialize the database
        insertedAccProccParameter = accProccParameterRepository.saveAndFlush(accProccParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accProccParameter using partial update
        AccProccParameter partialUpdatedAccProccParameter = new AccProccParameter();
        partialUpdatedAccProccParameter.setId(accProccParameter.getId());

        partialUpdatedAccProccParameter
            .title(UPDATED_TITLE)
            .manualOrAuto(UPDATED_MANUAL_OR_AUTO)
            .formula(UPDATED_FORMULA)
            .unit(UPDATED_UNIT)
            .isDeducTax(UPDATED_IS_DEDUC_TAX)
            .isDeducInsurance(UPDATED_IS_DEDUC_INSURANCE)
            .laborTime(UPDATED_LABOR_TIME)
            .hokm(UPDATED_HOKM)
            .earnings(UPDATED_EARNINGS)
            .deduction(UPDATED_DEDUCTION)
            .other(UPDATED_OTHER);

        restAccProccParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccProccParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccProccParameter))
            )
            .andExpect(status().isOk());

        // Validate the AccProccParameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccProccParameterUpdatableFieldsEquals(
            partialUpdatedAccProccParameter,
            getPersistedAccProccParameter(partialUpdatedAccProccParameter)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccProccParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        accProccParameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccProccParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accProccParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accProccParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccProccParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        accProccParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProccParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accProccParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccProccParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        accProccParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccProccParameterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accProccParameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccProccParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccProccParameter() throws Exception {
        // Initialize the database
        insertedAccProccParameter = accProccParameterRepository.saveAndFlush(accProccParameter);
        accProccParameterRepository.save(accProccParameter);
        accProccParameterSearchRepository.save(accProccParameter);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accProccParameter
        restAccProccParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, accProccParameter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accProccParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccProccParameter() throws Exception {
        // Initialize the database
        insertedAccProccParameter = accProccParameterRepository.saveAndFlush(accProccParameter);
        accProccParameterSearchRepository.save(accProccParameter);

        // Search the accProccParameter
        restAccProccParameterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accProccParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accProccParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].manualOrAuto").value(hasItem(DEFAULT_MANUAL_OR_AUTO.toString())))
            .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].isDeducTax").value(hasItem(DEFAULT_IS_DEDUC_TAX.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeducInsurance").value(hasItem(DEFAULT_IS_DEDUC_INSURANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].laborTime").value(hasItem(DEFAULT_LABOR_TIME.toString())))
            .andExpect(jsonPath("$.[*].hokm").value(hasItem(DEFAULT_HOKM.toString())))
            .andExpect(jsonPath("$.[*].earnings").value(hasItem(DEFAULT_EARNINGS.toString())))
            .andExpect(jsonPath("$.[*].deduction").value(hasItem(DEFAULT_DEDUCTION.toString())))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER.toString())));
    }

    protected long getRepositoryCount() {
        return accProccParameterRepository.count();
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

    protected AccProccParameter getPersistedAccProccParameter(AccProccParameter accProccParameter) {
        return accProccParameterRepository.findById(accProccParameter.getId()).orElseThrow();
    }

    protected void assertPersistedAccProccParameterToMatchAllProperties(AccProccParameter expectedAccProccParameter) {
        assertAccProccParameterAllPropertiesEquals(expectedAccProccParameter, getPersistedAccProccParameter(expectedAccProccParameter));
    }

    protected void assertPersistedAccProccParameterToMatchUpdatableProperties(AccProccParameter expectedAccProccParameter) {
        assertAccProccParameterAllUpdatablePropertiesEquals(
            expectedAccProccParameter,
            getPersistedAccProccParameter(expectedAccProccParameter)
        );
    }
}
