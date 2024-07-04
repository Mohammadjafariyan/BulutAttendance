package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * تنظیمات هر ساله مالیات که
 * هر ساله توسط دولت مشخص می شود
 */
@Schema(description = "تنظیمات هر ساله مالیات که\nهر ساله توسط دولت مشخص می شود")
@Entity
@Table(name = "tax_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "taxtemplate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "range_from", precision = 21, scale = 2)
    private BigDecimal rangeFrom;

    @Column(name = "range_to", precision = 21, scale = 2)
    private BigDecimal rangeTo;

    @Column(name = "jhi_percent")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer percent;

    @Column(name = "year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer year;

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

    public BigDecimal getRangeFrom() {
        return this.rangeFrom;
    }

    public TaxTemplate rangeFrom(BigDecimal rangeFrom) {
        this.setRangeFrom(rangeFrom);
        return this;
    }

    public void setRangeFrom(BigDecimal rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public BigDecimal getRangeTo() {
        return this.rangeTo;
    }

    public TaxTemplate rangeTo(BigDecimal rangeTo) {
        this.setRangeTo(rangeTo);
        return this;
    }

    public void setRangeTo(BigDecimal rangeTo) {
        this.rangeTo = rangeTo;
    }

    public Integer getPercent() {
        return this.percent;
    }

    public TaxTemplate percent(Integer percent) {
        this.setPercent(percent);
        return this;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Integer getYear() {
        return this.year;
    }

    public TaxTemplate year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public UUID getId() {
        return this.id;
    }

    public TaxTemplate id(UUID id) {
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

    public TaxTemplate status(RecordStatus recordStatus) {
        this.setStatus(recordStatus);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public TaxTemplate internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public TaxTemplate company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((TaxTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxTemplate{" +
            "id=" + getId() +
            ", rangeFrom=" + getRangeFrom() +
            ", rangeTo=" + getRangeTo() +
            ", percent=" + getPercent() +
            ", year=" + getYear() +
            "}";
    }
}
