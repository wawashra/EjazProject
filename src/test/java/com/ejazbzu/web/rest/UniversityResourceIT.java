package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.University;
import com.ejazbzu.domain.College;
import com.ejazbzu.repository.UniversityRepository;
import com.ejazbzu.service.UniversityService;
import com.ejazbzu.service.dto.UniversityDTO;
import com.ejazbzu.service.mapper.UniversityMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.UniversityCriteria;
import com.ejazbzu.service.UniversityQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ejazbzu.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UniversityResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class UniversityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UniversityMapper universityMapper;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UniversityQueryService universityQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUniversityMockMvc;

    private University university;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UniversityResource universityResource = new UniversityResource(universityService, universityQueryService);
        this.restUniversityMockMvc = MockMvcBuilders.standaloneSetup(universityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static University createEntity(EntityManager em) {
        University university = new University()
            .name(DEFAULT_NAME)
            .symbol(DEFAULT_SYMBOL);
        // Add required entity
        College college;
        if (TestUtil.findAll(em, College.class).isEmpty()) {
            college = CollegeResourceIT.createEntity(em);
            em.persist(college);
            em.flush();
        } else {
            college = TestUtil.findAll(em, College.class).get(0);
        }
        university.getColleges().add(college);
        return university;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static University createUpdatedEntity(EntityManager em) {
        University university = new University()
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL);
        // Add required entity
        College college;
        if (TestUtil.findAll(em, College.class).isEmpty()) {
            college = CollegeResourceIT.createUpdatedEntity(em);
            em.persist(college);
            em.flush();
        } else {
            college = TestUtil.findAll(em, College.class).get(0);
        }
        university.getColleges().add(college);
        return university;
    }

    @BeforeEach
    public void initTest() {
        university = createEntity(em);
    }

    @Test
    @Transactional
    public void createUniversity() throws Exception {
        int databaseSizeBeforeCreate = universityRepository.findAll().size();

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);
        restUniversityMockMvc.perform(post("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isCreated());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeCreate + 1);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUniversity.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
    }

    @Test
    @Transactional
    public void createUniversityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = universityRepository.findAll().size();

        // Create the University with an existing ID
        university.setId(1L);
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniversityMockMvc.perform(post("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = universityRepository.findAll().size();
        // set the field null
        university.setName(null);

        // Create the University, which fails.
        UniversityDTO universityDTO = universityMapper.toDto(university);

        restUniversityMockMvc.perform(post("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isBadRequest());

        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = universityRepository.findAll().size();
        // set the field null
        university.setSymbol(null);

        // Create the University, which fails.
        UniversityDTO universityDTO = universityMapper.toDto(university);

        restUniversityMockMvc.perform(post("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isBadRequest());

        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUniversities() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList
        restUniversityMockMvc.perform(get("/api/universities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(university.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)));
    }

    @Test
    @Transactional
    public void getUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get the university
        restUniversityMockMvc.perform(get("/api/universities/{id}", university.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(university.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL));
    }


    @Test
    @Transactional
    public void getUniversitiesByIdFiltering() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        Long id = university.getId();

        defaultUniversityShouldBeFound("id.equals=" + id);
        defaultUniversityShouldNotBeFound("id.notEquals=" + id);

        defaultUniversityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUniversityShouldNotBeFound("id.greaterThan=" + id);

        defaultUniversityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUniversityShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUniversitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where name equals to DEFAULT_NAME
        defaultUniversityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the universityList where name equals to UPDATED_NAME
        defaultUniversityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUniversitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where name not equals to DEFAULT_NAME
        defaultUniversityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the universityList where name not equals to UPDATED_NAME
        defaultUniversityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUniversitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUniversityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the universityList where name equals to UPDATED_NAME
        defaultUniversityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUniversitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where name is not null
        defaultUniversityShouldBeFound("name.specified=true");

        // Get all the universityList where name is null
        defaultUniversityShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllUniversitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where name contains DEFAULT_NAME
        defaultUniversityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the universityList where name contains UPDATED_NAME
        defaultUniversityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUniversitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where name does not contain DEFAULT_NAME
        defaultUniversityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the universityList where name does not contain UPDATED_NAME
        defaultUniversityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllUniversitiesBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where symbol equals to DEFAULT_SYMBOL
        defaultUniversityShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the universityList where symbol equals to UPDATED_SYMBOL
        defaultUniversityShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllUniversitiesBySymbolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where symbol not equals to DEFAULT_SYMBOL
        defaultUniversityShouldNotBeFound("symbol.notEquals=" + DEFAULT_SYMBOL);

        // Get all the universityList where symbol not equals to UPDATED_SYMBOL
        defaultUniversityShouldBeFound("symbol.notEquals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllUniversitiesBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultUniversityShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the universityList where symbol equals to UPDATED_SYMBOL
        defaultUniversityShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllUniversitiesBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where symbol is not null
        defaultUniversityShouldBeFound("symbol.specified=true");

        // Get all the universityList where symbol is null
        defaultUniversityShouldNotBeFound("symbol.specified=false");
    }
                @Test
    @Transactional
    public void getAllUniversitiesBySymbolContainsSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where symbol contains DEFAULT_SYMBOL
        defaultUniversityShouldBeFound("symbol.contains=" + DEFAULT_SYMBOL);

        // Get all the universityList where symbol contains UPDATED_SYMBOL
        defaultUniversityShouldNotBeFound("symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllUniversitiesBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList where symbol does not contain DEFAULT_SYMBOL
        defaultUniversityShouldNotBeFound("symbol.doesNotContain=" + DEFAULT_SYMBOL);

        // Get all the universityList where symbol does not contain UPDATED_SYMBOL
        defaultUniversityShouldBeFound("symbol.doesNotContain=" + UPDATED_SYMBOL);
    }


//    @Test
//    @Transactional
//    public void getAllUniversitiesByCollegeIsEqualToSomething() throws Exception {
//        // Get already existing entity
//        College college = university.getCollege();
//        universityRepository.saveAndFlush(university);
//        Long collegeId = college.getId();
//
//        // Get all the universityList where college equals to collegeId
//        defaultUniversityShouldBeFound("collegeId.equals=" + collegeId);
//
//        // Get all the universityList where college equals to collegeId + 1
//        defaultUniversityShouldNotBeFound("collegeId.equals=" + (collegeId + 1));
//    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUniversityShouldBeFound(String filter) throws Exception {
        restUniversityMockMvc.perform(get("/api/universities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(university.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)));

        // Check, that the count call also returns 1
        restUniversityMockMvc.perform(get("/api/universities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUniversityShouldNotBeFound(String filter) throws Exception {
        restUniversityMockMvc.perform(get("/api/universities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUniversityMockMvc.perform(get("/api/universities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUniversity() throws Exception {
        // Get the university
        restUniversityMockMvc.perform(get("/api/universities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        int databaseSizeBeforeUpdate = universityRepository.findAll().size();

        // Update the university
        University updatedUniversity = universityRepository.findById(university.getId()).get();
        // Disconnect from session so that the updates on updatedUniversity are not directly saved in db
        em.detach(updatedUniversity);
        updatedUniversity
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL);
        UniversityDTO universityDTO = universityMapper.toDto(updatedUniversity);

        restUniversityMockMvc.perform(put("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isOk());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUniversity.getSymbol()).isEqualTo(UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void updateNonExistingUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversityMockMvc.perform(put("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        int databaseSizeBeforeDelete = universityRepository.findAll().size();

        // Delete the university
        restUniversityMockMvc.perform(delete("/api/universities/{id}", university.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
