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
 * Criteria class for the {@link com.ejazbzu.domain.Document} entity. This class is used
 * in {@link com.ejazbzu.web.rest.DocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private BooleanFilter active;

    private StringFilter description;

    private IntegerFilter ratingSum;

    private IntegerFilter ratingNumber;

    private IntegerFilter view;

    private LongFilter attachmentId;

    private LongFilter reportId;

    private LongFilter tagsId;

    private LongFilter courseId;

    private LongFilter documentTypeId;

    private LongFilter studentId;

    public DocumentCriteria() {
    }

    public DocumentCriteria(DocumentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.ratingSum = other.ratingSum == null ? null : other.ratingSum.copy();
        this.ratingNumber = other.ratingNumber == null ? null : other.ratingNumber.copy();
        this.view = other.view == null ? null : other.view.copy();
        this.attachmentId = other.attachmentId == null ? null : other.attachmentId.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.documentTypeId = other.documentTypeId == null ? null : other.documentTypeId.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(IntegerFilter ratingSum) {
        this.ratingSum = ratingSum;
    }

    public IntegerFilter getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(IntegerFilter ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public IntegerFilter getView() {
        return view;
    }

    public void setView(IntegerFilter view) {
        this.view = view;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }

    public LongFilter getReportId() {
        return reportId;
    }

    public void setReportId(LongFilter reportId) {
        this.reportId = reportId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(LongFilter documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentCriteria that = (DocumentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(active, that.active) &&
            Objects.equals(description, that.description) &&
            Objects.equals(ratingSum, that.ratingSum) &&
            Objects.equals(ratingNumber, that.ratingNumber) &&
            Objects.equals(view, that.view) &&
            Objects.equals(attachmentId, that.attachmentId) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        active,
        description,
        ratingSum,
        ratingNumber,
        view,
        attachmentId,
        reportId,
        tagsId,
        courseId,
        documentTypeId,
        studentId
        );
    }

    @Override
    public String toString() {
        return "DocumentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (ratingSum != null ? "ratingSum=" + ratingSum + ", " : "") +
                (ratingNumber != null ? "ratingNumber=" + ratingNumber + ", " : "") +
                (view != null ? "view=" + view + ", " : "") +
                (attachmentId != null ? "attachmentId=" + attachmentId + ", " : "") +
                (reportId != null ? "reportId=" + reportId + ", " : "") +
                (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
                (documentTypeId != null ? "documentTypeId=" + documentTypeId + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
            "}";
    }

}
