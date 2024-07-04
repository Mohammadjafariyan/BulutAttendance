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
 * جزئیات مقدار هر عامل حکمی در این جدول ثبت مشود
 * شامل کارکرد ، حقوق مزایا ، کسورات ، بیمه و غیره
 */
@Schema(description = "جزئیات مقدار هر عامل حکمی در این جدول ثبت مشود\nشامل کارکرد ، حقوق مزایا ، کسورات ، بیمه و غیره")
@Entity
@Table(name = "work_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workitem")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    /**
     * این مقدار برای کدام پارامتر از حکم محاسبه شده است
     *
     */
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "workItem" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private HrLetterParameter hrLetterParameter;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    /**
     * کار یک ماه را نشان میدهد و ایتم جزئیات کارکرد آن ماه برای پرسنل
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "company" }, allowSetters = true)
    private Work work;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public WorkItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public WorkItem amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public HrLetterParameter getHrLetterParameter() {
        return this.hrLetterParameter;
    }

    public void setHrLetterParameter(HrLetterParameter hrLetterParameter) {
        this.hrLetterParameter = hrLetterParameter;
    }

    public WorkItem hrLetterParameter(HrLetterParameter hrLetterParameter) {
        this.setHrLetterParameter(hrLetterParameter);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public WorkItem internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public WorkItem company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Work getWork() {
        return this.work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public WorkItem work(Work work) {
        this.setWork(work);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkItem)) {
            return false;
        }
        return getId() != null && getId().equals(((WorkItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkItem{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
