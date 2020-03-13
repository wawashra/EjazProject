package com.ejazbzu.repository;

import com.ejazbzu.domain.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query(value = "select distinct student from Student student left join fetch student.courses",
        countQuery = "select count(distinct student) from Student student")
    Page<Student> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct student from Student student left join fetch student.courses")
    List<Student> findAllWithEagerRelationships();

    @Query("select student from Student student left join fetch student.courses where student.id =:id")
    Optional<Student> findOneWithEagerRelationships(@Param("id") Long id);

}
