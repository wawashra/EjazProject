package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.Department;
import com.ejazbzu.domain.Course;
import com.ejazbzu.domain.College;
import com.ejazbzu.repository.DepartmentRepository;
import com.ejazbzu.service.DepartmentService;
import com.ejazbzu.service.dto.DepartmentDTO;
import com.ejazbzu.service.mapper.DepartmentMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.DepartmentCriteria;
import com.ejazbzu.service.DepartmentQueryService;

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
 * Integration tests for the {@link DepartmentResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class DepartmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMG_URL = "BBBBBBBBBB";

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentQueryService departmentQueryService;

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

    private MockMvc restDepartmentMockMvc;

    private Department department;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepartmentResource departmentResource = new DepartmentResource(departmentService, departmentQueryService);
        this.restDepartmentMockMvc = MockMvcBuilders.standaloneSetup(departmentResource)
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
    public static Department createEntity(EntityManager em) {
        Department department = new Department()
            .name(DEFAULT_NAME)
            .symbol(DEFAULT_SYMBOL)
            .coverImgUrl(DEFAULT_COVER_IMG_URL);
        // Add required entity
        Course course;
        if (TestUtil.findAll(em, Course.class).isEmpty()) {
            course = CourseResourceIT.createEntity(em);
            em.persist(course);
            em.flush();
        } else {
            course = TestUtil.findAll(em, Course.class).get(0);
        }
        department.getCourses().add(course);
        return department;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createUpdatedEntity(EntityManager em) {
        Department department = new Department()
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .coverImgUrl(UPDATED_COVER_IMG_URL);
        // Add required entity
        Course course;
        if (TestUtil.findAll(em, Course.class).isEmpty()) {
            course = CourseResourceIT.createUpdatedEntity(em);
            em.persist(course);
            em.flush();
        } else {
            course = TestUtil.findAll(em, Course.class).get(0);
        }
        department.getCourses().add(course);
        return department;
    }

    @BeforeEach
    public void initTest() {
        department = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartment() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);
        restDepartmentMockMvc.perform(post("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate + 1);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartment.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testDepartment.getCoverImgUrl()).isEqualTo(DEFAULT_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void createDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        // Create the Department with an existing ID
        department.setId(1L);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMockMvc.perform(post("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRepository.findAll().size();
        // set the field null
        department.setName(null);

        // Create the Department, which fails.
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        restDepartmentMockMvc.perform(post("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRepository.findAll().size();
        // set the field null
        department.setSymbol(null);

        // Create the Department, which fails.
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        restDepartmentMockMvc.perform(post("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartments() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList
        restDepartmentMockMvc.perform(get("/api/departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)));
    }

    @Test
    @Transactional
    public void getDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get the department
        restDepartmentMockMvc.perform(get("/api/departments/{id}", department.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(department.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.coverImgUrl").value(DEFAULT_COVER_IMG_URL));
    }


    @Test
    @Transactional
    public void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        Long id = department.getId();

        defaultDepartmentShouldBeFound("id.equals=" + id);
        defaultDepartmentShouldNotBeFound("id.notEquals=" + id);

        defaultDepartmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartmentShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDepartmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name equals to DEFAULT_NAME
        defaultDepartmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the departmentList where name equals to UPDATED_NAME
        defaultDepartmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name not equals to DEFAULT_NAME
        defaultDepartmentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the departmentList where name not equals to UPDATED_NAME
        defaultDepartmentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDepartmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the departmentList where name equals to UPDATED_NAME
        defaultDepartmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name is not null
        defaultDepartmentShouldBeFound("name.specified=true");

        // Get all the departmentList where name is null
        defaultDepartmentShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepartmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name contains DEFAULT_NAME
        defaultDepartmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the departmentList where name contains UPDATED_NAME
        defaultDepartmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where name does not contain DEFAULT_NAME
        defaultDepartmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the departmentList where name does not contain UPDATED_NAME
        defaultDepartmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllDepartmentsBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where symbol equals to DEFAULT_SYMBOL
        defaultDepartmentShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the departmentList where symbol equals to UPDATED_SYMBOL
        defaultDepartmentShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsBySymbolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where symbol not equals to DEFAULT_SYMBOL
        defaultDepartmentShouldNotBeFound("symbol.notEquals=" + DEFAULT_SYMBOL);

        // Get all the departmentList where symbol not equals to UPDATED_SYMBOL
        defaultDepartmentShouldBeFound("symbol.notEquals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultDepartmentShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the departmentList where symbol equals to UPDATED_SYMBOL
        defaultDepartmentShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where symbol is not null
        defaultDepartmentShouldBeFound("symbol.specified=true");

        // Get all the departmentList where symbol is null
        defaultDepartmentShouldNotBeFound("symbol.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepartmentsBySymbolContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where symbol contains DEFAULT_SYMBOL
        defaultDepartmentShouldBeFound("symbol.contains=" + DEFAULT_SYMBOL);

        // Get all the departmentList where symbol contains UPDATED_SYMBOL
        defaultDepartmentShouldNotBeFound("symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where symbol does not contain DEFAULT_SYMBOL
        defaultDepartmentShouldNotBeFound("symbol.doesNotContain=" + DEFAULT_SYMBOL);

        // Get all the departmentList where symbol does not contain UPDATED_SYMBOL
        defaultDepartmentShouldBeFound("symbol.doesNotContain=" + UPDATED_SYMBOL);
    }


    @Test
    @Transactional
    public void getAllDepartmentsByCoverImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where coverImgUrl equals to DEFAULT_COVER_IMG_URL
        defaultDepartmentShouldBeFound("coverImgUrl.equals=" + DEFAULT_COVER_IMG_URL);

        // Get all the departmentList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultDepartmentShouldNotBeFound("coverImgUrl.equals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByCoverImgUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where coverImgUrl not equals to DEFAULT_COVER_IMG_URL
        defaultDepartmentShouldNotBeFound("coverImgUrl.notEquals=" + DEFAULT_COVER_IMG_URL);

        // Get all the departmentList where coverImgUrl not equals to UPDATED_COVER_IMG_URL
        defaultDepartmentShouldBeFound("coverImgUrl.notEquals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByCoverImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where coverImgUrl in DEFAULT_COVER_IMG_URL or UPDATED_COVER_IMG_URL
        defaultDepartmentShouldBeFound("coverImgUrl.in=" + DEFAULT_COVER_IMG_URL + "," + UPDATED_COVER_IMG_URL);

        // Get all the departmentList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultDepartmentShouldNotBeFound("coverImgUrl.in=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByCoverImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where coverImgUrl is not null
        defaultDepartmentShouldBeFound("coverImgUrl.specified=true");

        // Get all the departmentList where coverImgUrl is null
        defaultDepartmentShouldNotBeFound("coverImgUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepartmentsByCoverImgUrlContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where coverImgUrl contains DEFAULT_COVER_IMG_URL
        defaultDepartmentShouldBeFound("coverImgUrl.contains=" + DEFAULT_COVER_IMG_URL);

        // Get all the departmentList where coverImgUrl contains UPDATED_COVER_IMG_URL
        defaultDepartmentShouldNotBeFound("coverImgUrl.contains=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByCoverImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where coverImgUrl does not contain DEFAULT_COVER_IMG_URL
        defaultDepartmentShouldNotBeFound("coverImgUrl.doesNotContain=" + DEFAULT_COVER_IMG_URL);

        // Get all the departmentList where coverImgUrl does not contain UPDATED_COVER_IMG_URL
        defaultDepartmentShouldBeFound("coverImgUrl.doesNotContain=" + UPDATED_COVER_IMG_URL);
    }


//    @Test
//    @Transactional
//    public void getAllDepartmentsByCourseIsEqualToSomething() throws Exception {
//        // Get already existing entity
//        Course course = department.getCourse();
//        departmentRepository.saveAndFlush(department);
//        Long courseId = course.getId();
//
//        // Get all the departmentList where course equals to courseId
//        defaultDepartmentShouldBeFound("courseId.equals=" + courseId);
//
//        // Get all the departmentList where course equals to courseId + 1
//        defaultDepartmentShouldNotBeFound("courseId.equals=" + (courseId + 1));
//    }


    @Test
    @Transactional
    public void getAllDepartmentsByCollegeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);
        College college = CollegeResourceIT.createEntity(em);
        em.persist(college);
        em.flush();
        department.setCollege(college);
        departmentRepository.saveAndFlush(department);
        Long collegeId = college.getId();

        // Get all the departmentList where college equals to collegeId
        defaultDepartmentShouldBeFound("collegeId.equals=" + collegeId);

        // Get all the departmentList where college equals to collegeId + 1
        defaultDepartmentShouldNotBeFound("collegeId.equals=" + (collegeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentShouldBeFound(String filter) throws Exception {
        restDepartmentMockMvc.perform(get("/api/departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)));

        // Check, that the count call also returns 1
        restDepartmentMockMvc.perform(get("/api/departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentShouldNotBeFound(String filter) throws Exception {
        restDepartmentMockMvc.perform(get("/api/departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentMockMvc.perform(get("/api/departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDepartment() throws Exception {
        // Get the department
        restDepartmentMockMvc.perform(get("/api/departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Update the department
        Department updatedDepartment = departmentRepository.findById(department.getId()).get();
        // Disconnect from session so that the updates on updatedDepartment are not directly saved in db
        em.detach(updatedDepartment);
        updatedDepartment
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .coverImgUrl(UPDATED_COVER_IMG_URL);
        DepartmentDTO departmentDTO = departmentMapper.toDto(updatedDepartment);

        restDepartmentMockMvc.perform(put("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isOk());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartment.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testDepartment.getCoverImgUrl()).isEqualTo(UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc.perform(put("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        int databaseSizeBeforeDelete = departmentRepository.findAll().size();

        // Delete the department
        restDepartmentMockMvc.perform(delete("/api/departments/{id}", department.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
