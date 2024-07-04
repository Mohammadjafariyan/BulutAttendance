package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * مرخصی
 *
 */
@Schema(description = "مرخصی\n")
@Entity
@Table(name = "leave")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leave")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "start")
    private ZonedDateTime start;

    @Column(name = "jhi_end")
    private ZonedDateTime end;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "bpms_approve_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer bpmsApproveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecordStatus status;

    /**
     * مرخصی پرسنل
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company" }, allowSetters = true)
    private Personnel personnelId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ZonedDateTime getStart() {
        return this.start;
    }

    public Leave start(ZonedDateTime start) {
        this.setStart(start);
        return this;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return this.end;
    }

    public Leave end(ZonedDateTime end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public UUID getId() {
        return this.id;
    }

    public Leave id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getBpmsApproveStatus() {
        return this.bpmsApproveStatus;
    }

    public Leave bpmsApproveStatus(Integer bpmsApproveStatus) {
        this.setBpmsApproveStatus(bpmsApproveStatus);
        return this;
    }

    public void setBpmsApproveStatus(Integer bpmsApproveStatus) {
        this.bpmsApproveStatus = bpmsApproveStatus;
    }

    public RecordStatus getStatus() {
        return this.status;
    }

    public void setStatus(RecordStatus recordStatus) {
        this.status = recordStatus;
    }

    public Leave status(RecordStatus recordStatus) {
        this.setStatus(recordStatus);
        return this;
    }

    public Personnel getPersonnelId() {
        return this.personnelId;
    }

    public void setPersonnelId(Personnel personnel) {
        this.personnelId = personnel;
    }

    public Leave personnelId(Personnel personnel) {
        this.setPersonnelId(personnel);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public Leave internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Leave company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Leave)) {
            return false;
        }
        return getId() != null && getId().equals(((Leave) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Leave{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", bpmsApproveStatus=" + getBpmsApproveStatus() +
            "}";
    }
}
