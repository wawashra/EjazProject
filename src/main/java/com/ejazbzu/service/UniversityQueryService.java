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

import com.ejazbzu.domain.University;
import com.ejazbzu.domain.*; // for static metamodels
import com.ejazbzu.repository.UniversityRepository;
import com.ejazbzu.service.dto.UniversityCriteria;
import com.ejazbzu.service.dto.UniversityDTO;
import com.ejazbzu.service.mapper.UniversityMapper;

/**
 * Service for executing complex queries for {@link University} entities in the database.
 * The main input is a {@link UniversityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UniversityDTO} or a {@link Page} of {@link UniversityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UniversityQueryService extends QueryService<University> {

    private final Logger log = LoggerFactory.getLogger(UniversityQueryService.class);

    private final UniversityRepository universityRepository;

    private final UniversityMapper universityMapper;

    public UniversityQueryService(UniversityRepository universityRepository, UniversityMapper universityMapper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
    }

    /**
     * Return a {@link List} of {@link UniversityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UniversityDTO> findByCriteria(UniversityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<University> specification = createSpecification(criteria);
        return universityMapper.toDto(universityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UniversityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UniversityDTO> findByCriteria(UniversityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<University> specification = createSpecification(criteria);
        return universityRepository.findAll(specification, page)
            .map(universityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UniversityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<University> specification = createSpecification(criteria);
        return universityRepository.count(specification);
    }

    /**
     * Function to convert {@link UniversityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<University> createSpecification(UniversityCriteria criteria) {
        Specification<University> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), University_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), University_.name));
            }
            if (criteria.getSymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol(), University_.symbol));
            }
            if (criteria.getCollegeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCollegeId(),
                    root -> root.join(University_.colleges, JoinType.LEFT).get(College_.id)));
            }
        }
        return specification;
    }
}
