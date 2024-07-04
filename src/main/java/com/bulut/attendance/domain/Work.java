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
 * تمامی مقادیر شامل کارکرد و
 * آنچه در حکم تعریف شده در این جدول مقدار دهی می شود
 * و حقوق پرسنل و مزایا و کسورات و کارکرد ثبت می شود
 */
@Schema(
    description = "تمامی مقادیر شامل کارکرد و\nآنچه در حکم تعریف شده در این جدول مقدار دهی می شود\nو حقوق پرسنل و مزایا و کسورات و کارکرد ثبت می شود"
)
@Entity
@Table(name = "work")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "work")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Work implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    /**
     * تاریخ ثبت
     *
     */
    @Schema(description = "تاریخ ثبت\n")
    @Column(name = "issue_date")
    private ZonedDateTime issueDate;

    /**
     * توضیحات
     *
     */
    @Schema(description = "توضیحات\n")
    @Column(name = "jhi_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String desc;

    /**
     * برای مثال این فیش یا اطلاعات ثبتی مربوط به کدام سال است
     *
     */
    @Schema(description = "برای مثال این فیش یا اطلاعات ثبتی مربوط به کدام سال است\n")
    @Column(name = "year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer year;

    /**
     * مربوط به کدام ماه است
     *
     */
    @Schema(description = "مربوط به کدام ماه است\n")
    @Column(name = "month")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer month;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Work id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getIssueDate() {
        return this.issueDate;
    }

    public Work issueDate(ZonedDateTime issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(ZonedDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getDesc() {
        return this.desc;
    }

    public Work desc(String desc) {
        this.setDesc(desc);
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getYear() {
        return this.year;
    }

    public Work year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return this.month;
    }

    public Work month(Integer month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public Work internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Work company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Work)) {
            return false;
        }
        return getId() != null && getId().equals(((Work) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Work{" +
            "id=" + getId() +
            ", issueDate='" + getIssueDate() + "'" +
            ", desc='" + getDesc() + "'" +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            "}";
    }
}
