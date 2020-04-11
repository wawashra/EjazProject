package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.Document;
import com.ejazbzu.domain.Attachment;
import com.ejazbzu.domain.Report;
import com.ejazbzu.domain.Tag;
import com.ejazbzu.domain.Course;
import com.ejazbzu.domain.DocumentType;
import com.ejazbzu.domain.Student;
import com.ejazbzu.repository.DocumentRepository;
import com.ejazbzu.service.DocumentService;
import com.ejazbzu.service.dto.DocumentDTO;
import com.ejazbzu.service.mapper.DocumentMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.DocumentCriteria;
import com.ejazbzu.service.DocumentQueryService;

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
import java.util.ArrayList;
import java.util.List;

import static com.ejazbzu.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class DocumentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING_SUM = 1;
    private static final Integer UPDATED_RATING_SUM = 2;
    private static final Integer SMALLER_RATING_SUM = 1 - 1;

    private static final Integer DEFAULT_RATING_NUMBER = 1;
    private static final Integer UPDATED_RATING_NUMBER = 2;
    private static final Integer SMALLER_RATING_NUMBER = 1 - 1;

    private static final Integer DEFAULT_VIEW = 1;
    private static final Integer UPDATED_VIEW = 2;
    private static final Integer SMALLER_VIEW = 1 - 1;

    @Autowired
    private DocumentRepository documentRepository;

    @Mock
    private DocumentRepository documentRepositoryMock;

    @Autowired
    private DocumentMapper documentMapper;

    @Mock
    private DocumentService documentServiceMock;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentQueryService documentQueryService;

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

    private MockMvc restDocumentMockMvc;

    private Document document;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentResource documentResource = new DocumentResource(documentService, documentQueryService);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
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
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .title(DEFAULT_TITLE)
            .active(DEFAULT_ACTIVE)
            .description(DEFAULT_DESCRIPTION)
            .ratingSum(DEFAULT_RATING_SUM)
            .ratingNumber(DEFAULT_RATING_NUMBER)
            .view(DEFAULT_VIEW);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity(em);
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).get(0);
        }
        document.getAttachments().add(attachment);
        return document;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document()
            .title(UPDATED_TITLE)
            .active(UPDATED_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .ratingSum(UPDATED_RATING_SUM)
            .ratingNumber(UPDATED_RATING_NUMBER)
            .view(UPDATED_VIEW);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity(em);
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).get(0);
        }
        document.getAttachments().add(attachment);
        return document;
    }

    @BeforeEach
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDocument.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocument.getRatingSum()).isEqualTo(DEFAULT_RATING_SUM);
        assertThat(testDocument.getRatingNumber()).isEqualTo(DEFAULT_RATING_NUMBER);
        assertThat(testDocument.getView()).isEqualTo(DEFAULT_VIEW);
    }

    @Test
    @Transactional
    public void createDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document with an existing ID
        document.setId(1L);
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setTitle(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].ratingSum").value(hasItem(DEFAULT_RATING_SUM)))
            .andExpect(jsonPath("$.[*].ratingNumber").value(hasItem(DEFAULT_RATING_NUMBER)))
            .andExpect(jsonPath("$.[*].view").value(hasItem(DEFAULT_VIEW)));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        DocumentResource documentResource = new DocumentResource(documentServiceMock, documentQueryService);
        when(documentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDocumentMockMvc.perform(get("/api/documents?eagerload=true"))
        .andExpect(status().isOk());

        verify(documentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        DocumentResource documentResource = new DocumentResource(documentServiceMock, documentQueryService);
            when(documentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDocumentMockMvc.perform(get("/api/documents?eagerload=true"))
        .andExpect(status().isOk());

            verify(documentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.ratingSum").value(DEFAULT_RATING_SUM))
            .andExpect(jsonPath("$.ratingNumber").value(DEFAULT_RATING_NUMBER))
            .andExpect(jsonPath("$.view").value(DEFAULT_VIEW));
    }


    @Test
    @Transactional
    public void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        Long id = document.getId();

        defaultDocumentShouldBeFound("id.equals=" + id);
        defaultDocumentShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDocumentsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where title equals to DEFAULT_TITLE
        defaultDocumentShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the documentList where title equals to UPDATED_TITLE
        defaultDocumentShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where title not equals to DEFAULT_TITLE
        defaultDocumentShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the documentList where title not equals to UPDATED_TITLE
        defaultDocumentShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultDocumentShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the documentList where title equals to UPDATED_TITLE
        defaultDocumentShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where title is not null
        defaultDocumentShouldBeFound("title.specified=true");

        // Get all the documentList where title is null
        defaultDocumentShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentsByTitleContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where title contains DEFAULT_TITLE
        defaultDocumentShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the documentList where title contains UPDATED_TITLE
        defaultDocumentShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where title does not contain DEFAULT_TITLE
        defaultDocumentShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the documentList where title does not contain UPDATED_TITLE
        defaultDocumentShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllDocumentsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where active equals to DEFAULT_ACTIVE
        defaultDocumentShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the documentList where active equals to UPDATED_ACTIVE
        defaultDocumentShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where active not equals to DEFAULT_ACTIVE
        defaultDocumentShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the documentList where active not equals to UPDATED_ACTIVE
        defaultDocumentShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultDocumentShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the documentList where active equals to UPDATED_ACTIVE
        defaultDocumentShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where active is not null
        defaultDocumentShouldBeFound("active.specified=true");

        // Get all the documentList where active is null
        defaultDocumentShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description equals to DEFAULT_DESCRIPTION
        defaultDocumentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the documentList where description equals to UPDATED_DESCRIPTION
        defaultDocumentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description not equals to DEFAULT_DESCRIPTION
        defaultDocumentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the documentList where description not equals to UPDATED_DESCRIPTION
        defaultDocumentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDocumentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the documentList where description equals to UPDATED_DESCRIPTION
        defaultDocumentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description is not null
        defaultDocumentShouldBeFound("description.specified=true");

        // Get all the documentList where description is null
        defaultDocumentShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description contains DEFAULT_DESCRIPTION
        defaultDocumentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the documentList where description contains UPDATED_DESCRIPTION
        defaultDocumentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description does not contain DEFAULT_DESCRIPTION
        defaultDocumentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the documentList where description does not contain UPDATED_DESCRIPTION
        defaultDocumentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum equals to DEFAULT_RATING_SUM
        defaultDocumentShouldBeFound("ratingSum.equals=" + DEFAULT_RATING_SUM);

        // Get all the documentList where ratingSum equals to UPDATED_RATING_SUM
        defaultDocumentShouldNotBeFound("ratingSum.equals=" + UPDATED_RATING_SUM);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum not equals to DEFAULT_RATING_SUM
        defaultDocumentShouldNotBeFound("ratingSum.notEquals=" + DEFAULT_RATING_SUM);

        // Get all the documentList where ratingSum not equals to UPDATED_RATING_SUM
        defaultDocumentShouldBeFound("ratingSum.notEquals=" + UPDATED_RATING_SUM);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum in DEFAULT_RATING_SUM or UPDATED_RATING_SUM
        defaultDocumentShouldBeFound("ratingSum.in=" + DEFAULT_RATING_SUM + "," + UPDATED_RATING_SUM);

        // Get all the documentList where ratingSum equals to UPDATED_RATING_SUM
        defaultDocumentShouldNotBeFound("ratingSum.in=" + UPDATED_RATING_SUM);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum is not null
        defaultDocumentShouldBeFound("ratingSum.specified=true");

        // Get all the documentList where ratingSum is null
        defaultDocumentShouldNotBeFound("ratingSum.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum is greater than or equal to DEFAULT_RATING_SUM
        defaultDocumentShouldBeFound("ratingSum.greaterThanOrEqual=" + DEFAULT_RATING_SUM);

        // Get all the documentList where ratingSum is greater than or equal to UPDATED_RATING_SUM
        defaultDocumentShouldNotBeFound("ratingSum.greaterThanOrEqual=" + UPDATED_RATING_SUM);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum is less than or equal to DEFAULT_RATING_SUM
        defaultDocumentShouldBeFound("ratingSum.lessThanOrEqual=" + DEFAULT_RATING_SUM);

        // Get all the documentList where ratingSum is less than or equal to SMALLER_RATING_SUM
        defaultDocumentShouldNotBeFound("ratingSum.lessThanOrEqual=" + SMALLER_RATING_SUM);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsLessThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum is less than DEFAULT_RATING_SUM
        defaultDocumentShouldNotBeFound("ratingSum.lessThan=" + DEFAULT_RATING_SUM);

        // Get all the documentList where ratingSum is less than UPDATED_RATING_SUM
        defaultDocumentShouldBeFound("ratingSum.lessThan=" + UPDATED_RATING_SUM);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingSumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingSum is greater than DEFAULT_RATING_SUM
        defaultDocumentShouldNotBeFound("ratingSum.greaterThan=" + DEFAULT_RATING_SUM);

        // Get all the documentList where ratingSum is greater than SMALLER_RATING_SUM
        defaultDocumentShouldBeFound("ratingSum.greaterThan=" + SMALLER_RATING_SUM);
    }


    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber equals to DEFAULT_RATING_NUMBER
        defaultDocumentShouldBeFound("ratingNumber.equals=" + DEFAULT_RATING_NUMBER);

        // Get all the documentList where ratingNumber equals to UPDATED_RATING_NUMBER
        defaultDocumentShouldNotBeFound("ratingNumber.equals=" + UPDATED_RATING_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber not equals to DEFAULT_RATING_NUMBER
        defaultDocumentShouldNotBeFound("ratingNumber.notEquals=" + DEFAULT_RATING_NUMBER);

        // Get all the documentList where ratingNumber not equals to UPDATED_RATING_NUMBER
        defaultDocumentShouldBeFound("ratingNumber.notEquals=" + UPDATED_RATING_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber in DEFAULT_RATING_NUMBER or UPDATED_RATING_NUMBER
        defaultDocumentShouldBeFound("ratingNumber.in=" + DEFAULT_RATING_NUMBER + "," + UPDATED_RATING_NUMBER);

        // Get all the documentList where ratingNumber equals to UPDATED_RATING_NUMBER
        defaultDocumentShouldNotBeFound("ratingNumber.in=" + UPDATED_RATING_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber is not null
        defaultDocumentShouldBeFound("ratingNumber.specified=true");

        // Get all the documentList where ratingNumber is null
        defaultDocumentShouldNotBeFound("ratingNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber is greater than or equal to DEFAULT_RATING_NUMBER
        defaultDocumentShouldBeFound("ratingNumber.greaterThanOrEqual=" + DEFAULT_RATING_NUMBER);

        // Get all the documentList where ratingNumber is greater than or equal to UPDATED_RATING_NUMBER
        defaultDocumentShouldNotBeFound("ratingNumber.greaterThanOrEqual=" + UPDATED_RATING_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber is less than or equal to DEFAULT_RATING_NUMBER
        defaultDocumentShouldBeFound("ratingNumber.lessThanOrEqual=" + DEFAULT_RATING_NUMBER);

        // Get all the documentList where ratingNumber is less than or equal to SMALLER_RATING_NUMBER
        defaultDocumentShouldNotBeFound("ratingNumber.lessThanOrEqual=" + SMALLER_RATING_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber is less than DEFAULT_RATING_NUMBER
        defaultDocumentShouldNotBeFound("ratingNumber.lessThan=" + DEFAULT_RATING_NUMBER);

        // Get all the documentList where ratingNumber is less than UPDATED_RATING_NUMBER
        defaultDocumentShouldBeFound("ratingNumber.lessThan=" + UPDATED_RATING_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRatingNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where ratingNumber is greater than DEFAULT_RATING_NUMBER
        defaultDocumentShouldNotBeFound("ratingNumber.greaterThan=" + DEFAULT_RATING_NUMBER);

        // Get all the documentList where ratingNumber is greater than SMALLER_RATING_NUMBER
        defaultDocumentShouldBeFound("ratingNumber.greaterThan=" + SMALLER_RATING_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDocumentsByViewIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view equals to DEFAULT_VIEW
        defaultDocumentShouldBeFound("view.equals=" + DEFAULT_VIEW);

        // Get all the documentList where view equals to UPDATED_VIEW
        defaultDocumentShouldNotBeFound("view.equals=" + UPDATED_VIEW);
    }

    @Test
    @Transactional
    public void getAllDocumentsByViewIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view not equals to DEFAULT_VIEW
        defaultDocumentShouldNotBeFound("view.notEquals=" + DEFAULT_VIEW);

        // Get all the documentList where view not equals to UPDATED_VIEW
        defaultDocumentShouldBeFound("view.notEquals=" + UPDATED_VIEW);
    }

    @Test
    @Transactional
    public void getAllDocumentsByViewIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view in DEFAULT_VIEW or UPDATED_VIEW
        defaultDocumentShouldBeFound("view.in=" + DEFAULT_VIEW + "," + UPDATED_VIEW);

        // Get all the documentList where view equals to UPDATED_VIEW
        defaultDocumentShouldNotBeFound("view.in=" + UPDATED_VIEW);
    }

    @Test
    @Transactional
    public void getAllDocumentsByViewIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view is not null
        defaultDocumentShouldBeFound("view.specified=true");

        // Get all the documentList where view is null
        defaultDocumentShouldNotBeFound("view.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByViewIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view is greater than or equal to DEFAULT_VIEW
        defaultDocumentShouldBeFound("view.greaterThanOrEqual=" + DEFAULT_VIEW);

        // Get all the documentList where view is greater than or equal to UPDATED_VIEW
        defaultDocumentShouldNotBeFound("view.greaterThanOrEqual=" + UPDATED_VIEW);
    }

    @Test
    @Transactional
    public void getAllDocumentsByViewIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view is less than or equal to DEFAULT_VIEW
        defaultDocumentShouldBeFound("view.lessThanOrEqual=" + DEFAULT_VIEW);

        // Get all the documentList where view is less than or equal to SMALLER_VIEW
        defaultDocumentShouldNotBeFound("view.lessThanOrEqual=" + SMALLER_VIEW);
    }

    @Test
    @Transactional
    public void getAllDocumentsByViewIsLessThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view is less than DEFAULT_VIEW
        defaultDocumentShouldNotBeFound("view.lessThan=" + DEFAULT_VIEW);

        // Get all the documentList where view is less than UPDATED_VIEW
        defaultDocumentShouldBeFound("view.lessThan=" + UPDATED_VIEW);
    }

    @Test
    @Transactional
    public void getAllDocumentsByViewIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where view is greater than DEFAULT_VIEW
        defaultDocumentShouldNotBeFound("view.greaterThan=" + DEFAULT_VIEW);

        // Get all the documentList where view is greater than SMALLER_VIEW
        defaultDocumentShouldBeFound("view.greaterThan=" + SMALLER_VIEW);
    }


//    @Test
//    @Transactional
//    public void getAllDocumentsByAttachmentIsEqualToSomething() throws Exception {
//        // Get already existing entity
//        Attachment attachment = document.getAttachment();
//        documentRepository.saveAndFlush(document);
//        Long attachmentId = attachment.getId();
//
//        // Get all the documentList where attachment equals to attachmentId
//        defaultDocumentShouldBeFound("attachmentId.equals=" + attachmentId);
//
//        // Get all the documentList where attachment equals to attachmentId + 1
//        defaultDocumentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
//    }


    @Test
    @Transactional
    public void getAllDocumentsByReportIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        Report report = ReportResourceIT.createEntity(em);
        em.persist(report);
        em.flush();
        document.addReport(report);
        documentRepository.saveAndFlush(document);
        Long reportId = report.getId();

        // Get all the documentList where report equals to reportId
        defaultDocumentShouldBeFound("reportId.equals=" + reportId);

        // Get all the documentList where report equals to reportId + 1
        defaultDocumentShouldNotBeFound("reportId.equals=" + (reportId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentsByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        Tag tags = TagResourceIT.createEntity(em);
        em.persist(tags);
        em.flush();
        document.addTags(tags);
        documentRepository.saveAndFlush(document);
        Long tagsId = tags.getId();

        // Get all the documentList where tags equals to tagsId
        defaultDocumentShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the documentList where tags equals to tagsId + 1
        defaultDocumentShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        document.setCourse(course);
        documentRepository.saveAndFlush(document);
        Long courseId = course.getId();

        // Get all the documentList where course equals to courseId
        defaultDocumentShouldBeFound("courseId.equals=" + courseId);

        // Get all the documentList where course equals to courseId + 1
        defaultDocumentShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentsByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        DocumentType documentType = DocumentTypeResourceIT.createEntity(em);
        em.persist(documentType);
        em.flush();
        document.setDocumentType(documentType);
        documentRepository.saveAndFlush(document);
        Long documentTypeId = documentType.getId();

        // Get all the documentList where documentType equals to documentTypeId
        defaultDocumentShouldBeFound("documentTypeId.equals=" + documentTypeId);

        // Get all the documentList where documentType equals to documentTypeId + 1
        defaultDocumentShouldNotBeFound("documentTypeId.equals=" + (documentTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        document.setStudent(student);
        documentRepository.saveAndFlush(document);
        Long studentId = student.getId();

        // Get all the documentList where student equals to studentId
        defaultDocumentShouldBeFound("studentId.equals=" + studentId);

        // Get all the documentList where student equals to studentId + 1
        defaultDocumentShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentShouldBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].ratingSum").value(hasItem(DEFAULT_RATING_SUM)))
            .andExpect(jsonPath("$.[*].ratingNumber").value(hasItem(DEFAULT_RATING_NUMBER)))
            .andExpect(jsonPath("$.[*].view").value(hasItem(DEFAULT_VIEW)));

        // Check, that the count call also returns 1
        restDocumentMockMvc.perform(get("/api/documents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentShouldNotBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentMockMvc.perform(get("/api/documents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).get();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .title(UPDATED_TITLE)
            .active(UPDATED_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .ratingSum(UPDATED_RATING_SUM)
            .ratingNumber(UPDATED_RATING_NUMBER)
            .view(UPDATED_VIEW);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDocument.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocument.getRatingSum()).isEqualTo(UPDATED_RATING_SUM);
        assertThat(testDocument.getRatingNumber()).isEqualTo(UPDATED_RATING_NUMBER);
        assertThat(testDocument.getView()).isEqualTo(UPDATED_VIEW);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
