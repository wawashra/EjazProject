package com.ejazbzu.repository;

import com.ejazbzu.domain.AttachmentType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AttachmentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentTypeRepository extends JpaRepository<AttachmentType, Long>, JpaSpecificationExecutor<AttachmentType> {

}
