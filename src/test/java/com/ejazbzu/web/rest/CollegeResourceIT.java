package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.College;
import com.ejazbzu.domain.Department;
import com.ejazbzu.domain.University;
import com.ejazbzu.repository.CollegeRepository;
import com.ejazbzu.service.CollegeService;
import com.ejazbzu.service.dto.CollegeDTO;
import com.ejazbzu.service.mapper.CollegeMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.CollegeCriteria;
import com.ejazbzu.service.CollegeQueryService;

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
 * Integration tests for the {@link CollegeResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class CollegeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMG_URL = "BBBBBBBBBB";

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private CollegeQueryService collegeQueryService;

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

    private MockMvc restCollegeMockMvc;

    private College college;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollegeResource collegeResource = new CollegeResource(collegeService, collegeQueryService);
        this.restCollegeMockMvc = MockMvcBuilders.standaloneSetup(collegeResource)
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
    public static College createEntity(EntityManager em) {
        College college = new College()
            .name(DEFAULT_NAME)
            .symbol(DEFAULT_SYMBOL)
            .coverImgUrl(DEFAULT_COVER_IMG_URL);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        college.getDepartments().add(department);
        return college;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static College createUpdatedEntity(EntityManager em) {
        College college = new College()
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .coverImgUrl(UPDATED_COVER_IMG_URL);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        college.getDepartments().add(department);
        return college;
    }

    @BeforeEach
    public void initTest() {
        college = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollege() throws Exception {
        int databaseSizeBeforeCreate = collegeRepository.findAll().size();

        // Create the College
        CollegeDTO collegeDTO = collegeMapper.toDto(college);
        restCollegeMockMvc.perform(post("/api/colleges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
            .andExpect(status().isCreated());

        // Validate the College in the database
        List<College> collegeList = collegeRepository.findAll();
        assertThat(collegeList).hasSize(databaseSizeBeforeCreate + 1);
        College testCollege = collegeList.get(collegeList.size() - 1);
        assertThat(testCollege.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollege.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testCollege.getCoverImgUrl()).isEqualTo(DEFAULT_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void createCollegeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collegeRepository.findAll().size();

        // Create the College with an existing ID
        college.setId(1L);
        CollegeDTO collegeDTO = collegeMapper.toDto(college);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollegeMockMvc.perform(post("/api/colleges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the College in the database
        List<College> collegeList = collegeRepository.findAll();
        assertThat(collegeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = collegeRepository.findAll().size();
        // set the field null
        college.setName(null);

        // Create the College, which fails.
        CollegeDTO collegeDTO = collegeMapper.toDto(college);

        restCollegeMockMvc.perform(post("/api/colleges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
            .andExpect(status().isBadRequest());

        List<College> collegeList = collegeRepository.findAll();
        assertThat(collegeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = collegeRepository.findAll().size();
        // set the field null
        college.setSymbol(null);

        // Create the College, which fails.
        CollegeDTO collegeDTO = collegeMapper.toDto(college);

        restCollegeMockMvc.perform(post("/api/colleges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
            .andExpect(status().isBadRequest());

        List<College> collegeList = collegeRepository.findAll();
        assertThat(collegeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllColleges() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList
        restCollegeMockMvc.perform(get("/api/colleges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(college.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)));
    }

    @Test
    @Transactional
    public void getCollege() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get the college
        restCollegeMockMvc.perform(get("/api/colleges/{id}", college.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(college.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.coverImgUrl").value(DEFAULT_COVER_IMG_URL));
    }


    @Test
    @Transactional
    public void getCollegesByIdFiltering() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        Long id = college.getId();

        defaultCollegeShouldBeFound("id.equals=" + id);
        defaultCollegeShouldNotBeFound("id.notEquals=" + id);

        defaultCollegeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollegeShouldNotBeFound("id.greaterThan=" + id);

        defaultCollegeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollegeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCollegesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where name equals to DEFAULT_NAME
        defaultCollegeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the collegeList where name equals to UPDATED_NAME
        defaultCollegeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollegesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where name not equals to DEFAULT_NAME
        defaultCollegeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the collegeList where name not equals to UPDATED_NAME
        defaultCollegeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollegesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCollegeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the collegeList where name equals to UPDATED_NAME
        defaultCollegeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollegesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where name is not null
        defaultCollegeShouldBeFound("name.specified=true");

        // Get all the collegeList where name is null
        defaultCollegeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCollegesByNameContainsSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where name contains DEFAULT_NAME
        defaultCollegeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the collegeList where name contains UPDATED_NAME
        defaultCollegeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCollegesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where name does not contain DEFAULT_NAME
        defaultCollegeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the collegeList where name does not contain UPDATED_NAME
        defaultCollegeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCollegesBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where symbol equals to DEFAULT_SYMBOL
        defaultCollegeShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the collegeList where symbol equals to UPDATED_SYMBOL
        defaultCollegeShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCollegesBySymbolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where symbol not equals to DEFAULT_SYMBOL
        defaultCollegeShouldNotBeFound("symbol.notEquals=" + DEFAULT_SYMBOL);

        // Get all the collegeList where symbol not equals to UPDATED_SYMBOL
        defaultCollegeShouldBeFound("symbol.notEquals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCollegesBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultCollegeShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the collegeList where symbol equals to UPDATED_SYMBOL
        defaultCollegeShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCollegesBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where symbol is not null
        defaultCollegeShouldBeFound("symbol.specified=true");

        // Get all the collegeList where symbol is null
        defaultCollegeShouldNotBeFound("symbol.specified=false");
    }
                @Test
    @Transactional
    public void getAllCollegesBySymbolContainsSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where symbol contains DEFAULT_SYMBOL
        defaultCollegeShouldBeFound("symbol.contains=" + DEFAULT_SYMBOL);

        // Get all the collegeList where symbol contains UPDATED_SYMBOL
        defaultCollegeShouldNotBeFound("symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCollegesBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where symbol does not contain DEFAULT_SYMBOL
        defaultCollegeShouldNotBeFound("symbol.doesNotContain=" + DEFAULT_SYMBOL);

        // Get all the collegeList where symbol does not contain UPDATED_SYMBOL
        defaultCollegeShouldBeFound("symbol.doesNotContain=" + UPDATED_SYMBOL);
    }


    @Test
    @Transactional
    public void getAllCollegesByCoverImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where coverImgUrl equals to DEFAULT_COVER_IMG_URL
        defaultCollegeShouldBeFound("coverImgUrl.equals=" + DEFAULT_COVER_IMG_URL);

        // Get all the collegeList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultCollegeShouldNotBeFound("coverImgUrl.equals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCollegesByCoverImgUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where coverImgUrl not equals to DEFAULT_COVER_IMG_URL
        defaultCollegeShouldNotBeFound("coverImgUrl.notEquals=" + DEFAULT_COVER_IMG_URL);

        // Get all the collegeList where coverImgUrl not equals to UPDATED_COVER_IMG_URL
        defaultCollegeShouldBeFound("coverImgUrl.notEquals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCollegesByCoverImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where coverImgUrl in DEFAULT_COVER_IMG_URL or UPDATED_COVER_IMG_URL
        defaultCollegeShouldBeFound("coverImgUrl.in=" + DEFAULT_COVER_IMG_URL + "," + UPDATED_COVER_IMG_URL);

        // Get all the collegeList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultCollegeShouldNotBeFound("coverImgUrl.in=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCollegesByCoverImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where coverImgUrl is not null
        defaultCollegeShouldBeFound("coverImgUrl.specified=true");

        // Get all the collegeList where coverImgUrl is null
        defaultCollegeShouldNotBeFound("coverImgUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllCollegesByCoverImgUrlContainsSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where coverImgUrl contains DEFAULT_COVER_IMG_URL
        defaultCollegeShouldBeFound("coverImgUrl.contains=" + DEFAULT_COVER_IMG_URL);

        // Get all the collegeList where coverImgUrl contains UPDATED_COVER_IMG_URL
        defaultCollegeShouldNotBeFound("coverImgUrl.contains=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCollegesByCoverImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        // Get all the collegeList where coverImgUrl does not contain DEFAULT_COVER_IMG_URL
        defaultCollegeShouldNotBeFound("coverImgUrl.doesNotContain=" + DEFAULT_COVER_IMG_URL);

        // Get all the collegeList where coverImgUrl does not contain UPDATED_COVER_IMG_URL
        defaultCollegeShouldBeFound("coverImgUrl.doesNotContain=" + UPDATED_COVER_IMG_URL);
    }


//    @Test
//    @Transactional
//    public void getAllCollegesByDepartmentIsEqualToSomething() throws Exception {
//        // Get already existing entity
//        Department department = college.getDepartment();
//        collegeRepository.saveAndFlush(college);
//        Long departmentId = department.getId();
//
//        // Get all the collegeList where department equals to departmentId
//        defaultCollegeShouldBeFound("departmentId.equals=" + departmentId);
//
//        // Get all the collegeList where department equals to departmentId + 1
//        defaultCollegeShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
//    }


    @Test
    @Transactional
    public void getAllCollegesByUniversityIsEqualToSomething() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);
        University university = UniversityResourceIT.createEntity(em);
        em.persist(university);
        em.flush();
        college.setUniversity(university);
        collegeRepository.saveAndFlush(college);
        Long universityId = university.getId();

        // Get all the collegeList where university equals to universityId
        defaultCollegeShouldBeFound("universityId.equals=" + universityId);

        // Get all the collegeList where university equals to universityId + 1
        defaultCollegeShouldNotBeFound("universityId.equals=" + (universityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollegeShouldBeFound(String filter) throws Exception {
        restCollegeMockMvc.perform(get("/api/colleges?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(college.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)));

        // Check, that the count call also returns 1
        restCollegeMockMvc.perform(get("/api/colleges/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollegeShouldNotBeFound(String filter) throws Exception {
        restCollegeMockMvc.perform(get("/api/colleges?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollegeMockMvc.perform(get("/api/colleges/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCollege() throws Exception {
        // Get the college
        restCollegeMockMvc.perform(get("/api/colleges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollege() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        int databaseSizeBeforeUpdate = collegeRepository.findAll().size();

        // Update the college
        College updatedCollege = collegeRepository.findById(college.getId()).get();
        // Disconnect from session so that the updates on updatedCollege are not directly saved in db
        em.detach(updatedCollege);
        updatedCollege
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .coverImgUrl(UPDATED_COVER_IMG_URL);
        CollegeDTO collegeDTO = collegeMapper.toDto(updatedCollege);

        restCollegeMockMvc.perform(put("/api/colleges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
            .andExpect(status().isOk());

        // Validate the College in the database
        List<College> collegeList = collegeRepository.findAll();
        assertThat(collegeList).hasSize(databaseSizeBeforeUpdate);
        College testCollege = collegeList.get(collegeList.size() - 1);
        assertThat(testCollege.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollege.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCollege.getCoverImgUrl()).isEqualTo(UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingCollege() throws Exception {
        int databaseSizeBeforeUpdate = collegeRepository.findAll().size();

        // Create the College
        CollegeDTO collegeDTO = collegeMapper.toDto(college);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollegeMockMvc.perform(put("/api/colleges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the College in the database
        List<College> collegeList = collegeRepository.findAll();
        assertThat(collegeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCollege() throws Exception {
        // Initialize the database
        collegeRepository.saveAndFlush(college);

        int databaseSizeBeforeDelete = collegeRepository.findAll().size();

        // Delete the college
        restCollegeMockMvc.perform(delete("/api/colleges/{id}", college.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<College> collegeList = collegeRepository.findAll();
        assertThat(collegeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
