package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.AccountTemplateAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bulut.attendance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.AccountTemplate;
import com.bulut.attendance.domain.enumeration.AccountLevelInTree;
import com.bulut.attendance.domain.enumeration.AccountType;
import com.bulut.attendance.domain.enumeration.AccountingFormulaType;
import com.bulut.attendance.repository.AccountTemplateRepository;
import com.bulut.attendance.repository.search.AccountTemplateSearchRepository;
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
 * Integration tests for the {@link AccountTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountTemplateResourceIT {

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

    private static final String ENTITY_API_URL = "/api/account-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/account-templates/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountTemplateRepository accountTemplateRepository;

    @Autowired
    private AccountTemplateSearchRepository accountTemplateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountTemplateMockMvc;

    private AccountTemplate accountTemplate;

    private AccountTemplate insertedAccountTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountTemplate createEntity(EntityManager em) {
        AccountTemplate accountTemplate = new AccountTemplate()
            .title(DEFAULT_TITLE)
            .code(DEFAULT_CODE)
            .level(DEFAULT_LEVEL)
            .levelTitle(DEFAULT_LEVEL_TITLE)
            .type(DEFAULT_TYPE)
            .levelInTree(DEFAULT_LEVEL_IN_TREE)
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .typeInFormula(DEFAULT_TYPE_IN_FORMULA);
        return accountTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountTemplate createUpdatedEntity(EntityManager em) {
        AccountTemplate accountTemplate = new AccountTemplate()
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .level(UPDATED_LEVEL)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .type(UPDATED_TYPE)
            .levelInTree(UPDATED_LEVEL_IN_TREE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .typeInFormula(UPDATED_TYPE_IN_FORMULA);
        return accountTemplate;
    }

    @BeforeEach
    public void initTest() {
        accountTemplate = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccountTemplate != null) {
            accountTemplateRepository.delete(insertedAccountTemplate);
            accountTemplateSearchRepository.delete(insertedAccountTemplate);
            insertedAccountTemplate = null;
        }
    }

    @Test
    @Transactional
    void createAccountTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        // Create the AccountTemplate
        var returnedAccountTemplate = om.readValue(
            restAccountTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountTemplate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountTemplate.class
        );

        // Validate the AccountTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccountTemplateUpdatableFieldsEquals(returnedAccountTemplate, getPersistedAccountTemplate(returnedAccountTemplate));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAccountTemplate = returnedAccountTemplate;
    }

    @Test
    @Transactional
    void createAccountTemplateWithExistingId() throws Exception {
        // Create the AccountTemplate with an existing ID
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAccountTemplates() throws Exception {
        // Initialize the database
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);

        // Get all the accountTemplateList
        restAccountTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountTemplate.getId().toString())))
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
    void getAccountTemplate() throws Exception {
        // Initialize the database
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);

        // Get the accountTemplate
        restAccountTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, accountTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountTemplate.getId().toString()))
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
    void getNonExistingAccountTemplate() throws Exception {
        // Get the accountTemplate
        restAccountTemplateMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountTemplate() throws Exception {
        // Initialize the database
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountTemplateSearchRepository.save(accountTemplate);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());

        // Update the accountTemplate
        AccountTemplate updatedAccountTemplate = accountTemplateRepository.findById(accountTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccountTemplate are not directly saved in db
        em.detach(updatedAccountTemplate);
        updatedAccountTemplate
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .level(UPDATED_LEVEL)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .type(UPDATED_TYPE)
            .levelInTree(UPDATED_LEVEL_IN_TREE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .typeInFormula(UPDATED_TYPE_IN_FORMULA);

        restAccountTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccountTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountTemplateToMatchAllProperties(updatedAccountTemplate);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccountTemplate> accountTemplateSearchList = Streamable.of(accountTemplateSearchRepository.findAll()).toList();
                AccountTemplate testAccountTemplateSearch = accountTemplateSearchList.get(searchDatabaseSizeAfter - 1);

                assertAccountTemplateAllPropertiesEquals(testAccountTemplateSearch, updatedAccountTemplate);
            });
    }

    @Test
    @Transactional
    void putNonExistingAccountTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        accountTemplate.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        accountTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        accountTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAccountTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountTemplate using partial update
        AccountTemplate partialUpdatedAccountTemplate = new AccountTemplate();
        partialUpdatedAccountTemplate.setId(accountTemplate.getId());

        partialUpdatedAccountTemplate
            .title(UPDATED_TITLE)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .levelInTree(UPDATED_LEVEL_IN_TREE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT);

        restAccountTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AccountTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccountTemplate, accountTemplate),
            getPersistedAccountTemplate(accountTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccountTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountTemplate using partial update
        AccountTemplate partialUpdatedAccountTemplate = new AccountTemplate();
        partialUpdatedAccountTemplate.setId(accountTemplate.getId());

        partialUpdatedAccountTemplate
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .level(UPDATED_LEVEL)
            .levelTitle(UPDATED_LEVEL_TITLE)
            .type(UPDATED_TYPE)
            .levelInTree(UPDATED_LEVEL_IN_TREE)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .typeInFormula(UPDATED_TYPE_IN_FORMULA);

        restAccountTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountTemplate))
            )
            .andExpect(status().isOk());

        // Validate the AccountTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountTemplateUpdatableFieldsEquals(
            partialUpdatedAccountTemplate,
            getPersistedAccountTemplate(partialUpdatedAccountTemplate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccountTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        accountTemplate.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        accountTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        accountTemplate.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accountTemplate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAccountTemplate() throws Exception {
        // Initialize the database
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);
        accountTemplateRepository.save(accountTemplate);
        accountTemplateSearchRepository.save(accountTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accountTemplate
        restAccountTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountTemplate.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAccountTemplate() throws Exception {
        // Initialize the database
        insertedAccountTemplate = accountTemplateRepository.saveAndFlush(accountTemplate);
        accountTemplateSearchRepository.save(accountTemplate);

        // Search the accountTemplate
        restAccountTemplateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountTemplate.getId().toString())))
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
        return accountTemplateRepository.count();
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

    protected AccountTemplate getPersistedAccountTemplate(AccountTemplate accountTemplate) {
        return accountTemplateRepository.findById(accountTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedAccountTemplateToMatchAllProperties(AccountTemplate expectedAccountTemplate) {
        assertAccountTemplateAllPropertiesEquals(expectedAccountTemplate, getPersistedAccountTemplate(expectedAccountTemplate));
    }

    protected void assertPersistedAccountTemplateToMatchUpdatableProperties(AccountTemplate expectedAccountTemplate) {
        assertAccountTemplateAllUpdatablePropertiesEquals(expectedAccountTemplate, getPersistedAccountTemplate(expectedAccountTemplate));
    }
}
