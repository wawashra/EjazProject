package com.ejazbzu.service;

import com.ejazbzu.service.dto.UniversityDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.ejazbzu.domain.University}.
 */
public interface UniversityService {

    /**
     * Save a university.
     *
     * @param universityDTO the entity to save.
     * @return the persisted entity.
     */
    UniversityDTO save(UniversityDTO universityDTO);

    /**
     * Get all the universities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" university.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UniversityDTO> findOne(Long id);

    /**
     * Delete the "id" university.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
