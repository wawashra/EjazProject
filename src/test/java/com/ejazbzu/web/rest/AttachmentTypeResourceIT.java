package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.AttachmentType;
import com.ejazbzu.domain.Attachment;
import com.ejazbzu.repository.AttachmentTypeRepository;
import com.ejazbzu.service.AttachmentTypeService;
import com.ejazbzu.service.dto.AttachmentTypeDTO;
import com.ejazbzu.service.mapper.AttachmentTypeMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.AttachmentTypeCriteria;
import com.ejazbzu.service.AttachmentTypeQueryService;

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
 * Integration tests for the {@link AttachmentTypeResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class AttachmentTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private AttachmentTypeRepository attachmentTypeRepository;

    @Autowired
    private AttachmentTypeMapper attachmentTypeMapper;

    @Autowired
    private AttachmentTypeService attachmentTypeService;

    @Autowired
    private AttachmentTypeQueryService attachmentTypeQueryService;

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

    private MockMvc restAttachmentTypeMockMvc;

    private AttachmentType attachmentType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttachmentTypeResource attachmentTypeResource = new AttachmentTypeResource(attachmentTypeService, attachmentTypeQueryService);
        this.restAttachmentTypeMockMvc = MockMvcBuilders.standaloneSetup(attachmentTypeResource)
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
    public static AttachmentType createEntity(EntityManager em) {
        AttachmentType attachmentType = new AttachmentType()
            .type(DEFAULT_TYPE);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createEntity(em);
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).get(0);
        }
        attachmentType.getAttachments().add(attachment);
        return attachmentType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachmentType createUpdatedEntity(EntityManager em) {
        AttachmentType attachmentType = new AttachmentType()
            .type(UPDATED_TYPE);
        // Add required entity
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachment = AttachmentResourceIT.createUpdatedEntity(em);
            em.persist(attachment);
            em.flush();
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).get(0);
        }
        attachmentType.getAttachments().add(attachment);
        return attachmentType;
    }

    @BeforeEach
    public void initTest() {
        attachmentType = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttachmentType() throws Exception {
        int databaseSizeBeforeCreate = attachmentTypeRepository.findAll().size();

        // Create the AttachmentType
        AttachmentTypeDTO attachmentTypeDTO = attachmentTypeMapper.toDto(attachmentType);
        restAttachmentTypeMockMvc.perform(post("/api/attachment-types")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the AttachmentType in the database
        List<AttachmentType> attachmentTypeList = attachmentTypeRepository.findAll();
        assertThat(attachmentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AttachmentType testAttachmentType = attachmentTypeList.get(attachmentTypeList.size() - 1);
        assertThat(testAttachmentType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createAttachmentTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attachmentTypeRepository.findAll().size();

        // Create the AttachmentType with an existing ID
        attachmentType.setId(1L);
        AttachmentTypeDTO attachmentTypeDTO = attachmentTypeMapper.toDto(attachmentType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentTypeMockMvc.perform(post("/api/attachment-types")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AttachmentType in the database
        List<AttachmentType> attachmentTypeList = attachmentTypeRepository.findAll();
        assertThat(attachmentTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentTypeRepository.findAll().size();
        // set the field null
        attachmentType.setType(null);

        // Create the AttachmentType, which fails.
        AttachmentTypeDTO attachmentTypeDTO = attachmentTypeMapper.toDto(attachmentType);

        restAttachmentTypeMockMvc.perform(post("/api/attachment-types")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentTypeDTO)))
            .andExpect(status().isBadRequest());

        List<AttachmentType> attachmentTypeList = attachmentTypeRepository.findAll();
        assertThat(attachmentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttachmentTypes() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get all the attachmentTypeList
        restAttachmentTypeMockMvc.perform(get("/api/attachment-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    public void getAttachmentType() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get the attachmentType
        restAttachmentTypeMockMvc.perform(get("/api/attachment-types/{id}", attachmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachmentType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }


    @Test
    @Transactional
    public void getAttachmentTypesByIdFiltering() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        Long id = attachmentType.getId();

        defaultAttachmentTypeShouldBeFound("id.equals=" + id);
        defaultAttachmentTypeShouldNotBeFound("id.notEquals=" + id);

        defaultAttachmentTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAttachmentTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultAttachmentTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAttachmentTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAttachmentTypesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get all the attachmentTypeList where type equals to DEFAULT_TYPE
        defaultAttachmentTypeShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the attachmentTypeList where type equals to UPDATED_TYPE
        defaultAttachmentTypeShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttachmentTypesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get all the attachmentTypeList where type not equals to DEFAULT_TYPE
        defaultAttachmentTypeShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the attachmentTypeList where type not equals to UPDATED_TYPE
        defaultAttachmentTypeShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttachmentTypesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get all the attachmentTypeList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAttachmentTypeShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the attachmentTypeList where type equals to UPDATED_TYPE
        defaultAttachmentTypeShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttachmentTypesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get all the attachmentTypeList where type is not null
        defaultAttachmentTypeShouldBeFound("type.specified=true");

        // Get all the attachmentTypeList where type is null
        defaultAttachmentTypeShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllAttachmentTypesByTypeContainsSomething() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get all the attachmentTypeList where type contains DEFAULT_TYPE
        defaultAttachmentTypeShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the attachmentTypeList where type contains UPDATED_TYPE
        defaultAttachmentTypeShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttachmentTypesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        // Get all the attachmentTypeList where type does not contain DEFAULT_TYPE
        defaultAttachmentTypeShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the attachmentTypeList where type does not contain UPDATED_TYPE
        defaultAttachmentTypeShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


//    @Test
//    @Transactional
//    public void getAllAttachmentTypesByAttachmentIsEqualToSomething() throws Exception {
//        // Get already existing entity
//        Attachment attachment = attachmentType.getAttachment();
//        attachmentTypeRepository.saveAndFlush(attachmentType);
//        Long attachmentId = attachment.getId();
//
//        // Get all the attachmentTypeList where attachment equals to attachmentId
//        defaultAttachmentTypeShouldBeFound("attachmentId.equals=" + attachmentId);
//
//        // Get all the attachmentTypeList where attachment equals to attachmentId + 1
//        defaultAttachmentTypeShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
//    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttachmentTypeShouldBeFound(String filter) throws Exception {
        restAttachmentTypeMockMvc.perform(get("/api/attachment-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restAttachmentTypeMockMvc.perform(get("/api/attachment-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttachmentTypeShouldNotBeFound(String filter) throws Exception {
        restAttachmentTypeMockMvc.perform(get("/api/attachment-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttachmentTypeMockMvc.perform(get("/api/attachment-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAttachmentType() throws Exception {
        // Get the attachmentType
        restAttachmentTypeMockMvc.perform(get("/api/attachment-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachmentType() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        int databaseSizeBeforeUpdate = attachmentTypeRepository.findAll().size();

        // Update the attachmentType
        AttachmentType updatedAttachmentType = attachmentTypeRepository.findById(attachmentType.getId()).get();
        // Disconnect from session so that the updates on updatedAttachmentType are not directly saved in db
        em.detach(updatedAttachmentType);
        updatedAttachmentType
            .type(UPDATED_TYPE);
        AttachmentTypeDTO attachmentTypeDTO = attachmentTypeMapper.toDto(updatedAttachmentType);

        restAttachmentTypeMockMvc.perform(put("/api/attachment-types")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentTypeDTO)))
            .andExpect(status().isOk());

        // Validate the AttachmentType in the database
        List<AttachmentType> attachmentTypeList = attachmentTypeRepository.findAll();
        assertThat(attachmentTypeList).hasSize(databaseSizeBeforeUpdate);
        AttachmentType testAttachmentType = attachmentTypeList.get(attachmentTypeList.size() - 1);
        assertThat(testAttachmentType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAttachmentType() throws Exception {
        int databaseSizeBeforeUpdate = attachmentTypeRepository.findAll().size();

        // Create the AttachmentType
        AttachmentTypeDTO attachmentTypeDTO = attachmentTypeMapper.toDto(attachmentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentTypeMockMvc.perform(put("/api/attachment-types")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attachmentTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AttachmentType in the database
        List<AttachmentType> attachmentTypeList = attachmentTypeRepository.findAll();
        assertThat(attachmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAttachmentType() throws Exception {
        // Initialize the database
        attachmentTypeRepository.saveAndFlush(attachmentType);

        int databaseSizeBeforeDelete = attachmentTypeRepository.findAll().size();

        // Delete the attachmentType
        restAttachmentTypeMockMvc.perform(delete("/api/attachment-types/{id}", attachmentType.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttachmentType> attachmentTypeList = attachmentTypeRepository.findAll();
        assertThat(attachmentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
