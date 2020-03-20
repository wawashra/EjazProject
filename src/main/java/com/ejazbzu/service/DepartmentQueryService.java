package com.ejazbzu.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import com.ejazbzu.service.dto.DepartmentFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ejazbzu.domain.Department;
import com.ejazbzu.domain.*; // for static metamodels
import com.ejazbzu.repository.DepartmentRepository;
import com.ejazbzu.service.dto.DepartmentCriteria;
import com.ejazbzu.service.dto.DepartmentDTO;
import com.ejazbzu.service.mapper.DepartmentMapper;

/**
 * Service for executing complex queries for {@link Department} entities in the database.
 * The main input is a {@link DepartmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepartmentDTO} or a {@link Page} of {@link DepartmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartmentQueryService extends QueryService<Department> {

    private final Logger log = LoggerFactory.getLogger(DepartmentQueryService.class);

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    public DepartmentQueryService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepartmentDTO> findByCriteria(DepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentMapper.toDto(departmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartmentDTO> findByCriteria(DepartmentFilterDTO filter, Pageable page) {
        log.debug("find by filter : {}, page: {}", filter, page);
        Specification<Department> specification = Specification.where(null);
        if (filter.getCollageId() != null) {
            specification = ((Specification<Department>) specification).and((root, query, cb) -> {
                return cb.equal(root.get(Department_.college).get(College_.id),filter.getCollageId());
            });
        }

        if (filter.getDepartmentName() != null){
            specification = ((Specification<Department>) specification).and((root, query, cb) -> {
                return  cb.like(root.get(Department_.name),"%" + filter.getDepartmentName() + "%");
            });
        }
        return departmentRepository.findAll(specification, page)
            .map(departmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Department> createSpecification(DepartmentCriteria criteria) {
        Specification<Department> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Department_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Department_.name));
            }
            if (criteria.getSymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol(), Department_.symbol));
            }
            if (criteria.getCoverImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImgUrl(), Department_.coverImgUrl));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(Department_.courses, JoinType.LEFT).get(Course_.id)));
            }
            if (criteria.getCollegeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCollegeId(),
                    root -> root.join(Department_.college, JoinType.LEFT).get(College_.id)));
            }
        }
        return specification;
    }
}
