package com.bulut.attendance.web.rest;

import static com.bulut.attendance.domain.PersonnelAsserts.*;
import static com.bulut.attendance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bulut.attendance.IntegrationTest;
import com.bulut.attendance.domain.Personnel;
import com.bulut.attendance.repository.PersonnelRepository;
import com.bulut.attendance.repository.search.PersonnelSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PersonnelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonnelResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REQUITMENT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_REQUITMENT_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER = "AAAAAAAAAA";
    private static final String UPDATED_FATHER = "BBBBBBBBBB";

    private static final String DEFAULT_SHENASNAME = "AAAAAAAAAA";
    private static final String UPDATED_SHENASNAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAHALESODUR = "AAAAAAAAAA";
    private static final String UPDATED_MAHALESODUR = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTHDAY = "AAAAAAAAAA";
    private static final String UPDATED_BIRTHDAY = "BBBBBBBBBB";

    private static final String DEFAULT_IS_SINGLE = "AAAAAAAAAA";
    private static final String UPDATED_IS_SINGLE = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_EDUCATION = "AAAAAAAAAA";
    private static final String UPDATED_LAST_EDUCATION = "BBBBBBBBBB";

    private static final String DEFAULT_EDUCATION_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATION_FIELD = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHILDREN = 42;
    private static final Integer UPDATED_CHILDREN = 43;

    private static final String ENTITY_API_URL = "/api/personnel";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/personnel/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private PersonnelSearchRepository personnelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonnelMockMvc;

    private Personnel personnel;

    private Personnel insertedPersonnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnel createEntity(EntityManager em) {
        Personnel personnel = new Personnel()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .requitmentDate(DEFAULT_REQUITMENT_DATE)
            .father(DEFAULT_FATHER)
            .shenasname(DEFAULT_SHENASNAME)
            .mahalesodur(DEFAULT_MAHALESODUR)
            .birthday(DEFAULT_BIRTHDAY)
            .isSingle(DEFAULT_IS_SINGLE)
            .lastEducation(DEFAULT_LAST_EDUCATION)
            .educationField(DEFAULT_EDUCATION_FIELD)
            .children(DEFAULT_CHILDREN);
        return personnel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnel createUpdatedEntity(EntityManager em) {
        Personnel personnel = new Personnel()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .requitmentDate(UPDATED_REQUITMENT_DATE)
            .father(UPDATED_FATHER)
            .shenasname(UPDATED_SHENASNAME)
            .mahalesodur(UPDATED_MAHALESODUR)
            .birthday(UPDATED_BIRTHDAY)
            .isSingle(UPDATED_IS_SINGLE)
            .lastEducation(UPDATED_LAST_EDUCATION)
            .educationField(UPDATED_EDUCATION_FIELD)
            .children(UPDATED_CHILDREN);
        return personnel;
    }

    @BeforeEach
    public void initTest() {
        personnel = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPersonnel != null) {
            personnelRepository.delete(insertedPersonnel);
            personnelSearchRepository.delete(insertedPersonnel);
            insertedPersonnel = null;
        }
    }

    @Test
    @Transactional
    void createPersonnel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        // Create the Personnel
        var returnedPersonnel = om.readValue(
            restPersonnelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Personnel.class
        );

        // Validate the Personnel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPersonnelUpdatableFieldsEquals(returnedPersonnel, getPersistedPersonnel(returnedPersonnel));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPersonnel = returnedPersonnel;
    }

    @Test
    @Transactional
    void createPersonnelWithExistingId() throws Exception {
        // Create the Personnel with an existing ID
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnel)))
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPersonnel() throws Exception {
        // Initialize the database
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);

        // Get all the personnelList
        restPersonnelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnel.getId().toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].requitmentDate").value(hasItem(DEFAULT_REQUITMENT_DATE)))
            .andExpect(jsonPath("$.[*].father").value(hasItem(DEFAULT_FATHER)))
            .andExpect(jsonPath("$.[*].shenasname").value(hasItem(DEFAULT_SHENASNAME)))
            .andExpect(jsonPath("$.[*].mahalesodur").value(hasItem(DEFAULT_MAHALESODUR)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY)))
            .andExpect(jsonPath("$.[*].isSingle").value(hasItem(DEFAULT_IS_SINGLE)))
            .andExpect(jsonPath("$.[*].lastEducation").value(hasItem(DEFAULT_LAST_EDUCATION)))
            .andExpect(jsonPath("$.[*].educationField").value(hasItem(DEFAULT_EDUCATION_FIELD)))
            .andExpect(jsonPath("$.[*].children").value(hasItem(DEFAULT_CHILDREN)));
    }

    @Test
    @Transactional
    void getPersonnel() throws Exception {
        // Initialize the database
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);

        // Get the personnel
        restPersonnelMockMvc
            .perform(get(ENTITY_API_URL_ID, personnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personnel.getId().toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.requitmentDate").value(DEFAULT_REQUITMENT_DATE))
            .andExpect(jsonPath("$.father").value(DEFAULT_FATHER))
            .andExpect(jsonPath("$.shenasname").value(DEFAULT_SHENASNAME))
            .andExpect(jsonPath("$.mahalesodur").value(DEFAULT_MAHALESODUR))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY))
            .andExpect(jsonPath("$.isSingle").value(DEFAULT_IS_SINGLE))
            .andExpect(jsonPath("$.lastEducation").value(DEFAULT_LAST_EDUCATION))
            .andExpect(jsonPath("$.educationField").value(DEFAULT_EDUCATION_FIELD))
            .andExpect(jsonPath("$.children").value(DEFAULT_CHILDREN));
    }

    @Test
    @Transactional
    void getNonExistingPersonnel() throws Exception {
        // Get the personnel
        restPersonnelMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonnel() throws Exception {
        // Initialize the database
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        personnelSearchRepository.save(personnel);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());

        // Update the personnel
        Personnel updatedPersonnel = personnelRepository.findById(personnel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonnel are not directly saved in db
        em.detach(updatedPersonnel);
        updatedPersonnel
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .requitmentDate(UPDATED_REQUITMENT_DATE)
            .father(UPDATED_FATHER)
            .shenasname(UPDATED_SHENASNAME)
            .mahalesodur(UPDATED_MAHALESODUR)
            .birthday(UPDATED_BIRTHDAY)
            .isSingle(UPDATED_IS_SINGLE)
            .lastEducation(UPDATED_LAST_EDUCATION)
            .educationField(UPDATED_EDUCATION_FIELD)
            .children(UPDATED_CHILDREN);

        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonnel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonnelToMatchAllProperties(updatedPersonnel);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Personnel> personnelSearchList = Streamable.of(personnelSearchRepository.findAll()).toList();
                Personnel testPersonnelSearch = personnelSearchList.get(searchDatabaseSizeAfter - 1);

                assertPersonnelAllPropertiesEquals(testPersonnelSearch, updatedPersonnel);
            });
    }

    @Test
    @Transactional
    void putNonExistingPersonnel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        personnel.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personnel.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonnel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        personnel.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonnel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        personnel.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personnel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePersonnelWithPatch() throws Exception {
        // Initialize the database
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personnel using partial update
        Personnel partialUpdatedPersonnel = new Personnel();
        partialUpdatedPersonnel.setId(personnel.getId());

        partialUpdatedPersonnel
            .lastName(UPDATED_LAST_NAME)
            .requitmentDate(UPDATED_REQUITMENT_DATE)
            .mahalesodur(UPDATED_MAHALESODUR)
            .isSingle(UPDATED_IS_SINGLE)
            .educationField(UPDATED_EDUCATION_FIELD)
            .children(UPDATED_CHILDREN);

        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonnelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonnel, personnel),
            getPersistedPersonnel(personnel)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonnelWithPatch() throws Exception {
        // Initialize the database
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personnel using partial update
        Personnel partialUpdatedPersonnel = new Personnel();
        partialUpdatedPersonnel.setId(personnel.getId());

        partialUpdatedPersonnel
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .requitmentDate(UPDATED_REQUITMENT_DATE)
            .father(UPDATED_FATHER)
            .shenasname(UPDATED_SHENASNAME)
            .mahalesodur(UPDATED_MAHALESODUR)
            .birthday(UPDATED_BIRTHDAY)
            .isSingle(UPDATED_IS_SINGLE)
            .lastEducation(UPDATED_LAST_EDUCATION)
            .educationField(UPDATED_EDUCATION_FIELD)
            .children(UPDATED_CHILDREN);

        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonnelUpdatableFieldsEquals(partialUpdatedPersonnel, getPersistedPersonnel(partialUpdatedPersonnel));
    }

    @Test
    @Transactional
    void patchNonExistingPersonnel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        personnel.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonnel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        personnel.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonnel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        personnel.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personnel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePersonnel() throws Exception {
        // Initialize the database
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);
        personnelRepository.save(personnel);
        personnelSearchRepository.save(personnel);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the personnel
        restPersonnelMockMvc
            .perform(delete(ENTITY_API_URL_ID, personnel.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(personnelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPersonnel() throws Exception {
        // Initialize the database
        insertedPersonnel = personnelRepository.saveAndFlush(personnel);
        personnelSearchRepository.save(personnel);

        // Search the personnel
        restPersonnelMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + personnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnel.getId().toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].requitmentDate").value(hasItem(DEFAULT_REQUITMENT_DATE)))
            .andExpect(jsonPath("$.[*].father").value(hasItem(DEFAULT_FATHER)))
            .andExpect(jsonPath("$.[*].shenasname").value(hasItem(DEFAULT_SHENASNAME)))
            .andExpect(jsonPath("$.[*].mahalesodur").value(hasItem(DEFAULT_MAHALESODUR)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY)))
            .andExpect(jsonPath("$.[*].isSingle").value(hasItem(DEFAULT_IS_SINGLE)))
            .andExpect(jsonPath("$.[*].lastEducation").value(hasItem(DEFAULT_LAST_EDUCATION)))
            .andExpect(jsonPath("$.[*].educationField").value(hasItem(DEFAULT_EDUCATION_FIELD)))
            .andExpect(jsonPath("$.[*].children").value(hasItem(DEFAULT_CHILDREN)));
    }

    protected long getRepositoryCount() {
        return personnelRepository.count();
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

    protected Personnel getPersistedPersonnel(Personnel personnel) {
        return personnelRepository.findById(personnel.getId()).orElseThrow();
    }

    protected void assertPersistedPersonnelToMatchAllProperties(Personnel expectedPersonnel) {
        assertPersonnelAllPropertiesEquals(expectedPersonnel, getPersistedPersonnel(expectedPersonnel));
    }

    protected void assertPersistedPersonnelToMatchUpdatableProperties(Personnel expectedPersonnel) {
        assertPersonnelAllUpdatablePropertiesEquals(expectedPersonnel, getPersistedPersonnel(expectedPersonnel));
    }
}
