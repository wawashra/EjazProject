package com.ejazbzu.web.rest;

import com.ejazbzu.service.CollegeService;
import com.ejazbzu.web.rest.errors.BadRequestAlertException;
import com.ejazbzu.service.dto.CollegeDTO;
import com.ejazbzu.service.dto.CollegeCriteria;
import com.ejazbzu.service.CollegeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ejazbzu.domain.College}.
 */
@RestController
@RequestMapping("/api")
public class CollegeResource {

    private final Logger log = LoggerFactory.getLogger(CollegeResource.class);

    private static final String ENTITY_NAME = "college";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollegeService collegeService;

    private final CollegeQueryService collegeQueryService;

    public CollegeResource(CollegeService collegeService, CollegeQueryService collegeQueryService) {
        this.collegeService = collegeService;
        this.collegeQueryService = collegeQueryService;
    }

    /**
     * {@code POST  /colleges} : Create a new college.
     *
     * @param collegeDTO the collegeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collegeDTO, or with status {@code 400 (Bad Request)} if the college has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/colleges")
    public ResponseEntity<CollegeDTO> createCollege(@Valid @RequestBody CollegeDTO collegeDTO) throws URISyntaxException {
        log.debug("REST request to save College : {}", collegeDTO);
        if (collegeDTO.getId() != null) {
            throw new BadRequestAlertException("A new college cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollegeDTO result = collegeService.save(collegeDTO);
        return ResponseEntity.created(new URI("/api/colleges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /colleges} : Updates an existing college.
     *
     * @param collegeDTO the collegeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collegeDTO,
     * or with status {@code 400 (Bad Request)} if the collegeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collegeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/colleges")
    public ResponseEntity<CollegeDTO> updateCollege(@Valid @RequestBody CollegeDTO collegeDTO) throws URISyntaxException {
        log.debug("REST request to update College : {}", collegeDTO);
        if (collegeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CollegeDTO result = collegeService.save(collegeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collegeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /colleges} : get all the colleges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of colleges in body.
     */
    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeDTO>> getAllColleges(CollegeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Colleges by criteria: {}", criteria);
        Page<CollegeDTO> page = collegeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /colleges/count} : count all the colleges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/colleges/count")
    public ResponseEntity<Long> countColleges(CollegeCriteria criteria) {
        log.debug("REST request to count Colleges by criteria: {}", criteria);
        return ResponseEntity.ok().body(collegeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /colleges/:id} : get the "id" college.
     *
     * @param id the id of the collegeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collegeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/colleges/{id}")
    public ResponseEntity<CollegeDTO> getCollege(@PathVariable Long id) {
        log.debug("REST request to get College : {}", id);
        Optional<CollegeDTO> collegeDTO = collegeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collegeDTO);
    }

    /**
     * {@code DELETE  /colleges/:id} : delete the "id" college.
     *
     * @param id the id of the collegeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/colleges/{id}")
    public ResponseEntity<Void> deleteCollege(@PathVariable Long id) {
        log.debug("REST request to delete College : {}", id);
        collegeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
