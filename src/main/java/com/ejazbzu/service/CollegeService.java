package com.ejazbzu.service;

import com.ejazbzu.service.dto.CollegeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.ejazbzu.domain.College}.
 */
public interface CollegeService {

    /**
     * Save a college.
     *
     * @param collegeDTO the entity to save.
     * @return the persisted entity.
     */
    CollegeDTO save(CollegeDTO collegeDTO);

    /**
     * Get all the colleges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CollegeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" college.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CollegeDTO> findOne(Long id);

    /**
     * Delete the "id" college.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
