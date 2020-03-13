package com.ejazbzu.web.rest;

import com.ejazbzu.service.AttachmentTypeService;
import com.ejazbzu.web.rest.errors.BadRequestAlertException;
import com.ejazbzu.service.dto.AttachmentTypeDTO;
import com.ejazbzu.service.dto.AttachmentTypeCriteria;
import com.ejazbzu.service.AttachmentTypeQueryService;

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
 * REST controller for managing {@link com.ejazbzu.domain.AttachmentType}.
 */
@RestController
@RequestMapping("/api")
public class AttachmentTypeResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentTypeResource.class);

    private static final String ENTITY_NAME = "attachmentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachmentTypeService attachmentTypeService;

    private final AttachmentTypeQueryService attachmentTypeQueryService;

    public AttachmentTypeResource(AttachmentTypeService attachmentTypeService, AttachmentTypeQueryService attachmentTypeQueryService) {
        this.attachmentTypeService = attachmentTypeService;
        this.attachmentTypeQueryService = attachmentTypeQueryService;
    }

    /**
     * {@code POST  /attachment-types} : Create a new attachmentType.
     *
     * @param attachmentTypeDTO the attachmentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachmentTypeDTO, or with status {@code 400 (Bad Request)} if the attachmentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachment-types")
    public ResponseEntity<AttachmentTypeDTO> createAttachmentType(@Valid @RequestBody AttachmentTypeDTO attachmentTypeDTO) throws URISyntaxException {
        log.debug("REST request to save AttachmentType : {}", attachmentTypeDTO);
        if (attachmentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new attachmentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttachmentTypeDTO result = attachmentTypeService.save(attachmentTypeDTO);
        return ResponseEntity.created(new URI("/api/attachment-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attachment-types} : Updates an existing attachmentType.
     *
     * @param attachmentTypeDTO the attachmentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachmentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the attachmentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachmentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachment-types")
    public ResponseEntity<AttachmentTypeDTO> updateAttachmentType(@Valid @RequestBody AttachmentTypeDTO attachmentTypeDTO) throws URISyntaxException {
        log.debug("REST request to update AttachmentType : {}", attachmentTypeDTO);
        if (attachmentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttachmentTypeDTO result = attachmentTypeService.save(attachmentTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachmentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attachment-types} : get all the attachmentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachmentTypes in body.
     */
    @GetMapping("/attachment-types")
    public ResponseEntity<List<AttachmentTypeDTO>> getAllAttachmentTypes(AttachmentTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AttachmentTypes by criteria: {}", criteria);
        Page<AttachmentTypeDTO> page = attachmentTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attachment-types/count} : count all the attachmentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/attachment-types/count")
    public ResponseEntity<Long> countAttachmentTypes(AttachmentTypeCriteria criteria) {
        log.debug("REST request to count AttachmentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(attachmentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /attachment-types/:id} : get the "id" attachmentType.
     *
     * @param id the id of the attachmentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachmentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attachment-types/{id}")
    public ResponseEntity<AttachmentTypeDTO> getAttachmentType(@PathVariable Long id) {
        log.debug("REST request to get AttachmentType : {}", id);
        Optional<AttachmentTypeDTO> attachmentTypeDTO = attachmentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachmentTypeDTO);
    }

    /**
     * {@code DELETE  /attachment-types/:id} : delete the "id" attachmentType.
     *
     * @param id the id of the attachmentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attachment-types/{id}")
    public ResponseEntity<Void> deleteAttachmentType(@PathVariable Long id) {
        log.debug("REST request to delete AttachmentType : {}", id);
        attachmentTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
