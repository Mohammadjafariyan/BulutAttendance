package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * برخی تنظیمات کلی سیستم بصورت سالانه
 * شامل فرمول محاسبه مالیات و بیمه
 */
@Schema(description = "برخی تنظیمات کلی سیستم بصورت سالانه\nشامل فرمول محاسبه مالیات و بیمه")
@Entity
@Table(name = "sys_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sysconfig")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "tax_formula")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String taxFormula;

    @Column(name = "sanavat_formula")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sanavatFormula;

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

    public String getTaxFormula() {
        return this.taxFormula;
    }

    public SysConfig taxFormula(String taxFormula) {
        this.setTaxFormula(taxFormula);
        return this;
    }

    public void setTaxFormula(String taxFormula) {
        this.taxFormula = taxFormula;
    }

    public String getSanavatFormula() {
        return this.sanavatFormula;
    }

    public SysConfig sanavatFormula(String sanavatFormula) {
        this.setSanavatFormula(sanavatFormula);
        return this;
    }

    public void setSanavatFormula(String sanavatFormula) {
        this.sanavatFormula = sanavatFormula;
    }

    public Integer getYear() {
        return this.year;
    }

    public SysConfig year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public UUID getId() {
        return this.id;
    }

    public SysConfig id(UUID id) {
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

    public SysConfig status(RecordStatus recordStatus) {
        this.setStatus(recordStatus);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public SysConfig internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public SysConfig company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((SysConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysConfig{" +
            "id=" + getId() +
            ", taxFormula='" + getTaxFormula() + "'" +
            ", sanavatFormula='" + getSanavatFormula() + "'" +
            ", year=" + getYear() +
            "}";
    }
}
