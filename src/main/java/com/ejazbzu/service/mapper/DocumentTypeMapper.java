package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.DocumentTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentType} and its DTO {@link DocumentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentTypeMapper extends EntityMapper<DocumentTypeDTO, DocumentType> {


    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "removeDocument", ignore = true)
    DocumentType toEntity(DocumentTypeDTO documentTypeDTO);

    default DocumentType fromId(Long id) {
        if (id == null) {
            return null;
        }
        DocumentType documentType = new DocumentType();
        documentType.setId(id);
        return documentType;
    }
}
