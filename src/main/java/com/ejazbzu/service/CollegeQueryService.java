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

import com.ejazbzu.domain.College;
import com.ejazbzu.domain.*; // for static metamodels
import com.ejazbzu.repository.CollegeRepository;
import com.ejazbzu.service.dto.CollegeCriteria;
import com.ejazbzu.service.dto.CollegeDTO;
import com.ejazbzu.service.mapper.CollegeMapper;

/**
 * Service for executing complex queries for {@link College} entities in the database.
 * The main input is a {@link CollegeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CollegeDTO} or a {@link Page} of {@link CollegeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollegeQueryService extends QueryService<College> {

    private final Logger log = LoggerFactory.getLogger(CollegeQueryService.class);

    private final CollegeRepository collegeRepository;

    private final CollegeMapper collegeMapper;

    public CollegeQueryService(CollegeRepository collegeRepository, CollegeMapper collegeMapper) {
        this.collegeRepository = collegeRepository;
        this.collegeMapper = collegeMapper;
    }

    /**
     * Return a {@link List} of {@link CollegeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CollegeDTO> findByCriteria(CollegeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<College> specification = createSpecification(criteria);
        return collegeMapper.toDto(collegeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CollegeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollegeDTO> findByCriteria(CollegeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<College> specification = createSpecification(criteria);
        return collegeRepository.findAll(specification, page)
            .map(collegeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollegeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<College> specification = createSpecification(criteria);
        return collegeRepository.count(specification);
    }

    /**
     * Function to convert {@link CollegeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<College> createSpecification(CollegeCriteria criteria) {
        Specification<College> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), College_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), College_.name));
            }
            if (criteria.getSymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol(), College_.symbol));
            }
            if (criteria.getCoverImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImgUrl(), College_.coverImgUrl));
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getDepartmentId(),
                    root -> root.join(College_.departments, JoinType.LEFT).get(Department_.id)));
            }
            if (criteria.getUniversityId() != null) {
                specification = specification.and(buildSpecification(criteria.getUniversityId(),
                    root -> root.join(College_.university, JoinType.LEFT).get(University_.id)));
            }
        }
        return specification;
    }
}
