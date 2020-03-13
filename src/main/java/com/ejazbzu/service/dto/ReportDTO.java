package com.ejazbzu.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ejazbzu.domain.Report} entity.
 */
public class ReportDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String message;


    private Long documentId;

    private String documentTitle;

    private Long studentId;

    private String studentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;
        if (reportDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", documentId=" + getDocumentId() +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", studentId=" + getStudentId() +
            ", studentName='" + getStudentName() + "'" +
            "}";
    }
}
