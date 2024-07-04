package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.AccountHesabAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.AccountHesab;
import com.bulut.attendance.domain.enumeration.AccountLevelInTree;
import com.bulut.attendance.domain.enumeration.AccountType;
import com.bulut.attendance.domain.enumeration.AccountingFormulaType;
import com.bulut.attendance.repository.AccountHesabRepository;
import com.bulut.attendance.repository.search.AccountHesabSearchRepository;
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
 * Integration tests for the {@link AccountHesabResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountHesabResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String DEFAULT_LEVEL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL_TITLE = "BBBBBBBBBB";

    private static final AccountType DEFAULT_TYPE = AccountType.Credit;
    private static final AccountType UPDATED_TYPE = AccountType.Debit;

    private static final AccountLevelInTree DEFAULT_LEVEL_IN_TREE = AccountLevelInTree.GROUP;
    private static final AccountLevelInTree UPDATED_LEVEL_IN_TREE = AccountLevelInTree.KOL;

    private static final BigDecimal DEFAULT_DEBIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREDIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_AMOUNT = new BigDecimal(2);

    private static final AccountingFormulaType DEFAULT_TYPE_IN_FORMULA = AccountingFormulaType.ASSETS;
    private static final AccountingFormulaType UPDATED_TYPE_IN_FORMULA = AccountingFormulaType.EXPENSES;

    private static final String ENTITY_API_URL = "/api/account-hesabs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/account-hesabs/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountHesabRepository accountHesabRepository;

    @Autowired
    private AccountHesabSearchRepository accountHesabSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountHesabMockMvc;

    private AccountHesab accountHesab;

    private AccountHesab insertedAccountHesab;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountHesab createEntity(EntityManager em) {
        AccountHesab accountHesab = new AccountHesab()
            .title(DEFAULT_TITLE)
            .code(DEFAULT_CODE)
            .level(DEFAULT_LEVEL)
            .levelTitle(DEFAULT_LEVEL_TITLE)
            .type(DEFAULT_TYPE)
            .levelInTree(DEFAULT_LEVEL_IN_TREE)
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .typeInFormula(DEFAULT_TYPE_IN_FORMULA);
        return accountHesab;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountHesab createUpdatedEntity(EntityManager em) {
        AccountHesab accountHesab = new AccountHesab()
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .level(UPDATED_LEVEL)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .type(UPDATED_TYPE)
            .levelInTree(UPDATED_LEVEL_IN_TREE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .typeInFormula(UPDATED_TYPE_IN_FORMULA);
        return accountHesab;
    }

    @BeforeEach
    public void initTest() {
        accountHesab = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccountHesab != null) {
            accountHesabRepository.delete(insertedAccountHesab);
            accountHesabSearchRepository.delete(insertedAccountHesab);
            insertedAccountHesab = null;
        }
    }

    @Test
    @Transactional
    void createAccountHesab() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        // Create the AccountHesab
        var returnedAccountHesab = om.readValue(
            restAccountHesabMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountHesab)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountHesab.class
        );

        // Validate the AccountHesab in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccountHesabUpdatableFieldsEquals(returnedAccountHesab, getPersistedAccountHesab(returnedAccountHesab));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccountHesab = returnedAccountHesab;
    }

    @Test
    @Transactional
    void createAccountHesabWithExistingId() throws Exception {
        // Create the AccountHesab with an existing ID
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountHesabMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountHesab)))
            .andExpect(status().isBadRequest());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccountHesabs() throws Exception {
        // Initialize the database
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);

        // Get all the accountHesabList
        restAccountHesabMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountHesab.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].levelTitle").value(hasItem(DEFAULT_LEVEL_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelInTree").value(hasItem(DEFAULT_LEVEL_IN_TREE.toString())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(sameNumber(DEFAULT_DEBIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].typeInFormula").value(hasItem(DEFAULT_TYPE_IN_FORMULA.toString())));
    }

    @Test
    @Transactional
    void getAccountHesab() throws Exception {
        // Initialize the database
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);

        // Get the accountHesab
        restAccountHesabMockMvc
            .perform(get(ENTITY_API_URL_ID, accountHesab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountHesab.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.levelTitle").value(DEFAULT_LEVEL_TITLE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.levelInTree").value(DEFAULT_LEVEL_IN_TREE.toString()))
            .andExpect(jsonPath("$.debitAmount").value(sameNumber(DEFAULT_DEBIT_AMOUNT)))
            .andExpect(jsonPath("$.creditAmount").value(sameNumber(DEFAULT_CREDIT_AMOUNT)))
            .andExpect(jsonPath("$.typeInFormula").value(DEFAULT_TYPE_IN_FORMULA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccountHesab() throws Exception {
        // Get the accountHesab
        restAccountHesabMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountHesab() throws Exception {
        // Initialize the database
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountHesabSearchRepository.save(accountHesab);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());

        // Update the accountHesab
        AccountHesab updatedAccountHesab = accountHesabRepository.findById(accountHesab.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccountHesab are not directly saved in db
        em.detach(updatedAccountHesab);
        updatedAccountHesab
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .level(UPDATED_LEVEL)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .type(UPDATED_TYPE)
            .levelInTree(UPDATED_LEVEL_IN_TREE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .typeInFormula(UPDATED_TYPE_IN_FORMULA);

        restAccountHesabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountHesab.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccountHesab))
            )
            .andExpect(status().isOk());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountHesabToMatchAllProperties(updatedAccountHesab);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccountHesab> accountHesabSearchList = Streamable.of(accountHesabSearchRepository.findAll()).toList();
                AccountHesab testAccountHesabSearch = accountHesabSearchList.get(searchDatabaseSizeAfter - 1);

                assertAccountHesabAllPropertiesEquals(testAccountHesabSearch, updatedAccountHesab);
            });
    }

    @Test
    @Transactional
    void putNonExistingAccountHesab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        accountHesab.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountHesabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountHesab.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountHesab))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountHesab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        accountHesab.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountHesabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountHesab))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountHesab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        accountHesab.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountHesabMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountHesab)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccountHesabWithPatch() throws Exception {
        // Initialize the database
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountHesab using partial update
        AccountHesab partialUpdatedAccountHesab = new AccountHesab();
        partialUpdatedAccountHesab.setId(accountHesab.getId());

        partialUpdatedAccountHesab
            .title(UPDATED_TITLE)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .type(UPDATED_TYPE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .typeInFormula(UPDATED_TYPE_IN_FORMULA);

        restAccountHesabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountHesab.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountHesab))
            )
            .andExpect(status().isOk());

        // Validate the AccountHesab in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountHesabUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccountHesab, accountHesab),
            getPersistedAccountHesab(accountHesab)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccountHesabWithPatch() throws Exception {
        // Initialize the database
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountHesab using partial update
        AccountHesab partialUpdatedAccountHesab = new AccountHesab();
        partialUpdatedAccountHesab.setId(accountHesab.getId());

        partialUpdatedAccountHesab
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .level(UPDATED_LEVEL)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .type(UPDATED_TYPE)
            .levelInTree(UPDATED_LEVEL_IN_TREE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .typeInFormula(UPDATED_TYPE_IN_FORMULA);

        restAccountHesabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountHesab.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountHesab))
            )
            .andExpect(status().isOk());

        // Validate the AccountHesab in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountHesabUpdatableFieldsEquals(partialUpdatedAccountHesab, getPersistedAccountHesab(partialUpdatedAccountHesab));
    }

    @Test
    @Transactional
    void patchNonExistingAccountHesab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        accountHesab.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountHesabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountHesab.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountHesab))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountHesab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        accountHesab.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountHesabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountHesab))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountHesab() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        accountHesab.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountHesabMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accountHesab)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountHesab in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccountHesab() throws Exception {
        // Initialize the database
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);
        accountHesabRepository.save(accountHesab);
        accountHesabSearchRepository.save(accountHesab);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accountHesab
        restAccountHesabMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountHesab.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountHesabSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccountHesab() throws Exception {
        // Initialize the database
        insertedAccountHesab = accountHesabRepository.saveAndFlush(accountHesab);
        accountHesabSearchRepository.save(accountHesab);

        // Search the accountHesab
        restAccountHesabMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountHesab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountHesab.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].levelTitle").value(hasItem(DEFAULT_LEVEL_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelInTree").value(hasItem(DEFAULT_LEVEL_IN_TREE.toString())))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(sameNumber(DEFAULT_DEBIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].typeInFormula").value(hasItem(DEFAULT_TYPE_IN_FORMULA.toString())));
    }

    protected long getRepositoryCount() {
        return accountHesabRepository.count();
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

    protected AccountHesab getPersistedAccountHesab(AccountHesab accountHesab) {
        return accountHesabRepository.findById(accountHesab.getId()).orElseThrow();
    }

    protected void assertPersistedAccountHesabToMatchAllProperties(AccountHesab expectedAccountHesab) {
        assertAccountHesabAllPropertiesEquals(expectedAccountHesab, getPersistedAccountHesab(expectedAccountHesab));
    }

    protected void assertPersistedAccountHesabToMatchUpdatableProperties(AccountHesab expectedAccountHesab) {
        assertAccountHesabAllUpdatablePropertiesEquals(expectedAccountHesab, getPersistedAccountHesab(expectedAccountHesab));
    }
}
