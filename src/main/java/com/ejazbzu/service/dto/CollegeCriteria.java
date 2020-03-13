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
 * Criteria class for the {@link com.ejazbzu.domain.College} entity. This class is used
 * in {@link com.ejazbzu.web.rest.CollegeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /colleges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CollegeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter symbol;

    private StringFilter coverImgUrl;

    private LongFilter departmentId;

    private LongFilter universityId;

    public CollegeCriteria() {
    }

    public CollegeCriteria(CollegeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.symbol = other.symbol == null ? null : other.symbol.copy();
        this.coverImgUrl = other.coverImgUrl == null ? null : other.coverImgUrl.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.universityId = other.universityId == null ? null : other.universityId.copy();
    }

    @Override
    public CollegeCriteria copy() {
        return new CollegeCriteria(this);
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

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getUniversityId() {
        return universityId;
    }

    public void setUniversityId(LongFilter universityId) {
        this.universityId = universityId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CollegeCriteria that = (CollegeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(symbol, that.symbol) &&
            Objects.equals(coverImgUrl, that.coverImgUrl) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(universityId, that.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        symbol,
        coverImgUrl,
        departmentId,
        universityId
        );
    }

    @Override
    public String toString() {
        return "CollegeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (symbol != null ? "symbol=" + symbol + ", " : "") +
                (coverImgUrl != null ? "coverImgUrl=" + coverImgUrl + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
                (universityId != null ? "universityId=" + universityId + ", " : "") +
            "}";
    }

}
