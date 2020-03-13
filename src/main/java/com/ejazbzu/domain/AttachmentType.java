package com.ejazbzu.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AttachmentType.
 */
@Entity
@Table(name = "attachment_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AttachmentType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "attachmentType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attachment> attachments = new HashSet<>();

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

    public AttachmentType type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public AttachmentType attachments(Set<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public AttachmentType addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setAttachmentType(this);
        return this;
    }

    public AttachmentType removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setAttachmentType(null);
        return this;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentType)) {
            return false;
        }
        return id != null && id.equals(((AttachmentType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AttachmentType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
