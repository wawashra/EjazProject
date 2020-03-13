package com.ejazbzu.service;

import com.ejazbzu.service.dto.DocumentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.ejazbzu.domain.Document}.
 */
public interface DocumentService {

    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentDTO save(DocumentDTO documentDTO);

    /**
     * Get all the documents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentDTO> findAll(Pageable pageable);

    /**
     * Get all the documents with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<DocumentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" document.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentDTO> findOne(Long id);

    /**
     * Delete the "id" document.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
