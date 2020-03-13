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

import com.ejazbzu.domain.Student;
import com.ejazbzu.domain.*; // for static metamodels
import com.ejazbzu.repository.StudentRepository;
import com.ejazbzu.service.dto.StudentCriteria;
import com.ejazbzu.service.dto.StudentDTO;
import com.ejazbzu.service.mapper.StudentMapper;

/**
 * Service for executing complex queries for {@link Student} entities in the database.
 * The main input is a {@link StudentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentDTO} or a {@link Page} of {@link StudentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentQueryService extends QueryService<Student> {

    private final Logger log = LoggerFactory.getLogger(StudentQueryService.class);

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public StudentQueryService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    /**
     * Return a {@link List} of {@link StudentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findByCriteria(StudentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentMapper.toDto(studentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentDTO> findByCriteria(StudentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification, page)
            .map(studentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Student> createSpecification(StudentCriteria criteria) {
        Specification<Student> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Student_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Student_.name));
            }
            if (criteria.getBirthday() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthday(), Student_.birthday));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Student_.phoneNumber));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Student_.gender));
            }
            if (criteria.getProfileImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfileImgUrl(), Student_.profileImgUrl));
            }
            if (criteria.getCoverImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImgUrl(), Student_.coverImgUrl));
            }
            if (criteria.getStar() != null) {
                specification = specification.and(buildSpecification(criteria.getStar(), Student_.star));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Student_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getUniversityId() != null) {
                specification = specification.and(buildSpecification(criteria.getUniversityId(),
                    root -> root.join(Student_.university, JoinType.LEFT).get(University_.id)));
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getDepartmentId(),
                    root -> root.join(Student_.department, JoinType.LEFT).get(Department_.id)));
            }
            if (criteria.getCollegeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCollegeId(),
                    root -> root.join(Student_.college, JoinType.LEFT).get(College_.id)));
            }
            if (criteria.getDocumentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentsId(),
                    root -> root.join(Student_.documents, JoinType.LEFT).get(Document_.id)));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(),
                    root -> root.join(Student_.reports, JoinType.LEFT).get(Report_.id)));
            }
            if (criteria.getCoursesId() != null) {
                specification = specification.and(buildSpecification(criteria.getCoursesId(),
                    root -> root.join(Student_.courses, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}
