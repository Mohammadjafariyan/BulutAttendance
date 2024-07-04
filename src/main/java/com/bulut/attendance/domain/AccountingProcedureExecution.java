package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ثبت لاگ اجرای عملیات حسابداری
 *
 */
@Schema(description = "ثبت لاگ اجرای عملیات حسابداری\n")
@Entity
@Table(name = "accounting_procedure_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accountingprocedureexecution")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountingProcedureExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * تاریخ اجرا
     *
     */
    @Schema(description = "تاریخ اجرا\n")
    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    /**
     * توضیحات
     *
     */
    @Schema(description = "توضیحات\n")
    @Column(name = "jhi_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    /**
     * اجرا های عملیات حسابداری
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "executeAfter" }, allowSetters = true)
    private AccountingProcedure procedure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountingProcedureExecution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return this.dateTime;
    }

    public AccountingProcedureExecution dateTime(ZonedDateTime dateTime) {
        this.setDateTime(dateTime);
        return this;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDesc() {
        return this.desc;
    }

    public AccountingProcedureExecution desc(String desc) {
        this.setDesc(desc);
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public AccountingProcedureExecution internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AccountingProcedureExecution company(Company company) {
        this.setCompany(company);
        return this;
    }

    public AccountingProcedure getProcedure() {
        return this.procedure;
    }

    public void setProcedure(AccountingProcedure accountingProcedure) {
        this.procedure = accountingProcedure;
    }

    public AccountingProcedureExecution procedure(AccountingProcedure accountingProcedure) {
        this.setProcedure(accountingProcedure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountingProcedureExecution)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountingProcedureExecution) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountingProcedureExecution{" +
            "id=" + getId() +
            ", dateTime='" + getDateTime() + "'" +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
