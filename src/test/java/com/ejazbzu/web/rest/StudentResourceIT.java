package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.Student;
import com.ejazbzu.domain.User;
import com.ejazbzu.domain.University;
import com.ejazbzu.domain.Department;
import com.ejazbzu.domain.College;
import com.ejazbzu.domain.Document;
import com.ejazbzu.domain.Report;
import com.ejazbzu.domain.Course;
import com.ejazbzu.repository.StudentRepository;
import com.ejazbzu.service.StudentService;
import com.ejazbzu.service.dto.StudentDTO;
import com.ejazbzu.service.mapper.StudentMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.StudentCriteria;
import com.ejazbzu.service.StudentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.ejazbzu.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ejazbzu.domain.enumeration.Gender;
/**
 * Integration tests for the {@link StudentResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class StudentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTHDAY = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PROFILE_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMG_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMG_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STAR = false;
    private static final Boolean UPDATED_STAR = true;

    @Autowired
    private StudentRepository studentRepository;

    @Mock
    private StudentRepository studentRepositoryMock;

    @Autowired
    private StudentMapper studentMapper;

    @Mock
    private StudentService studentServiceMock;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentQueryService studentQueryService;

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

    private MockMvc restStudentMockMvc;

    private Student student;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudentResource studentResource = new StudentResource(studentService, studentQueryService);
        this.restStudentMockMvc = MockMvcBuilders.standaloneSetup(studentResource)
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
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .name(DEFAULT_NAME)
            .birthday(DEFAULT_BIRTHDAY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .gender(DEFAULT_GENDER)
            .profileImgUrl(DEFAULT_PROFILE_IMG_URL)
            .coverImgUrl(DEFAULT_COVER_IMG_URL)
            .star(DEFAULT_STAR);
        return student;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .gender(UPDATED_GENDER)
            .profileImgUrl(UPDATED_PROFILE_IMG_URL)
            .coverImgUrl(UPDATED_COVER_IMG_URL)
            .star(UPDATED_STAR);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudent.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testStudent.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testStudent.getProfileImgUrl()).isEqualTo(DEFAULT_PROFILE_IMG_URL);
        assertThat(testStudent.getCoverImgUrl()).isEqualTo(DEFAULT_COVER_IMG_URL);
        assertThat(testStudent.isStar()).isEqualTo(DEFAULT_STAR);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);
        StudentDTO studentDTO = studentMapper.toDto(student);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setName(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setBirthday(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setPhoneNumber(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setGender(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].profileImgUrl").value(hasItem(DEFAULT_PROFILE_IMG_URL)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllStudentsWithEagerRelationshipsIsEnabled() throws Exception {
        StudentResource studentResource = new StudentResource(studentServiceMock, studentQueryService);
        when(studentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restStudentMockMvc = MockMvcBuilders.standaloneSetup(studentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStudentMockMvc.perform(get("/api/students?eagerload=true"))
        .andExpect(status().isOk());

        verify(studentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllStudentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        StudentResource studentResource = new StudentResource(studentServiceMock, studentQueryService);
            when(studentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restStudentMockMvc = MockMvcBuilders.standaloneSetup(studentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStudentMockMvc.perform(get("/api/students?eagerload=true"))
        .andExpect(status().isOk());

            verify(studentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.profileImgUrl").value(DEFAULT_PROFILE_IMG_URL))
            .andExpect(jsonPath("$.coverImgUrl").value(DEFAULT_COVER_IMG_URL))
            .andExpect(jsonPath("$.star").value(DEFAULT_STAR.booleanValue()));
    }


    @Test
    @Transactional
    public void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentShouldBeFound("id.equals=" + id);
        defaultStudentShouldNotBeFound("id.notEquals=" + id);

        defaultStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStudentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where name equals to DEFAULT_NAME
        defaultStudentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the studentList where name equals to UPDATED_NAME
        defaultStudentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where name not equals to DEFAULT_NAME
        defaultStudentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the studentList where name not equals to UPDATED_NAME
        defaultStudentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStudentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the studentList where name equals to UPDATED_NAME
        defaultStudentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where name is not null
        defaultStudentShouldBeFound("name.specified=true");

        // Get all the studentList where name is null
        defaultStudentShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where name contains DEFAULT_NAME
        defaultStudentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the studentList where name contains UPDATED_NAME
        defaultStudentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where name does not contain DEFAULT_NAME
        defaultStudentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the studentList where name does not contain UPDATED_NAME
        defaultStudentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday equals to DEFAULT_BIRTHDAY
        defaultStudentShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the studentList where birthday equals to UPDATED_BIRTHDAY
        defaultStudentShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday not equals to DEFAULT_BIRTHDAY
        defaultStudentShouldNotBeFound("birthday.notEquals=" + DEFAULT_BIRTHDAY);

        // Get all the studentList where birthday not equals to UPDATED_BIRTHDAY
        defaultStudentShouldBeFound("birthday.notEquals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultStudentShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the studentList where birthday equals to UPDATED_BIRTHDAY
        defaultStudentShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday is not null
        defaultStudentShouldBeFound("birthday.specified=true");

        // Get all the studentList where birthday is null
        defaultStudentShouldNotBeFound("birthday.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday is greater than or equal to DEFAULT_BIRTHDAY
        defaultStudentShouldBeFound("birthday.greaterThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the studentList where birthday is greater than or equal to UPDATED_BIRTHDAY
        defaultStudentShouldNotBeFound("birthday.greaterThanOrEqual=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday is less than or equal to DEFAULT_BIRTHDAY
        defaultStudentShouldBeFound("birthday.lessThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the studentList where birthday is less than or equal to SMALLER_BIRTHDAY
        defaultStudentShouldNotBeFound("birthday.lessThanOrEqual=" + SMALLER_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday is less than DEFAULT_BIRTHDAY
        defaultStudentShouldNotBeFound("birthday.lessThan=" + DEFAULT_BIRTHDAY);

        // Get all the studentList where birthday is less than UPDATED_BIRTHDAY
        defaultStudentShouldBeFound("birthday.lessThan=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthdayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthday is greater than DEFAULT_BIRTHDAY
        defaultStudentShouldNotBeFound("birthday.greaterThan=" + DEFAULT_BIRTHDAY);

        // Get all the studentList where birthday is greater than SMALLER_BIRTHDAY
        defaultStudentShouldBeFound("birthday.greaterThan=" + SMALLER_BIRTHDAY);
    }


    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the studentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber is not null
        defaultStudentShouldBeFound("phoneNumber.specified=true");

        // Get all the studentList where phoneNumber is null
        defaultStudentShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllStudentsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender equals to DEFAULT_GENDER
        defaultStudentShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the studentList where gender equals to UPDATED_GENDER
        defaultStudentShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender not equals to DEFAULT_GENDER
        defaultStudentShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the studentList where gender not equals to UPDATED_GENDER
        defaultStudentShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultStudentShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the studentList where gender equals to UPDATED_GENDER
        defaultStudentShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender is not null
        defaultStudentShouldBeFound("gender.specified=true");

        // Get all the studentList where gender is null
        defaultStudentShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByProfileImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where profileImgUrl equals to DEFAULT_PROFILE_IMG_URL
        defaultStudentShouldBeFound("profileImgUrl.equals=" + DEFAULT_PROFILE_IMG_URL);

        // Get all the studentList where profileImgUrl equals to UPDATED_PROFILE_IMG_URL
        defaultStudentShouldNotBeFound("profileImgUrl.equals=" + UPDATED_PROFILE_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByProfileImgUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where profileImgUrl not equals to DEFAULT_PROFILE_IMG_URL
        defaultStudentShouldNotBeFound("profileImgUrl.notEquals=" + DEFAULT_PROFILE_IMG_URL);

        // Get all the studentList where profileImgUrl not equals to UPDATED_PROFILE_IMG_URL
        defaultStudentShouldBeFound("profileImgUrl.notEquals=" + UPDATED_PROFILE_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByProfileImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where profileImgUrl in DEFAULT_PROFILE_IMG_URL or UPDATED_PROFILE_IMG_URL
        defaultStudentShouldBeFound("profileImgUrl.in=" + DEFAULT_PROFILE_IMG_URL + "," + UPDATED_PROFILE_IMG_URL);

        // Get all the studentList where profileImgUrl equals to UPDATED_PROFILE_IMG_URL
        defaultStudentShouldNotBeFound("profileImgUrl.in=" + UPDATED_PROFILE_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByProfileImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where profileImgUrl is not null
        defaultStudentShouldBeFound("profileImgUrl.specified=true");

        // Get all the studentList where profileImgUrl is null
        defaultStudentShouldNotBeFound("profileImgUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByProfileImgUrlContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where profileImgUrl contains DEFAULT_PROFILE_IMG_URL
        defaultStudentShouldBeFound("profileImgUrl.contains=" + DEFAULT_PROFILE_IMG_URL);

        // Get all the studentList where profileImgUrl contains UPDATED_PROFILE_IMG_URL
        defaultStudentShouldNotBeFound("profileImgUrl.contains=" + UPDATED_PROFILE_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByProfileImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where profileImgUrl does not contain DEFAULT_PROFILE_IMG_URL
        defaultStudentShouldNotBeFound("profileImgUrl.doesNotContain=" + DEFAULT_PROFILE_IMG_URL);

        // Get all the studentList where profileImgUrl does not contain UPDATED_PROFILE_IMG_URL
        defaultStudentShouldBeFound("profileImgUrl.doesNotContain=" + UPDATED_PROFILE_IMG_URL);
    }


    @Test
    @Transactional
    public void getAllStudentsByCoverImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where coverImgUrl equals to DEFAULT_COVER_IMG_URL
        defaultStudentShouldBeFound("coverImgUrl.equals=" + DEFAULT_COVER_IMG_URL);

        // Get all the studentList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultStudentShouldNotBeFound("coverImgUrl.equals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByCoverImgUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where coverImgUrl not equals to DEFAULT_COVER_IMG_URL
        defaultStudentShouldNotBeFound("coverImgUrl.notEquals=" + DEFAULT_COVER_IMG_URL);

        // Get all the studentList where coverImgUrl not equals to UPDATED_COVER_IMG_URL
        defaultStudentShouldBeFound("coverImgUrl.notEquals=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByCoverImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where coverImgUrl in DEFAULT_COVER_IMG_URL or UPDATED_COVER_IMG_URL
        defaultStudentShouldBeFound("coverImgUrl.in=" + DEFAULT_COVER_IMG_URL + "," + UPDATED_COVER_IMG_URL);

        // Get all the studentList where coverImgUrl equals to UPDATED_COVER_IMG_URL
        defaultStudentShouldNotBeFound("coverImgUrl.in=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByCoverImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where coverImgUrl is not null
        defaultStudentShouldBeFound("coverImgUrl.specified=true");

        // Get all the studentList where coverImgUrl is null
        defaultStudentShouldNotBeFound("coverImgUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByCoverImgUrlContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where coverImgUrl contains DEFAULT_COVER_IMG_URL
        defaultStudentShouldBeFound("coverImgUrl.contains=" + DEFAULT_COVER_IMG_URL);

        // Get all the studentList where coverImgUrl contains UPDATED_COVER_IMG_URL
        defaultStudentShouldNotBeFound("coverImgUrl.contains=" + UPDATED_COVER_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByCoverImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where coverImgUrl does not contain DEFAULT_COVER_IMG_URL
        defaultStudentShouldNotBeFound("coverImgUrl.doesNotContain=" + DEFAULT_COVER_IMG_URL);

        // Get all the studentList where coverImgUrl does not contain UPDATED_COVER_IMG_URL
        defaultStudentShouldBeFound("coverImgUrl.doesNotContain=" + UPDATED_COVER_IMG_URL);
    }


    @Test
    @Transactional
    public void getAllStudentsByStarIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where star equals to DEFAULT_STAR
        defaultStudentShouldBeFound("star.equals=" + DEFAULT_STAR);

        // Get all the studentList where star equals to UPDATED_STAR
        defaultStudentShouldNotBeFound("star.equals=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllStudentsByStarIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where star not equals to DEFAULT_STAR
        defaultStudentShouldNotBeFound("star.notEquals=" + DEFAULT_STAR);

        // Get all the studentList where star not equals to UPDATED_STAR
        defaultStudentShouldBeFound("star.notEquals=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllStudentsByStarIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where star in DEFAULT_STAR or UPDATED_STAR
        defaultStudentShouldBeFound("star.in=" + DEFAULT_STAR + "," + UPDATED_STAR);

        // Get all the studentList where star equals to UPDATED_STAR
        defaultStudentShouldNotBeFound("star.in=" + UPDATED_STAR);
    }

    @Test
    @Transactional
    public void getAllStudentsByStarIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where star is not null
        defaultStudentShouldBeFound("star.specified=true");

        // Get all the studentList where star is null
        defaultStudentShouldNotBeFound("star.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        student.setUser(user);
        studentRepository.saveAndFlush(student);
        Long userId = user.getId();

        // Get all the studentList where user equals to userId
        defaultStudentShouldBeFound("userId.equals=" + userId);

        // Get all the studentList where user equals to userId + 1
        defaultStudentShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByUniversityIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        University university = UniversityResourceIT.createEntity(em);
        em.persist(university);
        em.flush();
        student.setUniversity(university);
        studentRepository.saveAndFlush(student);
        Long universityId = university.getId();

        // Get all the studentList where university equals to universityId
        defaultStudentShouldBeFound("universityId.equals=" + universityId);

        // Get all the studentList where university equals to universityId + 1
        defaultStudentShouldNotBeFound("universityId.equals=" + (universityId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Department department = DepartmentResourceIT.createEntity(em);
        em.persist(department);
        em.flush();
        student.setDepartment(department);
        studentRepository.saveAndFlush(student);
        Long departmentId = department.getId();

        // Get all the studentList where department equals to departmentId
        defaultStudentShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the studentList where department equals to departmentId + 1
        defaultStudentShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByCollegeIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        College college = CollegeResourceIT.createEntity(em);
        em.persist(college);
        em.flush();
        student.setCollege(college);
        studentRepository.saveAndFlush(student);
        Long collegeId = college.getId();

        // Get all the studentList where college equals to collegeId
        defaultStudentShouldBeFound("collegeId.equals=" + collegeId);

        // Get all the studentList where college equals to collegeId + 1
        defaultStudentShouldNotBeFound("collegeId.equals=" + (collegeId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Document documents = DocumentResourceIT.createEntity(em);
        em.persist(documents);
        em.flush();
        student.addDocuments(documents);
        studentRepository.saveAndFlush(student);
        Long documentsId = documents.getId();

        // Get all the studentList where documents equals to documentsId
        defaultStudentShouldBeFound("documentsId.equals=" + documentsId);

        // Get all the studentList where documents equals to documentsId + 1
        defaultStudentShouldNotBeFound("documentsId.equals=" + (documentsId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByReportIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Report report = ReportResourceIT.createEntity(em);
        em.persist(report);
        em.flush();
        student.addReport(report);
        studentRepository.saveAndFlush(student);
        Long reportId = report.getId();

        // Get all the studentList where report equals to reportId
        defaultStudentShouldBeFound("reportId.equals=" + reportId);

        // Get all the studentList where report equals to reportId + 1
        defaultStudentShouldNotBeFound("reportId.equals=" + (reportId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByCoursesIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Course courses = CourseResourceIT.createEntity(em);
        em.persist(courses);
        em.flush();
        student.addCourses(courses);
        studentRepository.saveAndFlush(student);
        Long coursesId = courses.getId();

        // Get all the studentList where courses equals to coursesId
        defaultStudentShouldBeFound("coursesId.equals=" + coursesId);

        // Get all the studentList where courses equals to coursesId + 1
        defaultStudentShouldNotBeFound("coursesId.equals=" + (coursesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].profileImgUrl").value(hasItem(DEFAULT_PROFILE_IMG_URL)))
            .andExpect(jsonPath("$.[*].coverImgUrl").value(hasItem(DEFAULT_COVER_IMG_URL)))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR.booleanValue())));

        // Check, that the count call also returns 1
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .gender(UPDATED_GENDER)
            .profileImgUrl(UPDATED_PROFILE_IMG_URL)
            .coverImgUrl(UPDATED_COVER_IMG_URL)
            .star(UPDATED_STAR);
        StudentDTO studentDTO = studentMapper.toDto(updatedStudent);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudent.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testStudent.getProfileImgUrl()).isEqualTo(UPDATED_PROFILE_IMG_URL);
        assertThat(testStudent.getCoverImgUrl()).isEqualTo(UPDATED_COVER_IMG_URL);
        assertThat(testStudent.isStar()).isEqualTo(UPDATED_STAR);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
