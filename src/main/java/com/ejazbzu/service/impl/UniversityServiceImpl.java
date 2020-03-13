package com.ejazbzu.service.impl;

import com.ejazbzu.service.UniversityService;
import com.ejazbzu.domain.University;
import com.ejazbzu.repository.UniversityRepository;
import com.ejazbzu.service.dto.UniversityDTO;
import com.ejazbzu.service.mapper.UniversityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link University}.
 */
@Service
@Transactional
public class UniversityServiceImpl implements UniversityService {

    private final Logger log = LoggerFactory.getLogger(UniversityServiceImpl.class);

    private final UniversityRepository universityRepository;

    private final UniversityMapper universityMapper;

    public UniversityServiceImpl(UniversityRepository universityRepository, UniversityMapper universityMapper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
    }

    /**
     * Save a university.
     *
     * @param universityDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UniversityDTO save(UniversityDTO universityDTO) {
        log.debug("Request to save University : {}", universityDTO);
        University university = universityMapper.toEntity(universityDTO);
        university = universityRepository.save(university);
        return universityMapper.toDto(university);
    }

    /**
     * Get all the universities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UniversityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Universities");
        return universityRepository.findAll(pageable)
            .map(universityMapper::toDto);
    }

    /**
     * Get one university by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UniversityDTO> findOne(Long id) {
        log.debug("Request to get University : {}", id);
        return universityRepository.findById(id)
            .map(universityMapper::toDto);
    }

    /**
     * Delete the university by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete University : {}", id);
        universityRepository.deleteById(id);
    }
}
