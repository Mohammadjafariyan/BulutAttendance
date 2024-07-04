package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.AccountingProcedureAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.AccountingProcedure;
import com.bulut.attendance.repository.AccountingProcedureRepository;
import com.bulut.attendance.repository.search.AccountingProcedureSearchRepository;
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
 * Integration tests for the {@link AccountingProcedureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountingProcedureResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/accounting-procedures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/accounting-procedures/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountingProcedureRepository accountingProcedureRepository;

    @Autowired
    private AccountingProcedureSearchRepository accountingProcedureSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountingProcedureMockMvc;

    private AccountingProcedure accountingProcedure;

    private AccountingProcedure insertedAccountingProcedure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountingProcedure createEntity(EntityManager em) {
        AccountingProcedure accountingProcedure = new AccountingProcedure().title(DEFAULT_TITLE);
        return accountingProcedure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountingProcedure createUpdatedEntity(EntityManager em) {
        AccountingProcedure accountingProcedure = new AccountingProcedure().title(UPDATED_TITLE);
        return accountingProcedure;
    }

    @BeforeEach
    public void initTest() {
        accountingProcedure = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccountingProcedure != null) {
            accountingProcedureRepository.delete(insertedAccountingProcedure);
            accountingProcedureSearchRepository.delete(insertedAccountingProcedure);
            insertedAccountingProcedure = null;
        }
    }

    @Test
    @Transactional
    void createAccountingProcedure() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        // Create the AccountingProcedure
        var returnedAccountingProcedure = om.readValue(
            restAccountingProcedureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountingProcedure)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountingProcedure.class
        );

        // Validate the AccountingProcedure in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccountingProcedureUpdatableFieldsEquals(
            returnedAccountingProcedure,
            getPersistedAccountingProcedure(returnedAccountingProcedure)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccountingProcedure = returnedAccountingProcedure;
    }

    @Test
    @Transactional
    void createAccountingProcedureWithExistingId() throws Exception {
        // Create the AccountingProcedure with an existing ID
        accountingProcedure.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountingProcedureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountingProcedure)))
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccountingProcedures() throws Exception {
        // Initialize the database
        insertedAccountingProcedure = accountingProcedureRepository.saveAndFlush(accountingProcedure);

        // Get all the accountingProcedureList
        restAccountingProcedureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingProcedure.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getAccountingProcedure() throws Exception {
        // Initialize the database
        insertedAccountingProcedure = accountingProcedureRepository.saveAndFlush(accountingProcedure);

        // Get the accountingProcedure
        restAccountingProcedureMockMvc
            .perform(get(ENTITY_API_URL_ID, accountingProcedure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountingProcedure.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingAccountingProcedure() throws Exception {
        // Get the accountingProcedure
        restAccountingProcedureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountingProcedure() throws Exception {
        // Initialize the database
        insertedAccountingProcedure = accountingProcedureRepository.saveAndFlush(accountingProcedure);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountingProcedureSearchRepository.save(accountingProcedure);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());

        // Update the accountingProcedure
        AccountingProcedure updatedAccountingProcedure = accountingProcedureRepository.findById(accountingProcedure.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccountingProcedure are not directly saved in db
        em.detach(updatedAccountingProcedure);
        updatedAccountingProcedure.title(UPDATED_TITLE);

        restAccountingProcedureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountingProcedure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccountingProcedure))
            )
            .andExpect(status().isOk());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountingProcedureToMatchAllProperties(updatedAccountingProcedure);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccountingProcedure> accountingProcedureSearchList = Streamable.of(
                    accountingProcedureSearchRepository.findAll()
                ).toList();
                AccountingProcedure testAccountingProcedureSearch = accountingProcedureSearchList.get(searchDatabaseSizeAfter - 1);

                assertAccountingProcedureAllPropertiesEquals(testAccountingProcedureSearch, updatedAccountingProcedure);
            });
    }

    @Test
    @Transactional
    void putNonExistingAccountingProcedure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        accountingProcedure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountingProcedureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountingProcedure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountingProcedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountingProcedure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        accountingProcedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountingProcedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountingProcedure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        accountingProcedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountingProcedure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccountingProcedureWithPatch() throws Exception {
        // Initialize the database
        insertedAccountingProcedure = accountingProcedureRepository.saveAndFlush(accountingProcedure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountingProcedure using partial update
        AccountingProcedure partialUpdatedAccountingProcedure = new AccountingProcedure();
        partialUpdatedAccountingProcedure.setId(accountingProcedure.getId());

        restAccountingProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountingProcedure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountingProcedure))
            )
            .andExpect(status().isOk());

        // Validate the AccountingProcedure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountingProcedureUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccountingProcedure, accountingProcedure),
            getPersistedAccountingProcedure(accountingProcedure)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccountingProcedureWithPatch() throws Exception {
        // Initialize the database
        insertedAccountingProcedure = accountingProcedureRepository.saveAndFlush(accountingProcedure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountingProcedure using partial update
        AccountingProcedure partialUpdatedAccountingProcedure = new AccountingProcedure();
        partialUpdatedAccountingProcedure.setId(accountingProcedure.getId());

        partialUpdatedAccountingProcedure.title(UPDATED_TITLE);

        restAccountingProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountingProcedure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountingProcedure))
            )
            .andExpect(status().isOk());

        // Validate the AccountingProcedure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountingProcedureUpdatableFieldsEquals(
            partialUpdatedAccountingProcedure,
            getPersistedAccountingProcedure(partialUpdatedAccountingProcedure)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccountingProcedure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        accountingProcedure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountingProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountingProcedure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountingProcedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountingProcedure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        accountingProcedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountingProcedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountingProcedure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        accountingProcedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountingProcedureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accountingProcedure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountingProcedure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccountingProcedure() throws Exception {
        // Initialize the database
        insertedAccountingProcedure = accountingProcedureRepository.saveAndFlush(accountingProcedure);
        accountingProcedureRepository.save(accountingProcedure);
        accountingProcedureSearchRepository.save(accountingProcedure);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accountingProcedure
        restAccountingProcedureMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountingProcedure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingProcedureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccountingProcedure() throws Exception {
        // Initialize the database
        insertedAccountingProcedure = accountingProcedureRepository.saveAndFlush(accountingProcedure);
        accountingProcedureSearchRepository.save(accountingProcedure);

        // Search the accountingProcedure
        restAccountingProcedureMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountingProcedure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingProcedure.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    protected long getRepositoryCount() {
        return accountingProcedureRepository.count();
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

    protected AccountingProcedure getPersistedAccountingProcedure(AccountingProcedure accountingProcedure) {
        return accountingProcedureRepository.findById(accountingProcedure.getId()).orElseThrow();
    }

    protected void assertPersistedAccountingProcedureToMatchAllProperties(AccountingProcedure expectedAccountingProcedure) {
        assertAccountingProcedureAllPropertiesEquals(
            expectedAccountingProcedure,
            getPersistedAccountingProcedure(expectedAccountingProcedure)
        );
    }

    protected void assertPersistedAccountingProcedureToMatchUpdatableProperties(AccountingProcedure expectedAccountingProcedure) {
        assertAccountingProcedureAllUpdatablePropertiesEquals(
            expectedAccountingProcedure,
            getPersistedAccountingProcedure(expectedAccountingProcedure)
        );
    }
}
