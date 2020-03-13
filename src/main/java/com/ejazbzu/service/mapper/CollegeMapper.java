package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.CollegeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link College} and its DTO {@link CollegeDTO}.
 */
@Mapper(componentModel = "spring", uses = {UniversityMapper.class})
public interface CollegeMapper extends EntityMapper<CollegeDTO, College> {

    @Mapping(source = "university.id", target = "universityId")
    @Mapping(source = "university.symbol", target = "universitySymbol")
    CollegeDTO toDto(College college);

    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "removeDepartment", ignore = true)
    @Mapping(source = "universityId", target = "university")
    College toEntity(CollegeDTO collegeDTO);

    default College fromId(Long id) {
        if (id == null) {
            return null;
        }
        College college = new College();
        college.setId(id);
        return college;
    }
}
