package com.ejazbzu.service.impl;

import com.ejazbzu.service.DocumentTypeService;
import com.ejazbzu.domain.DocumentType;
import com.ejazbzu.repository.DocumentTypeRepository;
import com.ejazbzu.service.dto.DocumentTypeDTO;
import com.ejazbzu.service.mapper.DocumentTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DocumentType}.
 */
@Service
@Transactional
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

    private final DocumentTypeRepository documentTypeRepository;

    private final DocumentTypeMapper documentTypeMapper;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository, DocumentTypeMapper documentTypeMapper) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeMapper = documentTypeMapper;
    }

    /**
     * Save a documentType.
     *
     * @param documentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DocumentTypeDTO save(DocumentTypeDTO documentTypeDTO) {
        log.debug("Request to save DocumentType : {}", documentTypeDTO);
        DocumentType documentType = documentTypeMapper.toEntity(documentTypeDTO);
        documentType = documentTypeRepository.save(documentType);
        return documentTypeMapper.toDto(documentType);
    }

    /**
     * Get all the documentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DocumentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentTypes");
        return documentTypeRepository.findAll(pageable)
            .map(documentTypeMapper::toDto);
    }

    /**
     * Get one documentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentTypeDTO> findOne(Long id) {
        log.debug("Request to get DocumentType : {}", id);
        return documentTypeRepository.findById(id)
            .map(documentTypeMapper::toDto);
    }

    /**
     * Delete the documentType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentType : {}", id);
        documentTypeRepository.deleteById(id);
    }
}
