package com.ejazbzu.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ejazbzu.domain.Attachment} entity. This class is used
 * in {@link com.ejazbzu.web.rest.AttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttachmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter url;

    private StringFilter extension;

    private StringFilter fileSize;

    private IntegerFilter hits;

    private LongFilter documentId;

    private LongFilter attachmentTypeId;

    public AttachmentCriteria() {
    }

    public AttachmentCriteria(AttachmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.extension = other.extension == null ? null : other.extension.copy();
        this.fileSize = other.fileSize == null ? null : other.fileSize.copy();
        this.hits = other.hits == null ? null : other.hits.copy();
        this.documentId = other.documentId == null ? null : other.documentId.copy();
        this.attachmentTypeId = other.attachmentTypeId == null ? null : other.attachmentTypeId.copy();
    }

    @Override
    public AttachmentCriteria copy() {
        return new AttachmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getExtension() {
        return extension;
    }

    public void setExtension(StringFilter extension) {
        this.extension = extension;
    }

    public StringFilter getFileSize() {
        return fileSize;
    }

    public void setFileSize(StringFilter fileSize) {
        this.fileSize = fileSize;
    }

    public IntegerFilter getHits() {
        return hits;
    }

    public void setHits(IntegerFilter hits) {
        this.hits = hits;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public LongFilter getAttachmentTypeId() {
        return attachmentTypeId;
    }

    public void setAttachmentTypeId(LongFilter attachmentTypeId) {
        this.attachmentTypeId = attachmentTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttachmentCriteria that = (AttachmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(url, that.url) &&
            Objects.equals(extension, that.extension) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(hits, that.hits) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(attachmentTypeId, that.attachmentTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        url,
        extension,
        fileSize,
        hits,
        documentId,
        attachmentTypeId
        );
    }

    @Override
    public String toString() {
        return "AttachmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (extension != null ? "extension=" + extension + ", " : "") +
                (fileSize != null ? "fileSize=" + fileSize + ", " : "") +
                (hits != null ? "hits=" + hits + ", " : "") +
                (documentId != null ? "documentId=" + documentId + ", " : "") +
                (attachmentTypeId != null ? "attachmentTypeId=" + attachmentTypeId + ", " : "") +
            "}";
    }

}
