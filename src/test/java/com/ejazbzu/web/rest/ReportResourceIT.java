package com.ejazbzu.web.rest;

import com.ejazbzu.EjazApp;
import com.ejazbzu.domain.Report;
import com.ejazbzu.domain.Document;
import com.ejazbzu.domain.Student;
import com.ejazbzu.repository.ReportRepository;
import com.ejazbzu.service.ReportService;
import com.ejazbzu.service.dto.ReportDTO;
import com.ejazbzu.service.mapper.ReportMapper;
import com.ejazbzu.web.rest.errors.ExceptionTranslator;
import com.ejazbzu.service.dto.ReportCriteria;
import com.ejazbzu.service.ReportQueryService;

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
 * Integration tests for the {@link ReportResource} REST controller.
 */
@SpringBootTest(classes = EjazApp.class)
public class ReportResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportQueryService reportQueryService;

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

    private MockMvc restReportMockMvc;

    private Report report;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportResource reportResource = new ReportResource(reportService, reportQueryService);
        this.restReportMockMvc = MockMvcBuilders.standaloneSetup(reportResource)
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
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .message(DEFAULT_MESSAGE);
        return report;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report()
            .message(UPDATED_MESSAGE);
        return report;
    }

    @BeforeEach
    public void initTest() {
        report = createEntity(em);
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report with an existing ID
        report.setId(1L);
        ReportDTO reportDTO = reportMapper.toDto(report);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setMessage(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc.perform(get("/api/reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }
    
    @Test
    @Transactional
    public void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }


    @Test
    @Transactional
    public void getReportsByIdFiltering() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        Long id = report.getId();

        defaultReportShouldBeFound("id.equals=" + id);
        defaultReportShouldNotBeFound("id.notEquals=" + id);

        defaultReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.greaterThan=" + id);

        defaultReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllReportsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message equals to DEFAULT_MESSAGE
        defaultReportShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the reportList where message equals to UPDATED_MESSAGE
        defaultReportShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReportsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message not equals to DEFAULT_MESSAGE
        defaultReportShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the reportList where message not equals to UPDATED_MESSAGE
        defaultReportShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReportsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultReportShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the reportList where message equals to UPDATED_MESSAGE
        defaultReportShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReportsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message is not null
        defaultReportShouldBeFound("message.specified=true");

        // Get all the reportList where message is null
        defaultReportShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllReportsByMessageContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message contains DEFAULT_MESSAGE
        defaultReportShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the reportList where message contains UPDATED_MESSAGE
        defaultReportShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReportsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message does not contain DEFAULT_MESSAGE
        defaultReportShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the reportList where message does not contain UPDATED_MESSAGE
        defaultReportShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllReportsByDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        Document document = DocumentResourceIT.createEntity(em);
        em.persist(document);
        em.flush();
        report.setDocument(document);
        reportRepository.saveAndFlush(report);
        Long documentId = document.getId();

        // Get all the reportList where document equals to documentId
        defaultReportShouldBeFound("documentId.equals=" + documentId);

        // Get all the reportList where document equals to documentId + 1
        defaultReportShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }


    @Test
    @Transactional
    public void getAllReportsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        report.setStudent(student);
        reportRepository.saveAndFlush(report);
        Long studentId = student.getId();

        // Get all the reportList where student equals to studentId
        defaultReportShouldBeFound("studentId.equals=" + studentId);

        // Get all the reportList where student equals to studentId + 1
        defaultReportShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportShouldBeFound(String filter) throws Exception {
        restReportMockMvc.perform(get("/api/reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));

        // Check, that the count call also returns 1
        restReportMockMvc.perform(get("/api/reports/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportShouldNotBeFound(String filter) throws Exception {
        restReportMockMvc.perform(get("/api/reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportMockMvc.perform(get("/api/reports/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).get();
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport
            .message(UPDATED_MESSAGE);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Delete the report
        restReportMockMvc.perform(delete("/api/reports/{id}", report.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
