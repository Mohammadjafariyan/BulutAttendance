package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.AccountingProcedureExecutionAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.AccountingProcedureExecution;
import com.bulut.attendance.repository.AccountingProcedureExecutionRepository;
import com.bulut.attendance.repository.search.AccountingProcedureExecutionSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link AccountingProcedureExecutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountingProcedureExecutionResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/accounting-procedure-executions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/accounting-procedure-executions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountingProcedureExecutionRepository accountingProcedureExecutionRepository;

    @Autowired
    private AccountingProcedureExecutionSearchRepository accountingProcedureExecutionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountingProcedureExecutionMockMvc;

    private AccountingProcedureExecution accountingProcedureExecution;

    private AccountingProcedureExecution insertedAccountingProcedureExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountingProcedureExecution createEntity(EntityManager em) {
        AccountingProcedureExecution accountingProcedureExecution = new AccountingProcedureExecution()
            .dateTime(DEFAULT_DATE_TIME)
            .desc(DEFAULT_DESC);
        return accountingProcedureExecution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountingProcedureExecution createUpdatedEntity(EntityManager em) {
        AccountingProcedureExecution accountingProcedureExecution = new AccountingProcedureExecution()
            .dateTime(UPDATED_DATE_TIME)
            .desc(UPDATED_DESC);
        return accountingProcedureExecution;
    }

    @BeforeEach
    public void initTest() {
        accountingProcedureExecution = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccountingProcedureExecution != null) {
            accountingProcedureExecutionRepository.delete(insertedAccountingProcedureExecution);
            accountingProcedureExecutionSearchRepository.delete(insertedAccountingProcedureExecution);
            insertedAccountingProcedureExecution = null;
        }
    }

    @Test
    @Transactional
    void createAccountingProcedureExecution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        // Create the AccountingProcedureExecution
        var returnedAccountingProcedureExecution = om.readValue(
            restAccountingProcedureExecutionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountingProcedureExecution))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountingProcedureExecution.class
        );

        // Validate the AccountingProcedureExecution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccountingProcedureExecutionUpdatableFieldsEquals(
            returnedAccountingProcedureExecution,
            getPersistedAccountingProcedureExecution(returnedAccountingProcedureExecution)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccountingProcedureExecution = returnedAccountingProcedureExecution;
    }

    @Test
    @Transactional
    void createAccountingProcedureExecutionWithExistingId() throws Exception {
        // Create the AccountingProcedureExecution with an existing ID
        accountingProcedureExecution.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountingProcedureExecutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountingProcedureExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccountingProcedureExecutions() throws Exception {
        // Initialize the database
        insertedAccountingProcedureExecution = accountingProcedureExecutionRepository.saveAndFlush(accountingProcedureExecution);

        // Get all the accountingProcedureExecutionList
        restAccountingProcedureExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingProcedureExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)));
    }

    @Test
    @Transactional
    void getAccountingProcedureExecution() throws Exception {
        // Initialize the database
        insertedAccountingProcedureExecution = accountingProcedureExecutionRepository.saveAndFlush(accountingProcedureExecution);

        // Get the accountingProcedureExecution
        restAccountingProcedureExecutionMockMvc
            .perform(get(ENTITY_API_URL_ID, accountingProcedureExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountingProcedureExecution.getId().intValue()))
            .andExpect(jsonPath("$.dateTime").value(sameInstant(DEFAULT_DATE_TIME)))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC));
    }

    @Test
    @Transactional
    void getNonExistingAccountingProcedureExecution() throws Exception {
        // Get the accountingProcedureExecution
        restAccountingProcedureExecutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountingProcedureExecution() throws Exception {
        // Initialize the database
        insertedAccountingProcedureExecution = accountingProcedureExecutionRepository.saveAndFlush(accountingProcedureExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountingProcedureExecutionSearchRepository.save(accountingProcedureExecution);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());

        // Update the accountingProcedureExecution
        AccountingProcedureExecution updatedAccountingProcedureExecution = accountingProcedureExecutionRepository
            .findById(accountingProcedureExecution.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAccountingProcedureExecution are not directly saved in db
        em.detach(updatedAccountingProcedureExecution);
        updatedAccountingProcedureExecution.dateTime(UPDATED_DATE_TIME).desc(UPDATED_DESC);

        restAccountingProcedureExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountingProcedureExecution.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccountingProcedureExecution))
            )
            .andExpect(status().isOk());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountingProcedureExecutionToMatchAllProperties(updatedAccountingProcedureExecution);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccountingProcedureExecution> accountingProcedureExecutionSearchList = Streamable.of(
                    accountingProcedureExecutionSearchRepository.findAll()
                ).toList();
                AccountingProcedureExecution testAccountingProcedureExecutionSearch = accountingProcedureExecutionSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertAccountingProcedureExecutionAllPropertiesEquals(
                    testAccountingProcedureExecutionSearch,
                    updatedAccountingProcedureExecution
                );
            });
    }

    @Test
    @Transactional
    void putNonExistingAccountingProcedureExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        accountingProcedureExecution.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountingProcedureExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountingProcedureExecution.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountingProcedureExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountingProcedureExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        accountingProcedureExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountingProcedureExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountingProcedureExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        accountingProcedureExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureExecutionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountingProcedureExecution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccountingProcedureExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedAccountingProcedureExecution = accountingProcedureExecutionRepository.saveAndFlush(accountingProcedureExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountingProcedureExecution using partial update
        AccountingProcedureExecution partialUpdatedAccountingProcedureExecution = new AccountingProcedureExecution();
        partialUpdatedAccountingProcedureExecution.setId(accountingProcedureExecution.getId());

        partialUpdatedAccountingProcedureExecution.dateTime(UPDATED_DATE_TIME).desc(UPDATED_DESC);

        restAccountingProcedureExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountingProcedureExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountingProcedureExecution))
            )
            .andExpect(status().isOk());

        // Validate the AccountingProcedureExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountingProcedureExecutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccountingProcedureExecution, accountingProcedureExecution),
            getPersistedAccountingProcedureExecution(accountingProcedureExecution)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccountingProcedureExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedAccountingProcedureExecution = accountingProcedureExecutionRepository.saveAndFlush(accountingProcedureExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountingProcedureExecution using partial update
        AccountingProcedureExecution partialUpdatedAccountingProcedureExecution = new AccountingProcedureExecution();
        partialUpdatedAccountingProcedureExecution.setId(accountingProcedureExecution.getId());

        partialUpdatedAccountingProcedureExecution.dateTime(UPDATED_DATE_TIME).desc(UPDATED_DESC);

        restAccountingProcedureExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountingProcedureExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountingProcedureExecution))
            )
            .andExpect(status().isOk());

        // Validate the AccountingProcedureExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountingProcedureExecutionUpdatableFieldsEquals(
            partialUpdatedAccountingProcedureExecution,
            getPersistedAccountingProcedureExecution(partialUpdatedAccountingProcedureExecution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccountingProcedureExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        accountingProcedureExecution.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountingProcedureExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountingProcedureExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountingProcedureExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountingProcedureExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        accountingProcedureExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountingProcedureExecution))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountingProcedureExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        accountingProcedureExecution.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountingProcedureExecution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountingProcedureExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccountingProcedureExecution() throws Exception {
        // Initialize the database
        insertedAccountingProcedureExecution = accountingProcedureExecutionRepository.saveAndFlush(accountingProcedureExecution);
        accountingProcedureExecutionRepository.save(accountingProcedureExecution);
        accountingProcedureExecutionSearchRepository.save(accountingProcedureExecution);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accountingProcedureExecution
        restAccountingProcedureExecutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountingProcedureExecution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureExecutionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccountingProcedureExecution() throws Exception {
        // Initialize the database
        insertedAccountingProcedureExecution = accountingProcedureExecutionRepository.saveAndFlush(accountingProcedureExecution);
        accountingProcedureExecutionSearchRepository.save(accountingProcedureExecution);

        // Search the accountingProcedureExecution
        restAccountingProcedureExecutionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountingProcedureExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingProcedureExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(sameInstant(DEFAULT_DATE_TIME))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)));
    }

    protected long getRepositoryCount() {
        return accountingProcedureExecutionRepository.count();
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

    protected AccountingProcedureExecution getPersistedAccountingProcedureExecution(
        AccountingProcedureExecution accountingProcedureExecution
    ) {
        return accountingProcedureExecutionRepository.findById(accountingProcedureExecution.getId()).orElseThrow();
    }

    protected void assertPersistedAccountingProcedureExecutionToMatchAllProperties(
        AccountingProcedureExecution expectedAccountingProcedureExecution
    ) {
        assertAccountingProcedureExecutionAllPropertiesEquals(
            expectedAccountingProcedureExecution,
            getPersistedAccountingProcedureExecution(expectedAccountingProcedureExecution)
        );
    }

    protected void assertPersistedAccountingProcedureExecutionToMatchUpdatableProperties(
        AccountingProcedureExecution expectedAccountingProcedureExecution
    ) {
        assertAccountingProcedureExecutionAllUpdatablePropertiesEquals(
            expectedAccountingProcedureExecution,
            getPersistedAccountingProcedureExecution(expectedAccountingProcedureExecution)
        );
    }
}
