package com.ejazbzu.web.rest;

import com.ejazbzu.service.DocumentTypeService;
import com.ejazbzu.web.rest.errors.BadRequestAlertException;
import com.ejazbzu.service.dto.DocumentTypeDTO;
import com.ejazbzu.service.dto.DocumentTypeCriteria;
import com.ejazbzu.service.DocumentTypeQueryService;

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
 * REST controller for managing {@link com.ejazbzu.domain.DocumentType}.
 */
@RestController
@RequestMapping("/api")
public class DocumentTypeResource {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeResource.class);

    private static final String ENTITY_NAME = "documentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentTypeService documentTypeService;

    private final DocumentTypeQueryService documentTypeQueryService;

    public DocumentTypeResource(DocumentTypeService documentTypeService, DocumentTypeQueryService documentTypeQueryService) {
        this.documentTypeService = documentTypeService;
        this.documentTypeQueryService = documentTypeQueryService;
    }

    /**
     * {@code POST  /document-types} : Create a new documentType.
     *
     * @param documentTypeDTO the documentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentTypeDTO, or with status {@code 400 (Bad Request)} if the documentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-types")
    public ResponseEntity<DocumentTypeDTO> createDocumentType(@Valid @RequestBody DocumentTypeDTO documentTypeDTO) throws URISyntaxException {
        log.debug("REST request to save DocumentType : {}", documentTypeDTO);
        if (documentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentTypeDTO result = documentTypeService.save(documentTypeDTO);
        return ResponseEntity.created(new URI("/api/document-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-types} : Updates an existing documentType.
     *
     * @param documentTypeDTO the documentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the documentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-types")
    public ResponseEntity<DocumentTypeDTO> updateDocumentType(@Valid @RequestBody DocumentTypeDTO documentTypeDTO) throws URISyntaxException {
        log.debug("REST request to update DocumentType : {}", documentTypeDTO);
        if (documentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DocumentTypeDTO result = documentTypeService.save(documentTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /document-types} : get all the documentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentTypes in body.
     */
    @GetMapping("/document-types")
    public ResponseEntity<List<DocumentTypeDTO>> getAllDocumentTypes(DocumentTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DocumentTypes by criteria: {}", criteria);
        Page<DocumentTypeDTO> page = documentTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-types/count} : count all the documentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/document-types/count")
    public ResponseEntity<Long> countDocumentTypes(DocumentTypeCriteria criteria) {
        log.debug("REST request to count DocumentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /document-types/:id} : get the "id" documentType.
     *
     * @param id the id of the documentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-types/{id}")
    public ResponseEntity<DocumentTypeDTO> getDocumentType(@PathVariable Long id) {
        log.debug("REST request to get DocumentType : {}", id);
        Optional<DocumentTypeDTO> documentTypeDTO = documentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentTypeDTO);
    }

    /**
     * {@code DELETE  /document-types/:id} : delete the "id" documentType.
     *
     * @param id the id of the documentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-types/{id}")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable Long id) {
        log.debug("REST request to delete DocumentType : {}", id);
        documentTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
