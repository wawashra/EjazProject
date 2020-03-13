package com.ejazbzu.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ejazbzu.domain.College} entity.
 */
public class CollegeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String symbol;

    private String coverImgUrl;


    private Long universityId;

    private String universitySymbol;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getUniversitySymbol() {
        return universitySymbol;
    }

    public void setUniversitySymbol(String universitySymbol) {
        this.universitySymbol = universitySymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CollegeDTO collegeDTO = (CollegeDTO) o;
        if (collegeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collegeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CollegeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", coverImgUrl='" + getCoverImgUrl() + "'" +
            ", universityId=" + getUniversityId() +
            ", universitySymbol='" + getUniversitySymbol() + "'" +
            "}";
    }
}
