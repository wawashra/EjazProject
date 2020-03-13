package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.StudentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UniversityMapper.class, DepartmentMapper.class, CollegeMapper.class, CourseMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "university.id", target = "universityId")
    @Mapping(source = "university.name", target = "universityName")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "college.id", target = "collegeId")
    @Mapping(source = "college.name", target = "collegeName")
    StudentDTO toDto(Student student);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "universityId", target = "university")
    @Mapping(source = "departmentId", target = "department")
    @Mapping(source = "collegeId", target = "college")
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "removeDocuments", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "removeReport", ignore = true)
    @Mapping(target = "removeCourses", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    default Student fromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
}
