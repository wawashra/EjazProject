package com.ejazbzu.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ejazbzu.domain.AttachmentType} entity.
 */
public class AttachmentTypeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttachmentTypeDTO attachmentTypeDTO = (AttachmentTypeDTO) o;
        if (attachmentTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attachmentTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttachmentTypeDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
