package com.ejazbzu.service.impl;

import com.ejazbzu.service.CollegeService;
import com.ejazbzu.domain.College;
import com.ejazbzu.repository.CollegeRepository;
import com.ejazbzu.service.dto.CollegeDTO;
import com.ejazbzu.service.mapper.CollegeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link College}.
 */
@Service
@Transactional
public class CollegeServiceImpl implements CollegeService {

    private final Logger log = LoggerFactory.getLogger(CollegeServiceImpl.class);

    private final CollegeRepository collegeRepository;

    private final CollegeMapper collegeMapper;

    public CollegeServiceImpl(CollegeRepository collegeRepository, CollegeMapper collegeMapper) {
        this.collegeRepository = collegeRepository;
        this.collegeMapper = collegeMapper;
    }

    /**
     * Save a college.
     *
     * @param collegeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CollegeDTO save(CollegeDTO collegeDTO) {
        log.debug("Request to save College : {}", collegeDTO);
        College college = collegeMapper.toEntity(collegeDTO);
        college = collegeRepository.save(college);
        return collegeMapper.toDto(college);
    }

    /**
     * Get all the colleges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CollegeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Colleges");
        return collegeRepository.findAll(pageable)
            .map(collegeMapper::toDto);
    }

    /**
     * Get one college by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDTO> findOne(Long id) {
        log.debug("Request to get College : {}", id);
        return collegeRepository.findById(id)
            .map(collegeMapper::toDto);
    }

    /**
     * Delete the college by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete College : {}", id);
        collegeRepository.deleteById(id);
    }
}
