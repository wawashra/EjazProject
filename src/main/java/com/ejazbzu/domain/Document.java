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
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "description")
    private String description;

    @Column(name = "rating_sum")
    private Integer ratingSum;

    @Column(name = "rating_number")
    private Integer ratingNumber;

    @Column(name = "view")
    private Integer view;

    @OneToMany(mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Report> reports = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "document_tags",
               joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("documents")
    private Course course;

    @ManyToOne
    @JsonIgnoreProperties("documents")
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Document title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isActive() {
        return active;
    }

    public Document active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public Document description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRatingSum() {
        return ratingSum;
    }

    public Document ratingSum(Integer ratingSum) {
        this.ratingSum = ratingSum;
        return this;
    }

    public void setRatingSum(Integer ratingSum) {
        this.ratingSum = ratingSum;
    }

    public Integer getRatingNumber() {
        return ratingNumber;
    }

    public Document ratingNumber(Integer ratingNumber) {
        this.ratingNumber = ratingNumber;
        return this;
    }

    public void setRatingNumber(Integer ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public Integer getView() {
        return view;
    }

    public Document view(Integer view) {
        this.view = view;
        return this;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public Document attachments(Set<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Document addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setDocument(this);
        return this;
    }

    public Document removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setDocument(null);
        return this;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public Document reports(Set<Report> reports) {
        this.reports = reports;
        return this;
    }

    public Document addReport(Report report) {
        this.reports.add(report);
        report.setDocument(this);
        return this;
    }

    public Document removeReport(Report report) {
        this.reports.remove(report);
        report.setDocument(null);
        return this;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Document tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Document addTags(Tag tag) {
        this.tags.add(tag);
        tag.getDocuments().add(this);
        return this;
    }

    public Document removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getDocuments().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Course getCourse() {
        return course;
    }

    public Document course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public Document student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", active='" + isActive() + "'" +
            ", description='" + getDescription() + "'" +
            ", ratingSum=" + getRatingSum() +
            ", ratingNumber=" + getRatingNumber() +
            ", view=" + getView() +
            "}";
    }
}
