package com.ejazbzu.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ejazbzu.domain.AttachmentType;
import com.ejazbzu.domain.*; // for static metamodels
import com.ejazbzu.repository.AttachmentTypeRepository;
import com.ejazbzu.service.dto.AttachmentTypeCriteria;
import com.ejazbzu.service.dto.AttachmentTypeDTO;
import com.ejazbzu.service.mapper.AttachmentTypeMapper;

/**
 * Service for executing complex queries for {@link AttachmentType} entities in the database.
 * The main input is a {@link AttachmentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AttachmentTypeDTO} or a {@link Page} of {@link AttachmentTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttachmentTypeQueryService extends QueryService<AttachmentType> {

    private final Logger log = LoggerFactory.getLogger(AttachmentTypeQueryService.class);

    private final AttachmentTypeRepository attachmentTypeRepository;

    private final AttachmentTypeMapper attachmentTypeMapper;

    public AttachmentTypeQueryService(AttachmentTypeRepository attachmentTypeRepository, AttachmentTypeMapper attachmentTypeMapper) {
        this.attachmentTypeRepository = attachmentTypeRepository;
        this.attachmentTypeMapper = attachmentTypeMapper;
    }

    /**
     * Return a {@link List} of {@link AttachmentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttachmentTypeDTO> findByCriteria(AttachmentTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AttachmentType> specification = createSpecification(criteria);
        return attachmentTypeMapper.toDto(attachmentTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AttachmentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttachmentTypeDTO> findByCriteria(AttachmentTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AttachmentType> specification = createSpecification(criteria);
        return attachmentTypeRepository.findAll(specification, page)
            .map(attachmentTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttachmentTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AttachmentType> specification = createSpecification(criteria);
        return attachmentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link AttachmentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AttachmentType> createSpecification(AttachmentTypeCriteria criteria) {
        Specification<AttachmentType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AttachmentType_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), AttachmentType_.type));
            }
            if (criteria.getAttachmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttachmentId(),
                    root -> root.join(AttachmentType_.attachments, JoinType.LEFT).get(Attachment_.id)));
            }
        }
        return specification;
    }
}
