package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * هر مرحله از عملیات حسابداری
 */
@Schema(description = "هر مرحله از عملیات حسابداری")
@Entity
@Table(name = "acc_proc_step")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accprocstep")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccProcStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    /**
     * حسابی که بستانکار خواهد شد
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "parentAccountId", "personnelId", "bank" }, allowSetters = true)
    private AccountHesab creditAccount;

    /**
     * حسابی که بدهکار خواهد شد
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "parentAccountId", "personnelId", "bank" }, allowSetters = true)
    private AccountHesab debitAccount;

    /**
     * پارامتری که محاسبه شده و مبلغ آن وارد خواهد شد
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "company" }, allowSetters = true)
    private AccProccParameter parameter;

    /**
     * مربوط به کدام عملیات حسابداری است
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "executeAfter" }, allowSetters = true)
    private AccountingProcedure procedure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccProcStep id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public AccProcStep internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AccProcStep company(Company company) {
        this.setCompany(company);
        return this;
    }

    public AccountHesab getCreditAccount() {
        return this.creditAccount;
    }

    public void setCreditAccount(AccountHesab accountHesab) {
        this.creditAccount = accountHesab;
    }

    public AccProcStep creditAccount(AccountHesab accountHesab) {
        this.setCreditAccount(accountHesab);
        return this;
    }

    public AccountHesab getDebitAccount() {
        return this.debitAccount;
    }

    public void setDebitAccount(AccountHesab accountHesab) {
        this.debitAccount = accountHesab;
    }

    public AccProcStep debitAccount(AccountHesab accountHesab) {
        this.setDebitAccount(accountHesab);
        return this;
    }

    public AccProccParameter getParameter() {
        return this.parameter;
    }

    public void setParameter(AccProccParameter accProccParameter) {
        this.parameter = accProccParameter;
    }

    public AccProcStep parameter(AccProccParameter accProccParameter) {
        this.setParameter(accProccParameter);
        return this;
    }

    public AccountingProcedure getProcedure() {
        return this.procedure;
    }

    public void setProcedure(AccountingProcedure accountingProcedure) {
        this.procedure = accountingProcedure;
    }

    public AccProcStep procedure(AccountingProcedure accountingProcedure) {
        this.setProcedure(accountingProcedure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccProcStep)) {
            return false;
        }
        return getId() != null && getId().equals(((AccProcStep) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccProcStep{" +
            "id=" + getId() +
            "}";
    }
}
