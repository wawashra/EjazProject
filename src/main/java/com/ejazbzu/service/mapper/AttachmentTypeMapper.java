package com.ejazbzu.service.mapper;


import com.ejazbzu.domain.*;
import com.ejazbzu.service.dto.AttachmentTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttachmentType} and its DTO {@link AttachmentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AttachmentTypeMapper extends EntityMapper<AttachmentTypeDTO, AttachmentType> {


    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "removeAttachment", ignore = true)
    AttachmentType toEntity(AttachmentTypeDTO attachmentTypeDTO);

    default AttachmentType fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttachmentType attachmentType = new AttachmentType();
        attachmentType.setId(id);
        return attachmentType;
    }
}
