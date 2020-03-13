package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.ReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring", uses = {DocumentMapper.class, StudentMapper.class})
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {

    @Mapping(source = "document.id", target = "documentId")
    @Mapping(source = "document.title", target = "documentTitle")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    ReportDTO toDto(Report report);

    @Mapping(source = "documentId", target = "document")
    @Mapping(source = "studentId", target = "student")
    Report toEntity(ReportDTO reportDTO);

    default Report fromId(Long id) {
        if (id == null) {
            return null;
        }
        Report report = new Report();
        report.setId(id);
        return report;
    }
}
