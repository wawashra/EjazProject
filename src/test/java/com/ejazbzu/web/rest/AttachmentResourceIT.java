package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.Attachment;
import com.ejazbzu.domain.Document;
import com.ejazbzu.domain.AttachmentType;
import com.ejazbzu.repository.AttachmentRepository;
import com.ejazbzu.service.AttachmentService;
import com.ejazbzu.service.dto.AttachmentDTO;
import com.ejazbzu.service.mapper.AttachmentMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.AttachmentCriteria;
import com.ejazbzu.service.AttachmentQueryService;

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
 * Integration tests for the {@link AttachmentResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class AttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_SIZE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HITS = 1;
    private static final Integer UPDATED_HITS = 2;
    private static final Integer SMALLER_HITS = 1 - 1;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentQueryService attachmentQueryService;

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

    private MockMvc restAttachmentMockMvc;

    private Attachment attachment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttachmentResource attachmentResource = new AttachmentResource(attachmentService, attachmentQueryService);
        this.restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
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
    public static Attachment createEntity(EntityManager em) {
        Attachment attachment = new Attachment()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .extension(DEFAULT_EXTENSION)
            .fileSize(DEFAULT_FILE_SIZE)
            .hits(DEFAULT_HITS);
        return attachment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createUpdatedEntity(EntityManager em) {
        Attachment attachment = new Attachment()
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .extension(UPDATED_EXTENSION)
            .fileSize(UPDATED_FILE_SIZE)
            .hits(UPDATED_HITS);
        return attachment;
    }

    @BeforeEach
    public void initTest() {
        attachment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttachment() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);
        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testAttachment.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testAttachment.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testAttachment.getHits()).isEqualTo(DEFAULT_HITS);
    }

    @Test
    @Transactional
    public void createAttachmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // Create the Attachment with an existing ID
        attachment.setId(1L);
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setName(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setUrl(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setExtension(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setFileSize(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)));
    }
    
    @Test
    @Transactional
    public void getAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", attachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.hits").value(DEFAULT_HITS));
    }


    @Test
    @Transactional
    public void getAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        Long id = attachment.getId();

        defaultAttachmentShouldBeFound("id.equals=" + id);
        defaultAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAttachmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name equals to DEFAULT_NAME
        defaultAttachmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name not equals to DEFAULT_NAME
        defaultAttachmentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the attachmentList where name not equals to UPDATED_NAME
        defaultAttachmentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttachmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name is not null
        defaultAttachmentShouldBeFound("name.specified=true");

        // Get all the attachmentList where name is null
        defaultAttachmentShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name contains DEFAULT_NAME
        defaultAttachmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the attachmentList where name contains UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name does not contain DEFAULT_NAME
        defaultAttachmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the attachmentList where name does not contain UPDATED_NAME
        defaultAttachmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where url equals to DEFAULT_URL
        defaultAttachmentShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the attachmentList where url equals to UPDATED_URL
        defaultAttachmentShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where url not equals to DEFAULT_URL
        defaultAttachmentShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the attachmentList where url not equals to UPDATED_URL
        defaultAttachmentShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where url in DEFAULT_URL or UPDATED_URL
        defaultAttachmentShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the attachmentList where url equals to UPDATED_URL
        defaultAttachmentShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where url is not null
        defaultAttachmentShouldBeFound("url.specified=true");

        // Get all the attachmentList where url is null
        defaultAttachmentShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllAttachmentsByUrlContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where url contains DEFAULT_URL
        defaultAttachmentShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the attachmentList where url contains UPDATED_URL
        defaultAttachmentShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where url does not contain DEFAULT_URL
        defaultAttachmentShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the attachmentList where url does not contain UPDATED_URL
        defaultAttachmentShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByExtensionIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where extension equals to DEFAULT_EXTENSION
        defaultAttachmentShouldBeFound("extension.equals=" + DEFAULT_EXTENSION);

        // Get all the attachmentList where extension equals to UPDATED_EXTENSION
        defaultAttachmentShouldNotBeFound("extension.equals=" + UPDATED_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByExtensionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where extension not equals to DEFAULT_EXTENSION
        defaultAttachmentShouldNotBeFound("extension.notEquals=" + DEFAULT_EXTENSION);

        // Get all the attachmentList where extension not equals to UPDATED_EXTENSION
        defaultAttachmentShouldBeFound("extension.notEquals=" + UPDATED_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByExtensionIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where extension in DEFAULT_EXTENSION or UPDATED_EXTENSION
        defaultAttachmentShouldBeFound("extension.in=" + DEFAULT_EXTENSION + "," + UPDATED_EXTENSION);

        // Get all the attachmentList where extension equals to UPDATED_EXTENSION
        defaultAttachmentShouldNotBeFound("extension.in=" + UPDATED_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByExtensionIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where extension is not null
        defaultAttachmentShouldBeFound("extension.specified=true");

        // Get all the attachmentList where extension is null
        defaultAttachmentShouldNotBeFound("extension.specified=false");
    }
                @Test
    @Transactional
    public void getAllAttachmentsByExtensionContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where extension contains DEFAULT_EXTENSION
        defaultAttachmentShouldBeFound("extension.contains=" + DEFAULT_EXTENSION);

        // Get all the attachmentList where extension contains UPDATED_EXTENSION
        defaultAttachmentShouldNotBeFound("extension.contains=" + UPDATED_EXTENSION);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByExtensionNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where extension does not contain DEFAULT_EXTENSION
        defaultAttachmentShouldNotBeFound("extension.doesNotContain=" + DEFAULT_EXTENSION);

        // Get all the attachmentList where extension does not contain UPDATED_EXTENSION
        defaultAttachmentShouldBeFound("extension.doesNotContain=" + UPDATED_EXTENSION);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize equals to DEFAULT_FILE_SIZE
        defaultAttachmentShouldBeFound("fileSize.equals=" + DEFAULT_FILE_SIZE);

        // Get all the attachmentList where fileSize equals to UPDATED_FILE_SIZE
        defaultAttachmentShouldNotBeFound("fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByFileSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize not equals to DEFAULT_FILE_SIZE
        defaultAttachmentShouldNotBeFound("fileSize.notEquals=" + DEFAULT_FILE_SIZE);

        // Get all the attachmentList where fileSize not equals to UPDATED_FILE_SIZE
        defaultAttachmentShouldBeFound("fileSize.notEquals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize in DEFAULT_FILE_SIZE or UPDATED_FILE_SIZE
        defaultAttachmentShouldBeFound("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE);

        // Get all the attachmentList where fileSize equals to UPDATED_FILE_SIZE
        defaultAttachmentShouldNotBeFound("fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize is not null
        defaultAttachmentShouldBeFound("fileSize.specified=true");

        // Get all the attachmentList where fileSize is null
        defaultAttachmentShouldNotBeFound("fileSize.specified=false");
    }
                @Test
    @Transactional
    public void getAllAttachmentsByFileSizeContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize contains DEFAULT_FILE_SIZE
        defaultAttachmentShouldBeFound("fileSize.contains=" + DEFAULT_FILE_SIZE);

        // Get all the attachmentList where fileSize contains UPDATED_FILE_SIZE
        defaultAttachmentShouldNotBeFound("fileSize.contains=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByFileSizeNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileSize does not contain DEFAULT_FILE_SIZE
        defaultAttachmentShouldNotBeFound("fileSize.doesNotContain=" + DEFAULT_FILE_SIZE);

        // Get all the attachmentList where fileSize does not contain UPDATED_FILE_SIZE
        defaultAttachmentShouldBeFound("fileSize.doesNotContain=" + UPDATED_FILE_SIZE);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits equals to DEFAULT_HITS
        defaultAttachmentShouldBeFound("hits.equals=" + DEFAULT_HITS);

        // Get all the attachmentList where hits equals to UPDATED_HITS
        defaultAttachmentShouldNotBeFound("hits.equals=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits not equals to DEFAULT_HITS
        defaultAttachmentShouldNotBeFound("hits.notEquals=" + DEFAULT_HITS);

        // Get all the attachmentList where hits not equals to UPDATED_HITS
        defaultAttachmentShouldBeFound("hits.notEquals=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits in DEFAULT_HITS or UPDATED_HITS
        defaultAttachmentShouldBeFound("hits.in=" + DEFAULT_HITS + "," + UPDATED_HITS);

        // Get all the attachmentList where hits equals to UPDATED_HITS
        defaultAttachmentShouldNotBeFound("hits.in=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits is not null
        defaultAttachmentShouldBeFound("hits.specified=true");

        // Get all the attachmentList where hits is null
        defaultAttachmentShouldNotBeFound("hits.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits is greater than or equal to DEFAULT_HITS
        defaultAttachmentShouldBeFound("hits.greaterThanOrEqual=" + DEFAULT_HITS);

        // Get all the attachmentList where hits is greater than or equal to UPDATED_HITS
        defaultAttachmentShouldNotBeFound("hits.greaterThanOrEqual=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits is less than or equal to DEFAULT_HITS
        defaultAttachmentShouldBeFound("hits.lessThanOrEqual=" + DEFAULT_HITS);

        // Get all the attachmentList where hits is less than or equal to SMALLER_HITS
        defaultAttachmentShouldNotBeFound("hits.lessThanOrEqual=" + SMALLER_HITS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsLessThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits is less than DEFAULT_HITS
        defaultAttachmentShouldNotBeFound("hits.lessThan=" + DEFAULT_HITS);

        // Get all the attachmentList where hits is less than UPDATED_HITS
        defaultAttachmentShouldBeFound("hits.lessThan=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByHitsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where hits is greater than DEFAULT_HITS
        defaultAttachmentShouldNotBeFound("hits.greaterThan=" + DEFAULT_HITS);

        // Get all the attachmentList where hits is greater than SMALLER_HITS
        defaultAttachmentShouldBeFound("hits.greaterThan=" + SMALLER_HITS);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);
        Document document = DocumentResourceIT.createEntity(em);
        em.persist(document);
        em.flush();
        attachment.setDocument(document);
        attachmentRepository.saveAndFlush(attachment);
        Long documentId = document.getId();

        // Get all the attachmentList where document equals to documentId
        defaultAttachmentShouldBeFound("documentId.equals=" + documentId);

        // Get all the attachmentList where document equals to documentId + 1
        defaultAttachmentShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }


    @Test
    @Transactional
    public void getAllAttachmentsByAttachmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);
        AttachmentType attachmentType = AttachmentTypeResourceIT.createEntity(em);
        em.persist(attachmentType);
        em.flush();
        attachment.setAttachmentType(attachmentType);
        attachmentRepository.saveAndFlush(attachment);
        Long attachmentTypeId = attachmentType.getId();

        // Get all the attachmentList where attachmentType equals to attachmentTypeId
        defaultAttachmentShouldBeFound("attachmentTypeId.equals=" + attachmentTypeId);

        // Get all the attachmentList where attachmentType equals to attachmentTypeId + 1
        defaultAttachmentShouldNotBeFound("attachmentTypeId.equals=" + (attachmentTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttachmentShouldBeFound(String filter) throws Exception {
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)));

        // Check, that the count call also returns 1
        restAttachmentMockMvc.perform(get("/api/attachments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttachmentShouldNotBeFound(String filter) throws Exception {
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttachmentMockMvc.perform(get("/api/attachments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAttachment() throws Exception {
        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment
        Attachment updatedAttachment = attachmentRepository.findById(attachment.getId()).get();
        // Disconnect from session so that the updates on updatedAttachment are not directly saved in db
        em.detach(updatedAttachment);
        updatedAttachment
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .extension(UPDATED_EXTENSION)
            .fileSize(UPDATED_FILE_SIZE)
            .hits(UPDATED_HITS);
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(updatedAttachment);

        restAttachmentMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testAttachment.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testAttachment.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testAttachment.getHits()).isEqualTo(UPDATED_HITS);
    }

    @Test
    @Transactional
    public void updateNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeDelete = attachmentRepository.findAll().size();

        // Delete the attachment
        restAttachmentMockMvc.perform(delete("/api/attachments/{id}", attachment.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
