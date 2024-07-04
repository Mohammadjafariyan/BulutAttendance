package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * لاگ اجرای عملیات حسابداری
 *
 */
@Schema(description = "لاگ اجرای عملیات حسابداری\n")
@Entity
@Table(name = "acc_proc_step_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accprocstepexecution")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccProcStepExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "debit_amount", precision = 21, scale = 2)
    private BigDecimal debitAmount;

    @Column(name = "credit_amount", precision = 21, scale = 2)
    private BigDecimal creditAmount;

    @Column(name = "jhi_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    /**
     * لاگ اجرا
     * حسابی که بستانکار شد
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "parentAccountId", "personnelId", "bank" }, allowSetters = true)
    private AccountHesab creditAccount;

    /**
     * حسابی که بدهکار شد
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "parentAccountId", "personnelId", "bank" }, allowSetters = true)
    private AccountHesab debitAccount;

    /**
     * مربوط به اجرای کدام عملیات حسابداری است
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "company", "procedure" }, allowSetters = true)
    private AccountingProcedureExecution procedure;

    /**
     * مربوط به اجرای کدام مرحله از عملیات حسابداری است
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "internalUser", "company", "creditAccount", "debitAccount", "parameter", "procedure" },
        allowSetters = true
    )
    private AccProcStep step;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccProcStepExecution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDebitAmount() {
        return this.debitAmount;
    }

    public AccProcStepExecution debitAmount(BigDecimal debitAmount) {
        this.setDebitAmount(debitAmount);
        return this;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return this.creditAmount;
    }

    public AccProcStepExecution creditAmount(BigDecimal creditAmount) {
        this.setCreditAmount(creditAmount);
        return this;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getDesc() {
        return this.desc;
    }

    public AccProcStepExecution desc(String desc) {
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

    public AccProcStepExecution internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AccProcStepExecution company(Company company) {
        this.setCompany(company);
        return this;
    }

    public AccountHesab getCreditAccount() {
        return this.creditAccount;
    }

    public void setCreditAccount(AccountHesab accountHesab) {
        this.creditAccount = accountHesab;
    }

    public AccProcStepExecution creditAccount(AccountHesab accountHesab) {
        this.setCreditAccount(accountHesab);
        return this;
    }

    public AccountHesab getDebitAccount() {
        return this.debitAccount;
    }

    public void setDebitAccount(AccountHesab accountHesab) {
        this.debitAccount = accountHesab;
    }

    public AccProcStepExecution debitAccount(AccountHesab accountHesab) {
        this.setDebitAccount(accountHesab);
        return this;
    }

    public AccountingProcedureExecution getProcedure() {
        return this.procedure;
    }

    public void setProcedure(AccountingProcedureExecution accountingProcedureExecution) {
        this.procedure = accountingProcedureExecution;
    }

    public AccProcStepExecution procedure(AccountingProcedureExecution accountingProcedureExecution) {
        this.setProcedure(accountingProcedureExecution);
        return this;
    }

    public AccProcStep getStep() {
        return this.step;
    }

    public void setStep(AccProcStep accProcStep) {
        this.step = accProcStep;
    }

    public AccProcStepExecution step(AccProcStep accProcStep) {
        this.setStep(accProcStep);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccProcStepExecution)) {
            return false;
        }
        return getId() != null && getId().equals(((AccProcStepExecution) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccProcStepExecution{" +
            "id=" + getId() +
            ", debitAmount=" + getDebitAmount() +
            ", creditAmount=" + getCreditAmount() +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
