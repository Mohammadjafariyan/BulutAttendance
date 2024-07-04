package com.bulut.attendance.domain;

import com.bulut.attendance.domain.enumeration.AccountLevelInTree;
import com.bulut.attendance.domain.enumeration.AccountType;
import com.bulut.attendance.domain.enumeration.AccountingFormulaType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * حساب هایی که مال سیستم هستند و موقع ثبت شرکت به حساب های شرکت
 * کپی می شوند
 */
@Schema(description = "حساب هایی که مال سیستم هستند و موقع ثبت شرکت به حساب های شرکت\nکپی می شوند")
@Entity
@Table(name = "account_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "accounttemplate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * نام حساب
     */
    @Schema(description = "نام حساب")
    @Column(name = "title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    /**
     * کد حساب
     */
    @Schema(description = "کد حساب")
    @Column(name = "code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String code;

    /**
     * سطح حساب
     */
    @Schema(description = "سطح حساب")
    @Column(name = "level")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer level;

    /**
     * عنوان سطح حساب
     */
    @Schema(description = "عنوان سطح حساب")
    @Column(name = "level_title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String levelTitle;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    /**
     * ماهیت حساب : بدهکار یا بستانکار
     */
    @Schema(description = "ماهیت حساب : بدهکار یا بستانکار")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AccountType type;

    /**
     * سطح حساب داری : کل ، معین ، تفظیل
     */
    @Schema(description = "سطح حساب داری : کل ، معین ، تفظیل")
    @Enumerated(EnumType.STRING)
    @Column(name = "level_in_tree")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AccountLevelInTree levelInTree;

    /**
     * مانده بدهکار
     */
    @Schema(description = "مانده بدهکار")
    @Column(name = "debit_amount", precision = 21, scale = 2)
    private BigDecimal debitAmount;

    /**
     * مانده بستانکار
     */
    @Schema(description = "مانده بستانکار")
    @Column(name = "credit_amount", precision = 21, scale = 2)
    private BigDecimal creditAmount;

    /**
     * تاثیر در فرمول معادله حسابداری
     */
    @Schema(description = "تاثیر در فرمول معادله حسابداری")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_in_formula")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AccountingFormulaType typeInFormula;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecordStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getTitle() {
        return this.title;
    }

    public AccountTemplate title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return this.code;
    }

    public AccountTemplate code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLevel() {
        return this.level;
    }

    public AccountTemplate level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelTitle() {
        return this.levelTitle;
    }

    public AccountTemplate levelTitle(String levelTitle) {
        this.setLevelTitle(levelTitle);
        return this;
    }

    public void setLevelTitle(String levelTitle) {
        this.levelTitle = levelTitle;
    }

    public UUID getId() {
        return this.id;
    }

    public AccountTemplate id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AccountType getType() {
        return this.type;
    }

    public AccountTemplate type(AccountType type) {
        this.setType(type);
        return this;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public AccountLevelInTree getLevelInTree() {
        return this.levelInTree;
    }

    public AccountTemplate levelInTree(AccountLevelInTree levelInTree) {
        this.setLevelInTree(levelInTree);
        return this;
    }

    public void setLevelInTree(AccountLevelInTree levelInTree) {
        this.levelInTree = levelInTree;
    }

    public BigDecimal getDebitAmount() {
        return this.debitAmount;
    }

    public AccountTemplate debitAmount(BigDecimal debitAmount) {
        this.setDebitAmount(debitAmount);
        return this;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return this.creditAmount;
    }

    public AccountTemplate creditAmount(BigDecimal creditAmount) {
        this.setCreditAmount(creditAmount);
        return this;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public AccountingFormulaType getTypeInFormula() {
        return this.typeInFormula;
    }

    public AccountTemplate typeInFormula(AccountingFormulaType typeInFormula) {
        this.setTypeInFormula(typeInFormula);
        return this;
    }

    public void setTypeInFormula(AccountingFormulaType typeInFormula) {
        this.typeInFormula = typeInFormula;
    }

    public RecordStatus getStatus() {
        return this.status;
    }

    public void setStatus(RecordStatus recordStatus) {
        this.status = recordStatus;
    }

    public AccountTemplate status(RecordStatus recordStatus) {
        this.setStatus(recordStatus);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public AccountTemplate internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AccountTemplate company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountTemplate{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", code='" + getCode() + "'" +
            ", level=" + getLevel() +
            ", levelTitle='" + getLevelTitle() + "'" +
            ", type='" + getType() + "'" +
            ", levelInTree='" + getLevelInTree() + "'" +
            ", debitAmount=" + getDebitAmount() +
            ", creditAmount=" + getCreditAmount() +
            ", typeInFormula='" + getTypeInFormula() + "'" +
            "}";
    }
}
