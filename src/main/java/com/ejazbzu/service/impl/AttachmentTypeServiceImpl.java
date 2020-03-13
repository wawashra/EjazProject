package com.ejazbzu.service.impl;

import com.ejazbzu.service.AttachmentTypeService;
import com.ejazbzu.domain.AttachmentType;
import com.ejazbzu.repository.AttachmentTypeRepository;
import com.ejazbzu.service.dto.AttachmentTypeDTO;
import com.ejazbzu.service.mapper.AttachmentTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AttachmentType}.
 */
@Service
@Transactional
public class AttachmentTypeServiceImpl implements AttachmentTypeService {

    private final Logger log = LoggerFactory.getLogger(AttachmentTypeServiceImpl.class);

    private final AttachmentTypeRepository attachmentTypeRepository;

    private final AttachmentTypeMapper attachmentTypeMapper;

    public AttachmentTypeServiceImpl(AttachmentTypeRepository attachmentTypeRepository, AttachmentTypeMapper attachmentTypeMapper) {
        this.attachmentTypeRepository = attachmentTypeRepository;
        this.attachmentTypeMapper = attachmentTypeMapper;
    }

    /**
     * Save a attachmentType.
     *
     * @param attachmentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AttachmentTypeDTO save(AttachmentTypeDTO attachmentTypeDTO) {
        log.debug("Request to save AttachmentType : {}", attachmentTypeDTO);
        AttachmentType attachmentType = attachmentTypeMapper.toEntity(attachmentTypeDTO);
        attachmentType = attachmentTypeRepository.save(attachmentType);
        return attachmentTypeMapper.toDto(attachmentType);
    }

    /**
     * Get all the attachmentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AttachmentTypes");
        return attachmentTypeRepository.findAll(pageable)
            .map(attachmentTypeMapper::toDto);
    }

    /**
     * Get one attachmentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AttachmentTypeDTO> findOne(Long id) {
        log.debug("Request to get AttachmentType : {}", id);
        return attachmentTypeRepository.findById(id)
            .map(attachmentTypeMapper::toDto);
    }

    /**
     * Delete the attachmentType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttachmentType : {}", id);
        attachmentTypeRepository.deleteById(id);
    }
}
