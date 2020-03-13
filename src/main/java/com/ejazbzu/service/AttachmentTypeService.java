package com.ejazbzu.service;

import com.ejazbzu.service.dto.AttachmentTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.ejazbzu.domain.AttachmentType}.
 */
public interface AttachmentTypeService {

    /**
     * Save a attachmentType.
     *
     * @param attachmentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    AttachmentTypeDTO save(AttachmentTypeDTO attachmentTypeDTO);

    /**
     * Get all the attachmentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttachmentTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" attachmentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttachmentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" attachmentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
