package com.ejazbzu.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ejazbzu.domain.Attachment} entity.
 */
public class AttachmentDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String url;

    @NotNull
    private String extension;

    @NotNull
    private String fileSize;

    private Integer hits;


    private Long documentId;

    private String documentTitle;

    private Long attachmentTypeId;

    private String attachmentTypeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public Long getAttachmentTypeId() {
        return attachmentTypeId;
    }

    public void setAttachmentTypeId(Long attachmentTypeId) {
        this.attachmentTypeId = attachmentTypeId;
    }

    public String getAttachmentTypeType() {
        return attachmentTypeType;
    }

    public void setAttachmentTypeType(String attachmentTypeType) {
        this.attachmentTypeType = attachmentTypeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttachmentDTO attachmentDTO = (AttachmentDTO) o;
        if (attachmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attachmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", extension='" + getExtension() + "'" +
            ", fileSize='" + getFileSize() + "'" +
            ", hits=" + getHits() +
            ", documentId=" + getDocumentId() +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", attachmentTypeId=" + getAttachmentTypeId() +
            ", attachmentTypeType='" + getAttachmentTypeType() + "'" +
            "}";
    }
}
