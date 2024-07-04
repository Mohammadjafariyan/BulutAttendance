package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.TransactionAccountAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.TransactionAccount;
import com.bulut.attendance.repository.TransactionAccountRepository;
import com.bulut.attendance.repository.search.TransactionAccountSearchRepository;
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
 * Integration tests for the {@link TransactionAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionAccountResourceIT {

    private static final BigDecimal DEFAULT_DEBIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREDIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/transaction-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/transaction-accounts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionAccountRepository transactionAccountRepository;

    @Autowired
    private TransactionAccountSearchRepository transactionAccountSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionAccountMockMvc;

    private TransactionAccount transactionAccount;

    private TransactionAccount insertedTransactionAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccount createEntity(EntityManager em) {
        TransactionAccount transactionAccount = new TransactionAccount()
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .creditAmount(DEFAULT_CREDIT_AMOUNT);
        return transactionAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionAccount createUpdatedEntity(EntityManager em) {
        TransactionAccount transactionAccount = new TransactionAccount()
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT);
        return transactionAccount;
    }

    @BeforeEach
    public void initTest() {
        transactionAccount = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTransactionAccount != null) {
            transactionAccountRepository.delete(insertedTransactionAccount);
            transactionAccountSearchRepository.delete(insertedTransactionAccount);
            insertedTransactionAccount = null;
        }
    }

    @Test
    @Transactional
    void createTransactionAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        // Create the TransactionAccount
        var returnedTransactionAccount = om.readValue(
            restTransactionAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionAccount)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionAccount.class
        );

        // Validate the TransactionAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTransactionAccountUpdatableFieldsEquals(
            returnedTransactionAccount,
            getPersistedTransactionAccount(returnedTransactionAccount)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTransactionAccount = returnedTransactionAccount;
    }

    @Test
    @Transactional
    void createTransactionAccountWithExistingId() throws Exception {
        // Create the TransactionAccount with an existing ID
        transactionAccount.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionAccount)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTransactionAccounts() throws Exception {
        // Initialize the database
        insertedTransactionAccount = transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get all the transactionAccountList
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(sameNumber(DEFAULT_DEBIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))));
    }

    @Test
    @Transactional
    void getTransactionAccount() throws Exception {
        // Initialize the database
        insertedTransactionAccount = transactionAccountRepository.saveAndFlush(transactionAccount);

        // Get the transactionAccount
        restTransactionAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionAccount.getId().intValue()))
            .andExpect(jsonPath("$.debitAmount").value(sameNumber(DEFAULT_DEBIT_AMOUNT)))
            .andExpect(jsonPath("$.creditAmount").value(sameNumber(DEFAULT_CREDIT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingTransactionAccount() throws Exception {
        // Get the transactionAccount
        restTransactionAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionAccount() throws Exception {
        // Initialize the database
        insertedTransactionAccount = transactionAccountRepository.saveAndFlush(transactionAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionAccountSearchRepository.save(transactionAccount);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());

        // Update the transactionAccount
        TransactionAccount updatedTransactionAccount = transactionAccountRepository.findById(transactionAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionAccount are not directly saved in db
        em.detach(updatedTransactionAccount);
        updatedTransactionAccount.debitAmount(UPDATED_DEBIT_AMOUNT).creditAmount(UPDATED_CREDIT_AMOUNT);

        restTransactionAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTransactionAccount))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionAccountToMatchAllProperties(updatedTransactionAccount);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TransactionAccount> transactionAccountSearchList = Streamable.of(
                    transactionAccountSearchRepository.findAll()
                ).toList();
                TransactionAccount testTransactionAccountSearch = transactionAccountSearchList.get(searchDatabaseSizeAfter - 1);

                assertTransactionAccountAllPropertiesEquals(testTransactionAccountSearch, updatedTransactionAccount);
            });
    }

    @Test
    @Transactional
    void putNonExistingTransactionAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        transactionAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        transactionAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        transactionAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTransactionAccountWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionAccount = transactionAccountRepository.saveAndFlush(transactionAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionAccount using partial update
        TransactionAccount partialUpdatedTransactionAccount = new TransactionAccount();
        partialUpdatedTransactionAccount.setId(transactionAccount.getId());

        partialUpdatedTransactionAccount.creditAmount(UPDATED_CREDIT_AMOUNT);

        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionAccount))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionAccount, transactionAccount),
            getPersistedTransactionAccount(transactionAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionAccountWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionAccount = transactionAccountRepository.saveAndFlush(transactionAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionAccount using partial update
        TransactionAccount partialUpdatedTransactionAccount = new TransactionAccount();
        partialUpdatedTransactionAccount.setId(transactionAccount.getId());

        partialUpdatedTransactionAccount.debitAmount(UPDATED_DEBIT_AMOUNT).creditAmount(UPDATED_CREDIT_AMOUNT);

        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionAccount))
            )
            .andExpect(status().isOk());

        // Validate the TransactionAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionAccountUpdatableFieldsEquals(
            partialUpdatedTransactionAccount,
            getPersistedTransactionAccount(partialUpdatedTransactionAccount)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransactionAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        transactionAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        transactionAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        transactionAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTransactionAccount() throws Exception {
        // Initialize the database
        insertedTransactionAccount = transactionAccountRepository.saveAndFlush(transactionAccount);
        transactionAccountRepository.save(transactionAccount);
        transactionAccountSearchRepository.save(transactionAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the transactionAccount
        restTransactionAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(transactionAccountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTransactionAccount() throws Exception {
        // Initialize the database
        insertedTransactionAccount = transactionAccountRepository.saveAndFlush(transactionAccount);
        transactionAccountSearchRepository.save(transactionAccount);

        // Search the transactionAccount
        restTransactionAccountMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(sameNumber(DEFAULT_DEBIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))));
    }

    protected long getRepositoryCount() {
        return transactionAccountRepository.count();
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

    protected TransactionAccount getPersistedTransactionAccount(TransactionAccount transactionAccount) {
        return transactionAccountRepository.findById(transactionAccount.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionAccountToMatchAllProperties(TransactionAccount expectedTransactionAccount) {
        assertTransactionAccountAllPropertiesEquals(expectedTransactionAccount, getPersistedTransactionAccount(expectedTransactionAccount));
    }

    protected void assertPersistedTransactionAccountToMatchUpdatableProperties(TransactionAccount expectedTransactionAccount) {
        assertTransactionAccountAllUpdatablePropertiesEquals(
            expectedTransactionAccount,
            getPersistedTransactionAccount(expectedTransactionAccount)
        );
    }
}
