package com.bulut.attendance.domain;

import com.bulut.attendance.domain.enumeration.CalcType;
import com.bulut.attendance.domain.enumeration.CalcUnit;
import com.bulut.attendance.domain.enumeration.Deduction;
import com.bulut.attendance.domain.enumeration.Earning;
import com.bulut.attendance.domain.enumeration.Hokm;
import com.bulut.attendance.domain.enumeration.LaborTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * پارامتر هر مرحله از عملیات حسابداری
 */
@Schema(description = "پارامتر هر مرحله از عملیات حسابداری")
@Entity
@Table(name = "acc_procc_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accproccparameter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccProccParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * عنوان
     */
    @Schema(description = "عنوان")
    @Column(name = "title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    /**
     * نحوه محاسبه دستی یا سیستمی
     */
    @Schema(description = "نحوه محاسبه دستی یا سیستمی")
    @Enumerated(EnumType.STRING)
    @Column(name = "manual_or_auto")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private CalcType manualOrAuto;

    /**
     * فرمول
     */
    @Schema(description = "فرمول")
    @Column(name = "formula")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String formula;

    /**
     * واحد روزانه یا ساعتی
     */
    @Schema(description = "واحد روزانه یا ساعتی")
    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private CalcUnit unit;

    /**
     * مشمول مالیات
     */
    @Schema(description = "مشمول مالیات")
    @Column(name = "is_deduc_tax")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeducTax;

    /**
     * مشمول بیمه
     */
    @Schema(description = "مشمول بیمه")
    @Column(name = "is_deduc_insurance")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeducInsurance;

    /**
     * نوع سیستمی پارامتر
     * عوامل کارکرد
     */
    @Schema(description = "نوع سیستمی پارامتر\nعوامل کارکرد")
    @Enumerated(EnumType.STRING)
    @Column(name = "labor_time")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private LaborTime laborTime;

    /**
     * نوع سیستمی پارامتر
     * عوامل حکمی
     */
    @Schema(description = "نوع سیستمی پارامتر\nعوامل حکمی")
    @Enumerated(EnumType.STRING)
    @Column(name = "hokm")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Hokm hokm;

    /**
     * نوع سیستمی پارامتر
     * عوامل حقوق و مزایا
     */
    @Schema(description = "نوع سیستمی پارامتر\nعوامل حقوق و مزایا")
    @Enumerated(EnumType.STRING)
    @Column(name = "earnings")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Earning earnings;

    /**
     * نوع سیستمی پارامتر
     * عوامل کسور
     */
    @Schema(description = "نوع سیستمی پارامتر\nعوامل کسور")
    @Enumerated(EnumType.STRING)
    @Column(name = "deduction")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Deduction deduction;

    /**
     * نوع سیستمی پارامتر
     * عوامل دیگر
     */
    @Schema(description = "نوع سیستمی پارامتر\nعوامل دیگر")
    @Enumerated(EnumType.STRING)
    @Column(name = "other")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Deduction other;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccProccParameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AccProccParameter title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CalcType getManualOrAuto() {
        return this.manualOrAuto;
    }

    public AccProccParameter manualOrAuto(CalcType manualOrAuto) {
        this.setManualOrAuto(manualOrAuto);
        return this;
    }

    public void setManualOrAuto(CalcType manualOrAuto) {
        this.manualOrAuto = manualOrAuto;
    }

    public String getFormula() {
        return this.formula;
    }

    public AccProccParameter formula(String formula) {
        this.setFormula(formula);
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public CalcUnit getUnit() {
        return this.unit;
    }

    public AccProccParameter unit(CalcUnit unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(CalcUnit unit) {
        this.unit = unit;
    }

    public Boolean getIsDeducTax() {
        return this.isDeducTax;
    }

    public AccProccParameter isDeducTax(Boolean isDeducTax) {
        this.setIsDeducTax(isDeducTax);
        return this;
    }

    public void setIsDeducTax(Boolean isDeducTax) {
        this.isDeducTax = isDeducTax;
    }

    public Boolean getIsDeducInsurance() {
        return this.isDeducInsurance;
    }

    public AccProccParameter isDeducInsurance(Boolean isDeducInsurance) {
        this.setIsDeducInsurance(isDeducInsurance);
        return this;
    }

    public void setIsDeducInsurance(Boolean isDeducInsurance) {
        this.isDeducInsurance = isDeducInsurance;
    }

    public LaborTime getLaborTime() {
        return this.laborTime;
    }

    public AccProccParameter laborTime(LaborTime laborTime) {
        this.setLaborTime(laborTime);
        return this;
    }

    public void setLaborTime(LaborTime laborTime) {
        this.laborTime = laborTime;
    }

    public Hokm getHokm() {
        return this.hokm;
    }

    public AccProccParameter hokm(Hokm hokm) {
        this.setHokm(hokm);
        return this;
    }

    public void setHokm(Hokm hokm) {
        this.hokm = hokm;
    }

    public Earning getEarnings() {
        return this.earnings;
    }

    public AccProccParameter earnings(Earning earnings) {
        this.setEarnings(earnings);
        return this;
    }

    public void setEarnings(Earning earnings) {
        this.earnings = earnings;
    }

    public Deduction getDeduction() {
        return this.deduction;
    }

    public AccProccParameter deduction(Deduction deduction) {
        this.setDeduction(deduction);
        return this;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    public Deduction getOther() {
        return this.other;
    }

    public AccProccParameter other(Deduction other) {
        this.setOther(other);
        return this;
    }

    public void setOther(Deduction other) {
        this.other = other;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public AccProccParameter internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AccProccParameter company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccProccParameter)) {
            return false;
        }
        return getId() != null && getId().equals(((AccProccParameter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccProccParameter{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", manualOrAuto='" + getManualOrAuto() + "'" +
            ", formula='" + getFormula() + "'" +
            ", unit='" + getUnit() + "'" +
            ", isDeducTax='" + getIsDeducTax() + "'" +
            ", isDeducInsurance='" + getIsDeducInsurance() + "'" +
            ", laborTime='" + getLaborTime() + "'" +
            ", hokm='" + getHokm() + "'" +
            ", earnings='" + getEarnings() + "'" +
            ", deduction='" + getDeduction() + "'" +
            ", other='" + getOther() + "'" +
            "}";
    }
}
