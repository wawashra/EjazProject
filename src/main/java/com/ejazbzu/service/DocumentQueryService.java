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

import com.ejazbzu.domain.Document;
import com.ejazbzu.domain.*; // for static metamodels
import com.ejazbzu.repository.DocumentRepository;
import com.ejazbzu.service.dto.DocumentCriteria;
import com.ejazbzu.service.dto.DocumentDTO;
import com.ejazbzu.service.mapper.DocumentMapper;

/**
 * Service for executing complex queries for {@link Document} entities in the database.
 * The main input is a {@link DocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentDTO} or a {@link Page} of {@link DocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentQueryService extends QueryService<Document> {

    private final Logger log = LoggerFactory.getLogger(DocumentQueryService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    public DocumentQueryService(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    /**
     * Return a {@link List} of {@link DocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentDTO> findByCriteria(DocumentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentMapper.toDto(documentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByCriteria(DocumentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.findAll(specification, page)
            .map(documentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Document> createSpecification(DocumentCriteria criteria) {
        Specification<Document> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Document_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Document_.title));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Document_.active));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Document_.description));
            }
            if (criteria.getRatingSum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatingSum(), Document_.ratingSum));
            }
            if (criteria.getRatingNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRatingNumber(), Document_.ratingNumber));
            }
            if (criteria.getView() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getView(), Document_.view));
            }
            if (criteria.getAttachmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttachmentId(),
                    root -> root.join(Document_.attachments, JoinType.LEFT).get(Attachment_.id)));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(),
                    root -> root.join(Document_.reports, JoinType.LEFT).get(Report_.id)));
            }
            if (criteria.getTagsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagsId(),
                    root -> root.join(Document_.tags, JoinType.LEFT).get(Tag_.id)));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(Document_.course, JoinType.LEFT).get(Course_.id)));
            }
            if (criteria.getDocumentTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentTypeId(),
                    root -> root.join(Document_.documentType, JoinType.LEFT).get(DocumentType_.id)));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentId(),
                    root -> root.join(Document_.student, JoinType.LEFT).get(Student_.id)));
            }
        }
        return specification;
    }
}
