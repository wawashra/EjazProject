package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.DocumentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class, CourseMapper.class, DocumentTypeMapper.class, StudentMapper.class})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.symbol", target = "courseSymbol")
    @Mapping(source = "documentType.id", target = "documentTypeId")
    @Mapping(source = "documentType.type", target = "documentTypeType")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    DocumentDTO toDto(Document document);

    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "removeAttachment", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "removeReport", ignore = true)
    @Mapping(target = "removeTags", ignore = true)
    @Mapping(source = "courseId", target = "course")
    @Mapping(source = "documentTypeId", target = "documentType")
    @Mapping(source = "studentId", target = "student")
    Document toEntity(DocumentDTO documentDTO);

    default Document fromId(Long id) {
        if (id == null) {
            return null;
        }
        Document document = new Document();
        document.setId(id);
        return document;
    }
}
