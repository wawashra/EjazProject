package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.symbol", target = "departmentSymbol")
    CourseDTO toDto(Course course);

    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "removeDocument", ignore = true)
    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "removeStudents", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
