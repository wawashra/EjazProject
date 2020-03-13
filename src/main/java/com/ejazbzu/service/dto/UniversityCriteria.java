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
 * Criteria class for the {@link com.ejazbzu.domain.University} entity. This class is used
 * in {@link com.ejazbzu.web.rest.UniversityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /universities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UniversityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter symbol;

    private LongFilter collegeId;

    public UniversityCriteria() {
    }

    public UniversityCriteria(UniversityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.symbol = other.symbol == null ? null : other.symbol.copy();
        this.collegeId = other.collegeId == null ? null : other.collegeId.copy();
    }

    @Override
    public UniversityCriteria copy() {
        return new UniversityCriteria(this);
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
        final UniversityCriteria that = (UniversityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(symbol, that.symbol) &&
            Objects.equals(collegeId, that.collegeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        symbol,
        collegeId
        );
    }

    @Override
    public String toString() {
        return "UniversityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (symbol != null ? "symbol=" + symbol + ", " : "") +
                (collegeId != null ? "collegeId=" + collegeId + ", " : "") +
            "}";
    }

}
