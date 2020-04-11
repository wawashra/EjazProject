package com.ejazbzu.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DocumentType.
 */
@Entity
@Table(name = "document_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "documentType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Document> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public DocumentType type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public DocumentType documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public DocumentType addDocument(Document document) {
        this.documents.add(document);
        document.setDocumentType(this);
        return this;
    }

    public DocumentType removeDocument(Document document) {
        this.documents.remove(document);
        document.setDocumentType(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentType)) {
            return false;
        }
        return id != null && id.equals(((DocumentType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DocumentType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
