package com.ejazbzu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A College.
 */
@Entity
@Table(name = "college")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class College extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "symbol", nullable = false, unique = true)
    private String symbol;

    @Column(name = "cover_img_url")
    private String coverImgUrl;

    @OneToMany(mappedBy = "college")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Department> departments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("colleges")
    private University university;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public College name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public College symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public College coverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
        return this;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public College departments(Set<Department> departments) {
        this.departments = departments;
        return this;
    }

    public College addDepartment(Department department) {
        this.departments.add(department);
        department.setCollege(this);
        return this;
    }

    public College removeDepartment(Department department) {
        this.departments.remove(department);
        department.setCollege(null);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public University getUniversity() {
        return university;
    }

    public College university(University university) {
        this.university = university;
        return this;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof College)) {
            return false;
        }
        return id != null && id.equals(((College) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "College{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", coverImgUrl='" + getCoverImgUrl() + "'" +
            "}";
    }
}
