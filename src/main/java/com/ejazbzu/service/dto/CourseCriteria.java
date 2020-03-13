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
 * Criteria class for the {@link com.ejazbzu.domain.Course} entity. This class is used
 * in {@link com.ejazbzu.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter symbol;

    private StringFilter description;

    private StringFilter coverImgUrl;

    private LongFilter documentId;

    private LongFilter departmentId;

    private LongFilter studentsId;

    public CourseCriteria() {
    }

    public CourseCriteria(CourseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.symbol = other.symbol == null ? null : other.symbol.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.coverImgUrl = other.coverImgUrl == null ? null : other.coverImgUrl.copy();
        this.documentId = other.documentId == null ? null : other.documentId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.studentsId = other.studentsId == null ? null : other.studentsId.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
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

    public StringFilter getSymbol() {
        return symbol;
    }

    public void setSymbol(StringFilter symbol) {
        this.symbol = symbol;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(StringFilter coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(LongFilter studentsId) {
        this.studentsId = studentsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(symbol, that.symbol) &&
            Objects.equals(description, that.description) &&
            Objects.equals(coverImgUrl, that.coverImgUrl) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(studentsId, that.studentsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        symbol,
        description,
        coverImgUrl,
        documentId,
        departmentId,
        studentsId
        );
    }

    @Override
    public String toString() {
        return "CourseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (symbol != null ? "symbol=" + symbol + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (coverImgUrl != null ? "coverImgUrl=" + coverImgUrl + ", " : "") +
                (documentId != null ? "documentId=" + documentId + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
                (studentsId != null ? "studentsId=" + studentsId + ", " : "") +
            "}";
    }

}
