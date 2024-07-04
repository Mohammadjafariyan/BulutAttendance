package com.bulut.attendance.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RecordStatus.
 */
@Entity
@Table(name = "record_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "recordstatus")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecordStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "from_date_time")
    private ZonedDateTime fromDateTime;

    @Column(name = "to_date_time")
    private ZonedDateTime toDateTime;

    @Column(name = "is_deleted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeleted;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ZonedDateTime getFromDateTime() {
        return this.fromDateTime;
    }

    public RecordStatus fromDateTime(ZonedDateTime fromDateTime) {
        this.setFromDateTime(fromDateTime);
        return this;
    }

    public void setFromDateTime(ZonedDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public ZonedDateTime getToDateTime() {
        return this.toDateTime;
    }

    public RecordStatus toDateTime(ZonedDateTime toDateTime) {
        this.setToDateTime(toDateTime);
        return this;
    }

    public void setToDateTime(ZonedDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public RecordStatus isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UUID getId() {
        return this.id;
    }

    public RecordStatus id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((RecordStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordStatus{" +
            "id=" + getId() +
            ", fromDateTime='" + getFromDateTime() + "'" +
            ", toDateTime='" + getToDateTime() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
