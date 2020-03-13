package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.AttachmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {DocumentMapper.class, AttachmentTypeMapper.class})
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {

    @Mapping(source = "document.id", target = "documentId")
    @Mapping(source = "document.title", target = "documentTitle")
    @Mapping(source = "attachmentType.id", target = "attachmentTypeId")
    @Mapping(source = "attachmentType.type", target = "attachmentTypeType")
    AttachmentDTO toDto(Attachment attachment);

    @Mapping(source = "documentId", target = "document")
    @Mapping(source = "attachmentTypeId", target = "attachmentType")
    Attachment toEntity(AttachmentDTO attachmentDTO);

    default Attachment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return attachment;
    }
}
