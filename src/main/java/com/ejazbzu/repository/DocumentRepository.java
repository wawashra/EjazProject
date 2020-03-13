package com.ejazbzu.repository;

import com.ejazbzu.domain.Document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Document entity.
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    @Query(value = "select distinct document from Document document left join fetch document.tags",
        countQuery = "select count(distinct document) from Document document")
    Page<Document> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct document from Document document left join fetch document.tags")
    List<Document> findAllWithEagerRelationships();

    @Query("select document from Document document left join fetch document.tags where document.id =:id")
    Optional<Document> findOneWithEagerRelationships(@Param("id") Long id);

}
