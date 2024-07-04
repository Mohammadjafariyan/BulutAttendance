package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.ParameterAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.Parameter;
import com.bulut.attendance.domain.enumeration.CalcType;
import com.bulut.attendance.domain.enumeration.CalcUnit;
import com.bulut.attendance.domain.enumeration.Deduction;
import com.bulut.attendance.domain.enumeration.Deduction;
import com.bulut.attendance.domain.enumeration.Earning;
import com.bulut.attendance.domain.enumeration.Hokm;
import com.bulut.attendance.domain.enumeration.LaborTime;
import com.bulut.attendance.repository.ParameterRepository;
import com.bulut.attendance.repository.search.ParameterSearchRepository;
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
 * Integration tests for the {@link ParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParameterResourceIT {

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

    private static final String ENTITY_API_URL = "/api/parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/parameters/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterSearchRepository parameterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    private Parameter insertedParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter()
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
        return parameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createUpdatedEntity(EntityManager em) {
        Parameter parameter = new Parameter()
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
        return parameter;
    }

    @BeforeEach
    public void initTest() {
        parameter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedParameter != null) {
            parameterRepository.delete(insertedParameter);
            parameterSearchRepository.delete(insertedParameter);
            insertedParameter = null;
        }
    }

    @Test
    @Transactional
    void createParameter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        // Create the Parameter
        var returnedParameter = om.readValue(
            restParameterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Parameter.class
        );

        // Validate the Parameter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertParameterUpdatableFieldsEquals(returnedParameter, getPersistedParameter(returnedParameter));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedParameter = returnedParameter;
    }

    @Test
    @Transactional
    void createParameterWithExistingId() throws Exception {
        // Create the Parameter with an existing ID
        parameter.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter)))
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllParameters() throws Exception {
        // Initialize the database
        insertedParameter = parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList
        restParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
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
    void getParameter() throws Exception {
        // Initialize the database
        insertedParameter = parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
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
    void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParameter() throws Exception {
        // Initialize the database
        insertedParameter = parameterRepository.saveAndFlush(parameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameterSearchRepository.save(parameter);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findById(parameter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParameter are not directly saved in db
        em.detach(updatedParameter);
        updatedParameter
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

        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedParameterToMatchAllProperties(updatedParameter);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Parameter> parameterSearchList = Streamable.of(parameterSearchRepository.findAll()).toList();
                Parameter testParameterSearch = parameterSearchList.get(searchDatabaseSizeAfter - 1);

                assertParameterAllPropertiesEquals(testParameterSearch, updatedParameter);
            });
    }

    @Test
    @Transactional
    void putNonExistingParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        parameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parameter.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        insertedParameter = parameterRepository.saveAndFlush(parameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter
            .formula(UPDATED_FORMULA)
            .unit(UPDATED_UNIT)
            .isDeducTax(UPDATED_IS_DEDUC_TAX)
            .isDeducInsurance(UPDATED_IS_DEDUC_INSURANCE)
            .laborTime(UPDATED_LABOR_TIME)
            .deduction(UPDATED_DEDUCTION);

        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParameterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedParameter, parameter),
            getPersistedParameter(parameter)
        );
    }

    @Test
    @Transactional
    void fullUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        insertedParameter = parameterRepository.saveAndFlush(parameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter
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

        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParameterUpdatableFieldsEquals(partialUpdatedParameter, getPersistedParameter(partialUpdatedParameter));
    }

    @Test
    @Transactional
    void patchNonExistingParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        parameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(parameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteParameter() throws Exception {
        // Initialize the database
        insertedParameter = parameterRepository.saveAndFlush(parameter);
        parameterRepository.save(parameter);
        parameterSearchRepository.save(parameter);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the parameter
        restParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, parameter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(parameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchParameter() throws Exception {
        // Initialize the database
        insertedParameter = parameterRepository.saveAndFlush(parameter);
        parameterSearchRepository.save(parameter);

        // Search the parameter
        restParameterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
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
        return parameterRepository.count();
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

    protected Parameter getPersistedParameter(Parameter parameter) {
        return parameterRepository.findById(parameter.getId()).orElseThrow();
    }

    protected void assertPersistedParameterToMatchAllProperties(Parameter expectedParameter) {
        assertParameterAllPropertiesEquals(expectedParameter, getPersistedParameter(expectedParameter));
    }

    protected void assertPersistedParameterToMatchUpdatableProperties(Parameter expectedParameter) {
        assertParameterAllUpdatablePropertiesEquals(expectedParameter, getPersistedParameter(expectedParameter));
    }
}
