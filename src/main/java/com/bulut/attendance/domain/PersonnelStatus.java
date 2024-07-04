package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * جانباز ، عادی
 * کارورز ، سهامدار و...
 */
@Schema(description = "جانباز ، عادی\nکارورز ، سهامدار و...")
@Entity
@Table(name = "personnel_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "personnelstatus")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonnelStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecordStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getTitle() {
        return this.title;
    }

    public PersonnelStatus title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return this.id;
    }

    public PersonnelStatus id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RecordStatus getStatus() {
        return this.status;
    }

    public void setStatus(RecordStatus recordStatus) {
        this.status = recordStatus;
    }

    public PersonnelStatus status(RecordStatus recordStatus) {
        this.setStatus(recordStatus);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public PersonnelStatus internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PersonnelStatus company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonnelStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonnelStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonnelStatus{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
