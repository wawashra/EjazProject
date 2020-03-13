package com.ejazbzu.web.rest;

import com.ejazbzu.service.UniversityService;
import com.ejazbzu.web.rest.errors.BadRequestAlertException;
import com.ejazbzu.service.dto.UniversityDTO;
import com.ejazbzu.service.dto.UniversityCriteria;
import com.ejazbzu.service.UniversityQueryService;

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
 * REST controller for managing {@link com.ejazbzu.domain.University}.
 */
@RestController
@RequestMapping("/api")
public class UniversityResource {

    private final Logger log = LoggerFactory.getLogger(UniversityResource.class);

    private static final String ENTITY_NAME = "university";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniversityService universityService;

    private final UniversityQueryService universityQueryService;

    public UniversityResource(UniversityService universityService, UniversityQueryService universityQueryService) {
        this.universityService = universityService;
        this.universityQueryService = universityQueryService;
    }

    /**
     * {@code POST  /universities} : Create a new university.
     *
     * @param universityDTO the universityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new universityDTO, or with status {@code 400 (Bad Request)} if the university has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/universities")
    public ResponseEntity<UniversityDTO> createUniversity(@Valid @RequestBody UniversityDTO universityDTO) throws URISyntaxException {
        log.debug("REST request to save University : {}", universityDTO);
        if (universityDTO.getId() != null) {
            throw new BadRequestAlertException("A new university cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UniversityDTO result = universityService.save(universityDTO);
        return ResponseEntity.created(new URI("/api/universities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /universities} : Updates an existing university.
     *
     * @param universityDTO the universityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated universityDTO,
     * or with status {@code 400 (Bad Request)} if the universityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the universityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/universities")
    public ResponseEntity<UniversityDTO> updateUniversity(@Valid @RequestBody UniversityDTO universityDTO) throws URISyntaxException {
        log.debug("REST request to update University : {}", universityDTO);
        if (universityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UniversityDTO result = universityService.save(universityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, universityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /universities} : get all the universities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of universities in body.
     */
    @GetMapping("/universities")
    public ResponseEntity<List<UniversityDTO>> getAllUniversities(UniversityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Universities by criteria: {}", criteria);
        Page<UniversityDTO> page = universityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /universities/count} : count all the universities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/universities/count")
    public ResponseEntity<Long> countUniversities(UniversityCriteria criteria) {
        log.debug("REST request to count Universities by criteria: {}", criteria);
        return ResponseEntity.ok().body(universityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /universities/:id} : get the "id" university.
     *
     * @param id the id of the universityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the universityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/universities/{id}")
    public ResponseEntity<UniversityDTO> getUniversity(@PathVariable Long id) {
        log.debug("REST request to get University : {}", id);
        Optional<UniversityDTO> universityDTO = universityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(universityDTO);
    }

    /**
     * {@code DELETE  /universities/:id} : delete the "id" university.
     *
     * @param id the id of the universityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/universities/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        log.debug("REST request to delete University : {}", id);
        universityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
