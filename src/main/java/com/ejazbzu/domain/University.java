package com.ejazbzu.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A University.
 */
@Entity
@Table(name = "university")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class University extends AbstractAuditingEntity implements Serializable {

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

    @OneToMany(mappedBy = "university")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<College> colleges = new HashSet<>();

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

    public University name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public University symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Set<College> getColleges() {
        return colleges;
    }

    public University colleges(Set<College> colleges) {
        this.colleges = colleges;
        return this;
    }

    public University addCollege(College college) {
        this.colleges.add(college);
        college.setUniversity(this);
        return this;
    }

    public University removeCollege(College college) {
        this.colleges.remove(college);
        college.setUniversity(null);
        return this;
    }

    public void setColleges(Set<College> colleges) {
        this.colleges = colleges;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof University)) {
            return false;
        }
        return id != null && id.equals(((University) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "University{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", symbol='" + getSymbol() + "'" +
            "}";
    }
}
