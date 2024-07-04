package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.HrLetterParameterAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.HrLetterParameter;
import com.bulut.attendance.domain.enumeration.CalcType;
import com.bulut.attendance.domain.enumeration.CalcUnit;
import com.bulut.attendance.domain.enumeration.Deduction;
import com.bulut.attendance.domain.enumeration.Deduction;
import com.bulut.attendance.domain.enumeration.Earning;
import com.bulut.attendance.domain.enumeration.Hokm;
import com.bulut.attendance.domain.enumeration.LaborTime;
import com.bulut.attendance.repository.HrLetterParameterRepository;
import com.bulut.attendance.repository.search.HrLetterParameterSearchRepository;
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
 * Integration tests for the {@link HrLetterParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HrLetterParameterResourceIT {

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

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/hr-letter-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/hr-letter-parameters/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HrLetterParameterRepository hrLetterParameterRepository;

    @Autowired
    private HrLetterParameterSearchRepository hrLetterParameterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHrLetterParameterMockMvc;

    private HrLetterParameter hrLetterParameter;

    private HrLetterParameter insertedHrLetterParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrLetterParameter createEntity(EntityManager em) {
        HrLetterParameter hrLetterParameter = new HrLetterParameter()
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
            .other(DEFAULT_OTHER)
            .isEnabled(DEFAULT_IS_ENABLED);
        return hrLetterParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrLetterParameter createUpdatedEntity(EntityManager em) {
        HrLetterParameter hrLetterParameter = new HrLetterParameter()
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
            .other(UPDATED_OTHER)
            .isEnabled(UPDATED_IS_ENABLED);
        return hrLetterParameter;
    }

    @BeforeEach
    public void initTest() {
        hrLetterParameter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHrLetterParameter != null) {
            hrLetterParameterRepository.delete(insertedHrLetterParameter);
            hrLetterParameterSearchRepository.delete(insertedHrLetterParameter);
            insertedHrLetterParameter = null;
        }
    }

    @Test
    @Transactional
    void createHrLetterParameter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        // Create the HrLetterParameter
        var returnedHrLetterParameter = om.readValue(
            restHrLetterParameterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetterParameter)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HrLetterParameter.class
        );

        // Validate the HrLetterParameter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHrLetterParameterUpdatableFieldsEquals(returnedHrLetterParameter, getPersistedHrLetterParameter(returnedHrLetterParameter));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedHrLetterParameter = returnedHrLetterParameter;
    }

    @Test
    @Transactional
    void createHrLetterParameterWithExistingId() throws Exception {
        // Create the HrLetterParameter with an existing ID
        hrLetterParameter.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHrLetterParameterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetterParameter)))
            .andExpect(status().isBadRequest());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllHrLetterParameters() throws Exception {
        // Initialize the database
        insertedHrLetterParameter = hrLetterParameterRepository.saveAndFlush(hrLetterParameter);

        // Get all the hrLetterParameterList
        restHrLetterParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hrLetterParameter.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getHrLetterParameter() throws Exception {
        // Initialize the database
        insertedHrLetterParameter = hrLetterParameterRepository.saveAndFlush(hrLetterParameter);

        // Get the hrLetterParameter
        restHrLetterParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, hrLetterParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hrLetterParameter.getId().intValue()))
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
            .andExpect(jsonPath("$.other").value(DEFAULT_OTHER.toString()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHrLetterParameter() throws Exception {
        // Get the hrLetterParameter
        restHrLetterParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHrLetterParameter() throws Exception {
        // Initialize the database
        insertedHrLetterParameter = hrLetterParameterRepository.saveAndFlush(hrLetterParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        hrLetterParameterSearchRepository.save(hrLetterParameter);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());

        // Update the hrLetterParameter
        HrLetterParameter updatedHrLetterParameter = hrLetterParameterRepository.findById(hrLetterParameter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHrLetterParameter are not directly saved in db
        em.detach(updatedHrLetterParameter);
        updatedHrLetterParameter
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
            .other(UPDATED_OTHER)
            .isEnabled(UPDATED_IS_ENABLED);

        restHrLetterParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHrLetterParameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHrLetterParameter))
            )
            .andExpect(status().isOk());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHrLetterParameterToMatchAllProperties(updatedHrLetterParameter);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HrLetterParameter> hrLetterParameterSearchList = Streamable.of(hrLetterParameterSearchRepository.findAll()).toList();
                HrLetterParameter testHrLetterParameterSearch = hrLetterParameterSearchList.get(searchDatabaseSizeAfter - 1);

                assertHrLetterParameterAllPropertiesEquals(testHrLetterParameterSearch, updatedHrLetterParameter);
            });
    }

    @Test
    @Transactional
    void putNonExistingHrLetterParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        hrLetterParameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHrLetterParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hrLetterParameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hrLetterParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchHrLetterParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        hrLetterParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hrLetterParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHrLetterParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        hrLetterParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterParameterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hrLetterParameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateHrLetterParameterWithPatch() throws Exception {
        // Initialize the database
        insertedHrLetterParameter = hrLetterParameterRepository.saveAndFlush(hrLetterParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hrLetterParameter using partial update
        HrLetterParameter partialUpdatedHrLetterParameter = new HrLetterParameter();
        partialUpdatedHrLetterParameter.setId(hrLetterParameter.getId());

        partialUpdatedHrLetterParameter
            .title(UPDATED_TITLE)
            .manualOrAuto(UPDATED_MANUAL_OR_AUTO)
            .unit(UPDATED_UNIT)
            .laborTime(UPDATED_LABOR_TIME)
            .deduction(UPDATED_DEDUCTION)
            .isEnabled(UPDATED_IS_ENABLED);

        restHrLetterParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHrLetterParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHrLetterParameter))
            )
            .andExpect(status().isOk());

        // Validate the HrLetterParameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHrLetterParameterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHrLetterParameter, hrLetterParameter),
            getPersistedHrLetterParameter(hrLetterParameter)
        );
    }

    @Test
    @Transactional
    void fullUpdateHrLetterParameterWithPatch() throws Exception {
        // Initialize the database
        insertedHrLetterParameter = hrLetterParameterRepository.saveAndFlush(hrLetterParameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hrLetterParameter using partial update
        HrLetterParameter partialUpdatedHrLetterParameter = new HrLetterParameter();
        partialUpdatedHrLetterParameter.setId(hrLetterParameter.getId());

        partialUpdatedHrLetterParameter
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
            .other(UPDATED_OTHER)
            .isEnabled(UPDATED_IS_ENABLED);

        restHrLetterParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHrLetterParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHrLetterParameter))
            )
            .andExpect(status().isOk());

        // Validate the HrLetterParameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHrLetterParameterUpdatableFieldsEquals(
            partialUpdatedHrLetterParameter,
            getPersistedHrLetterParameter(partialUpdatedHrLetterParameter)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHrLetterParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        hrLetterParameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHrLetterParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hrLetterParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hrLetterParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHrLetterParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        hrLetterParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hrLetterParameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHrLetterParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        hrLetterParameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHrLetterParameterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hrLetterParameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HrLetterParameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteHrLetterParameter() throws Exception {
        // Initialize the database
        insertedHrLetterParameter = hrLetterParameterRepository.saveAndFlush(hrLetterParameter);
        hrLetterParameterRepository.save(hrLetterParameter);
        hrLetterParameterSearchRepository.save(hrLetterParameter);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the hrLetterParameter
        restHrLetterParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, hrLetterParameter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hrLetterParameterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchHrLetterParameter() throws Exception {
        // Initialize the database
        insertedHrLetterParameter = hrLetterParameterRepository.saveAndFlush(hrLetterParameter);
        hrLetterParameterSearchRepository.save(hrLetterParameter);

        // Search the hrLetterParameter
        restHrLetterParameterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + hrLetterParameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hrLetterParameter.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    protected long getRepositoryCount() {
        return hrLetterParameterRepository.count();
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

    protected HrLetterParameter getPersistedHrLetterParameter(HrLetterParameter hrLetterParameter) {
        return hrLetterParameterRepository.findById(hrLetterParameter.getId()).orElseThrow();
    }

    protected void assertPersistedHrLetterParameterToMatchAllProperties(HrLetterParameter expectedHrLetterParameter) {
        assertHrLetterParameterAllPropertiesEquals(expectedHrLetterParameter, getPersistedHrLetterParameter(expectedHrLetterParameter));
    }

    protected void assertPersistedHrLetterParameterToMatchUpdatableProperties(HrLetterParameter expectedHrLetterParameter) {
        assertHrLetterParameterAllUpdatablePropertiesEquals(
            expectedHrLetterParameter,
            getPersistedHrLetterParameter(expectedHrLetterParameter)
        );
    }
}
