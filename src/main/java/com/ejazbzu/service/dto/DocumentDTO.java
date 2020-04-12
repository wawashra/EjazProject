package com.ejazbzu.service.dto;

import com.ejazbzu.domain.Attachment;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.ejazbzu.domain.Document} entity.
 */
public class DocumentDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Boolean active;

    private String description;

    private Integer ratingSum;

    private Integer ratingNumber;

    private Integer view;


    private Set<TagDTO> tags = new HashSet<>();

    private Long courseId;

    private String courseSymbol;

    private Long studentId;

    private String studentName;

    private Set<Attachment> attachments = new HashSet<>();

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(Integer ratingSum) {
        this.ratingSum = ratingSum;
    }

    public Integer getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(Integer ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseSymbol() {
        return courseSymbol;
    }

    public void setCourseSymbol(String courseSymbol) {
        this.courseSymbol = courseSymbol;
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

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (documentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", active='" + isActive() + "'" +
            ", description='" + getDescription() + "'" +
            ", ratingSum=" + getRatingSum() +
            ", ratingNumber=" + getRatingNumber() +
            ", view=" + getView() +
            ", courseId=" + getCourseId() +
            ", courseSymbol='" + getCourseSymbol() + "'" +
            ", studentId=" + getStudentId() +
            ", studentName='" + getStudentName() + "'" +
            "}";
    }
}
