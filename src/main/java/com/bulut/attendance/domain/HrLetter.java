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
 * حکم پرسنلی
 * کلیه اطلاعات نحوه محاسبه حقوق پرسنل
 * شامل انتخاب کار ساعتی ، روزانه که قابلیت فعال و غیر فعال دارد
 */
@Schema(description = "حکم پرسنلی\nکلیه اطلاعات نحوه محاسبه حقوق پرسنل\nشامل انتخاب کار ساعتی ، روزانه که قابلیت فعال و غیر فعال دارد")
@Entity
@Table(name = "hr_letter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hrletter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HrLetter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * عنوان حکم
     *
     */
    @Schema(description = "عنوان حکم\n")
    @Column(name = "title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    /**
     * شماره حکم
     *
     */
    @Schema(description = "شماره حکم\n")
    @Column(name = "unique_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String uniqueNumber;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    /**
     * تاریخ صدور
     *
     */
    @Schema(description = "تاریخ صدور\n")
    @Column(name = "issue_date")
    private ZonedDateTime issueDate;

    /**
     * تاریخ اجرا
     *
     */
    @Schema(description = "تاریخ اجرا\n")
    @Column(name = "execution_date")
    private ZonedDateTime executionDate;

    @Column(name = "bpms_approve_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer bpmsApproveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecordStatus status;

    /**
     * نوع حکم
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company" }, allowSetters = true)
    private HrLetterType type;

    /**
     * پرسنل انتخاب شده
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company" }, allowSetters = true)
    private Personnel personnelId;

    /**
     * پست سازمانی
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company" }, allowSetters = true)
    private OrgPosition orgPosition;

    /**
     * واحد سازمانی
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "parent" }, allowSetters = true)
    private OrgUnit orgUnit;

    /**
     * جانباز و..
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company" }, allowSetters = true)
    private PersonnelStatus personnelStatus;

    /**
     * عوامل حکمی
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser", "company", "workItem" }, allowSetters = true)
    private HrLetterParameter hrLetterParameter;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser internalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "status", "internalUser" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getTitle() {
        return this.title;
    }

    public HrLetter title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUniqueNumber() {
        return this.uniqueNumber;
    }

    public HrLetter uniqueNumber(String uniqueNumber) {
        this.setUniqueNumber(uniqueNumber);
        return this;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public UUID getId() {
        return this.id;
    }

    public HrLetter id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getIssueDate() {
        return this.issueDate;
    }

    public HrLetter issueDate(ZonedDateTime issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(ZonedDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public ZonedDateTime getExecutionDate() {
        return this.executionDate;
    }

    public HrLetter executionDate(ZonedDateTime executionDate) {
        this.setExecutionDate(executionDate);
        return this;
    }

    public void setExecutionDate(ZonedDateTime executionDate) {
        this.executionDate = executionDate;
    }

    public Integer getBpmsApproveStatus() {
        return this.bpmsApproveStatus;
    }

    public HrLetter bpmsApproveStatus(Integer bpmsApproveStatus) {
        this.setBpmsApproveStatus(bpmsApproveStatus);
        return this;
    }

    public void setBpmsApproveStatus(Integer bpmsApproveStatus) {
        this.bpmsApproveStatus = bpmsApproveStatus;
    }

    public RecordStatus getStatus() {
        return this.status;
    }

    public void setStatus(RecordStatus recordStatus) {
        this.status = recordStatus;
    }

    public HrLetter status(RecordStatus recordStatus) {
        this.setStatus(recordStatus);
        return this;
    }

    public HrLetterType getType() {
        return this.type;
    }

    public void setType(HrLetterType hrLetterType) {
        this.type = hrLetterType;
    }

    public HrLetter type(HrLetterType hrLetterType) {
        this.setType(hrLetterType);
        return this;
    }

    public Personnel getPersonnelId() {
        return this.personnelId;
    }

    public void setPersonnelId(Personnel personnel) {
        this.personnelId = personnel;
    }

    public HrLetter personnelId(Personnel personnel) {
        this.setPersonnelId(personnel);
        return this;
    }

    public OrgPosition getOrgPosition() {
        return this.orgPosition;
    }

    public void setOrgPosition(OrgPosition orgPosition) {
        this.orgPosition = orgPosition;
    }

    public HrLetter orgPosition(OrgPosition orgPosition) {
        this.setOrgPosition(orgPosition);
        return this;
    }

    public OrgUnit getOrgUnit() {
        return this.orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public HrLetter orgUnit(OrgUnit orgUnit) {
        this.setOrgUnit(orgUnit);
        return this;
    }

    public PersonnelStatus getPersonnelStatus() {
        return this.personnelStatus;
    }

    public void setPersonnelStatus(PersonnelStatus personnelStatus) {
        this.personnelStatus = personnelStatus;
    }

    public HrLetter personnelStatus(PersonnelStatus personnelStatus) {
        this.setPersonnelStatus(personnelStatus);
        return this;
    }

    public HrLetterParameter getHrLetterParameter() {
        return this.hrLetterParameter;
    }

    public void setHrLetterParameter(HrLetterParameter hrLetterParameter) {
        this.hrLetterParameter = hrLetterParameter;
    }

    public HrLetter hrLetterParameter(HrLetterParameter hrLetterParameter) {
        this.setHrLetterParameter(hrLetterParameter);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public HrLetter internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public HrLetter company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HrLetter)) {
            return false;
        }
        return getId() != null && getId().equals(((HrLetter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HrLetter{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", uniqueNumber='" + getUniqueNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", executionDate='" + getExecutionDate() + "'" +
            ", bpmsApproveStatus=" + getBpmsApproveStatus() +
            "}";
    }
}
