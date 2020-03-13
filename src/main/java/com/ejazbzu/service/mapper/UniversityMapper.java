package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.UniversityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link University} and its DTO {@link UniversityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UniversityMapper extends EntityMapper<UniversityDTO, University> {


    @Mapping(target = "colleges", ignore = true)
    @Mapping(target = "removeCollege", ignore = true)
    University toEntity(UniversityDTO universityDTO);

    default University fromId(Long id) {
        if (id == null) {
            return null;
        }
        University university = new University();
        university.setId(id);
        return university;
    }
}
