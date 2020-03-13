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
 * Criteria class for the {@link com.ejazbzu.domain.Department} entity. This class is used
 * in {@link com.ejazbzu.web.rest.DepartmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /departments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter symbol;

    private StringFilter coverImgUrl;

    private LongFilter courseId;

    private LongFilter collegeId;

    public DepartmentCriteria() {
    }

    public DepartmentCriteria(DepartmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.symbol = other.symbol == null ? null : other.symbol.copy();
        this.coverImgUrl = other.coverImgUrl == null ? null : other.coverImgUrl.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.collegeId = other.collegeId == null ? null : other.collegeId.copy();
    }

    @Override
    public DepartmentCriteria copy() {
        return new DepartmentCriteria(this);
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

    public StringFilter getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(StringFilter coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(LongFilter collegeId) {
        this.collegeId = collegeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DepartmentCriteria that = (DepartmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(symbol, that.symbol) &&
            Objects.equals(coverImgUrl, that.coverImgUrl) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(collegeId, that.collegeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        symbol,
        coverImgUrl,
        courseId,
        collegeId
        );
    }

    @Override
    public String toString() {
        return "DepartmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (symbol != null ? "symbol=" + symbol + ", " : "") +
                (coverImgUrl != null ? "coverImgUrl=" + coverImgUrl + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
                (collegeId != null ? "collegeId=" + collegeId + ", " : "") +
            "}";
    }

}
