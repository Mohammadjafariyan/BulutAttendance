package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * سند
 */
@Schema(description = "سند")
@Entity
@Table(name = "jhi_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * تاریخ
     */
    @Schema(description = "تاریخ")
    @Column(name = "issue_date_time")
    private ZonedDateTime issueDateTime;

    /**
     * مبلغ کل
     */
    @Schema(description = "مبلغ کل")
    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

    /**
     * توضیحات
     */
    @Schema(description = "توضیحات")
    @Column(name = "jhi_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String desc;

    /**
     * شماره سند
     */
    @Schema(description = "شماره سند")
    @Column(name = "doc_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String docNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getIssueDateTime() {
        return this.issueDateTime;
    }

    public Transaction issueDateTime(ZonedDateTime issueDateTime) {
        this.setIssueDateTime(issueDateTime);
        return this;
    }

    public void setIssueDateTime(ZonedDateTime issueDateTime) {
        this.issueDateTime = issueDateTime;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Transaction totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDesc() {
        return this.desc;
    }

    public Transaction desc(String desc) {
        this.setDesc(desc);
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDocNumber() {
        return this.docNumber;
    }

    public Transaction docNumber(String docNumber) {
        this.setDocNumber(docNumber);
        return this;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public Transaction internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Transaction company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Transaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", issueDateTime='" + getIssueDateTime() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", desc='" + getDesc() + "'" +
            ", docNumber='" + getDocNumber() + "'" +
            "}";
    }
}
