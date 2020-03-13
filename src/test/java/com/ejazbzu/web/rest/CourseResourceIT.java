package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.Course;
import com.ejazbzu.domain.Document;
import com.ejazbzu.domain.Department;
import com.ejazbzu.domain.Student;
import com.ejazbzu.repository.CourseRepository;
import com.ejazbzu.service.CourseService;
import com.ejazbzu.service.dto.CourseDTO;
import com.ejazbzu.service.mapper.CourseMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.CourseCriteria;
import com.ejazbzu.service.CourseQueryService;

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
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class CourseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMG_URL = "BBBBBBBBBB";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseQueryService courseQueryService;

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

    private MockMvc restCourseMockMvc;

    private Course course;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseResource courseResource = new CourseResource(courseService, courseQueryService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
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
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .name(DEFAULT_NAME)
            .symbol(DEFAULT_SYMBOL)
            .description(DEFAULT_DESCRIPTION)
            .coverImgUrl(DEFAULT_COVER_IMG_URL);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        course.getDocuments().add(document);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .description(UPDATED_DESCRIPTION)
            .coverImgUrl(UPDATED_COVER_IMG_URL);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createUpdatedEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        course.getDocuments().add(document);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getCoverImgUrl()).isEqualTo(DEFAULT_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDto(course);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setName(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setSymbol(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)));
    }

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.coverImgUrl").value(DEFAULT_COVER_IMG_URL));
    }


    @Test
    @Transactional
    public void getCoursesByIdFiltering() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        Long id = course.getId();

        defaultCourseShouldBeFound("id.equals=" + id);
        defaultCourseShouldNotBeFound("id.notEquals=" + id);

        defaultCourseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name equals to DEFAULT_NAME
        defaultCourseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name not equals to DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the courseList where name not equals to UPDATED_NAME
        defaultCourseShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCourseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name is not null
        defaultCourseShouldBeFound("name.specified=true");

        // Get all the courseList where name is null
        defaultCourseShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByNameContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name contains DEFAULT_NAME
        defaultCourseShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the courseList where name contains UPDATED_NAME
        defaultCourseShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name does not contain DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the courseList where name does not contain UPDATED_NAME
        defaultCourseShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCoursesBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where symbol equals to DEFAULT_SYMBOL
        defaultCourseShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the courseList where symbol equals to UPDATED_SYMBOL
        defaultCourseShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCoursesBySymbolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where symbol not equals to DEFAULT_SYMBOL
        defaultCourseShouldNotBeFound("symbol.notEquals=" + DEFAULT_SYMBOL);

        // Get all the courseList where symbol not equals to UPDATED_SYMBOL
        defaultCourseShouldBeFound("symbol.notEquals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCoursesBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultCourseShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the courseList where symbol equals to UPDATED_SYMBOL
        defaultCourseShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCoursesBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where symbol is not null
        defaultCourseShouldBeFound("symbol.specified=true");

        // Get all the courseList where symbol is null
        defaultCourseShouldNotBeFound("symbol.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesBySymbolContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where symbol contains DEFAULT_SYMBOL
        defaultCourseShouldBeFound("symbol.contains=" + DEFAULT_SYMBOL);

        // Get all the courseList where symbol contains UPDATED_SYMBOL
        defaultCourseShouldNotBeFound("symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCoursesBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where symbol does not contain DEFAULT_SYMBOL
        defaultCourseShouldNotBeFound("symbol.doesNotContain=" + DEFAULT_SYMBOL);

        // Get all the courseList where symbol does not contain UPDATED_SYMBOL
        defaultCourseShouldBeFound("symbol.doesNotContain=" + UPDATED_SYMBOL);
    }


    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description equals to DEFAULT_DESCRIPTION
        defaultCourseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description not equals to DEFAULT_DESCRIPTION
        defaultCourseShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description not equals to UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description is not null
        defaultCourseShouldBeFound("description.specified=true");

        // Get all the courseList where description is null
        defaultCourseShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description contains DEFAULT_DESCRIPTION
        defaultCourseShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description contains UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description does not contain DEFAULT_DESCRIPTION
        defaultCourseShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description does not contain UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCoursesByCoverImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where coverImgUrl equals to DEFAULT_COVER_IMG_URL
        defaultCourseShouldBeFound("coverImgUrl.equals=" + DEFAULT_COVER_IMG_URL);

        // Get all the courseList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultCourseShouldNotBeFound("coverImgUrl.equals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCoursesByCoverImgUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where coverImgUrl not equals to DEFAULT_COVER_IMG_URL
        defaultCourseShouldNotBeFound("coverImgUrl.notEquals=" + DEFAULT_COVER_IMG_URL);

        // Get all the courseList where coverImgUrl not equals to UPDATED_COVER_IMG_URL
        defaultCourseShouldBeFound("coverImgUrl.notEquals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCoursesByCoverImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where coverImgUrl in DEFAULT_COVER_IMG_URL or UPDATED_COVER_IMG_URL
        defaultCourseShouldBeFound("coverImgUrl.in=" + DEFAULT_COVER_IMG_URL + "," + UPDATED_COVER_IMG_URL);

        // Get all the courseList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultCourseShouldNotBeFound("coverImgUrl.in=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCoursesByCoverImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where coverImgUrl is not null
        defaultCourseShouldBeFound("coverImgUrl.specified=true");

        // Get all the courseList where coverImgUrl is null
        defaultCourseShouldNotBeFound("coverImgUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByCoverImgUrlContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where coverImgUrl contains DEFAULT_COVER_IMG_URL
        defaultCourseShouldBeFound("coverImgUrl.contains=" + DEFAULT_COVER_IMG_URL);

        // Get all the courseList where coverImgUrl contains UPDATED_COVER_IMG_URL
        defaultCourseShouldNotBeFound("coverImgUrl.contains=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllCoursesByCoverImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where coverImgUrl does not contain DEFAULT_COVER_IMG_URL
        defaultCourseShouldNotBeFound("coverImgUrl.doesNotContain=" + DEFAULT_COVER_IMG_URL);

        // Get all the courseList where coverImgUrl does not contain UPDATED_COVER_IMG_URL
        defaultCourseShouldBeFound("coverImgUrl.doesNotContain=" + UPDATED_COVER_IMG_URL);
    }


//    @Test
//    @Transactional
//    public void getAllCoursesByDocumentIsEqualToSomething() throws Exception {
//        // Get already existing entity
//        Document document = course.getDocument();
//        courseRepository.saveAndFlush(course);
//        Long documentId = document.getId();
//
//        // Get all the courseList where document equals to documentId
//        defaultCourseShouldBeFound("documentId.equals=" + documentId);
//
//        // Get all the courseList where document equals to documentId + 1
//        defaultCourseShouldNotBeFound("documentId.equals=" + (documentId + 1));
//    }


    @Test
    @Transactional
    public void getAllCoursesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Department department = DepartmentResourceIT.createEntity(em);
        em.persist(department);
        em.flush();
        course.setDepartment(department);
        courseRepository.saveAndFlush(course);
        Long departmentId = department.getId();

        // Get all the courseList where department equals to departmentId
        defaultCourseShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the courseList where department equals to departmentId + 1
        defaultCourseShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }


    @Test
    @Transactional
    public void getAllCoursesByStudentsIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Student students = StudentResourceIT.createEntity(em);
        em.persist(students);
        em.flush();
        course.addStudents(students);
        courseRepository.saveAndFlush(course);
        Long studentsId = students.getId();

        // Get all the courseList where students equals to studentsId
        defaultCourseShouldBeFound("studentsId.equals=" + studentsId);

        // Get all the courseList where students equals to studentsId + 1
        defaultCourseShouldNotBeFound("studentsId.equals=" + (studentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)));

        // Check, that the count call also returns 1
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .description(UPDATED_DESCRIPTION)
            .coverImgUrl(UPDATED_COVER_IMG_URL);
        CourseDTO courseDTO = courseMapper.toDto(updatedCourse);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getCoverImgUrl()).isEqualTo(UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
