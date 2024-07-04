package com.bulut.attendance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personnel.
 */
@Entity
@Table(name = "personnel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "personnel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "first_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String firstName;

    @Column(name = "last_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastName;

    @Column(name = "requitment_date")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String requitmentDate;

    @Column(name = "father")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String father;

    @Column(name = "shenasname")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String shenasname;

    @Column(name = "mahalesodur")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mahalesodur;

    @Column(name = "birthday")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String birthday;

    @Column(name = "is_single")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String isSingle;

    @Column(name = "last_education")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastEducation;

    @Column(name = "education_field")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String educationField;

    @Min(value = 42)
    @Max(value = 42)
    @Column(name = "children")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer children;

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

    public String getFirstName() {
        return this.firstName;
    }

    public Personnel firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Personnel lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRequitmentDate() {
        return this.requitmentDate;
    }

    public Personnel requitmentDate(String requitmentDate) {
        this.setRequitmentDate(requitmentDate);
        return this;
    }

    public void setRequitmentDate(String requitmentDate) {
        this.requitmentDate = requitmentDate;
    }

    public String getFather() {
        return this.father;
    }

    public Personnel father(String father) {
        this.setFather(father);
        return this;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getShenasname() {
        return this.shenasname;
    }

    public Personnel shenasname(String shenasname) {
        this.setShenasname(shenasname);
        return this;
    }

    public void setShenasname(String shenasname) {
        this.shenasname = shenasname;
    }

    public String getMahalesodur() {
        return this.mahalesodur;
    }

    public Personnel mahalesodur(String mahalesodur) {
        this.setMahalesodur(mahalesodur);
        return this;
    }

    public void setMahalesodur(String mahalesodur) {
        this.mahalesodur = mahalesodur;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public Personnel birthday(String birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIsSingle() {
        return this.isSingle;
    }

    public Personnel isSingle(String isSingle) {
        this.setIsSingle(isSingle);
        return this;
    }

    public void setIsSingle(String isSingle) {
        this.isSingle = isSingle;
    }

    public String getLastEducation() {
        return this.lastEducation;
    }

    public Personnel lastEducation(String lastEducation) {
        this.setLastEducation(lastEducation);
        return this;
    }

    public void setLastEducation(String lastEducation) {
        this.lastEducation = lastEducation;
    }

    public String getEducationField() {
        return this.educationField;
    }

    public Personnel educationField(String educationField) {
        this.setEducationField(educationField);
        return this;
    }

    public void setEducationField(String educationField) {
        this.educationField = educationField;
    }

    public Integer getChildren() {
        return this.children;
    }

    public Personnel children(Integer children) {
        this.setChildren(children);
        return this;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public UUID getId() {
        return this.id;
    }

    public Personnel id(UUID id) {
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

    public Personnel status(RecordStatus recordStatus) {
        this.setStatus(recordStatus);
        return this;
    }

    public ApplicationUser getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(ApplicationUser applicationUser) {
        this.internalUser = applicationUser;
    }

    public Personnel internalUser(ApplicationUser applicationUser) {
        this.setInternalUser(applicationUser);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Personnel company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personnel)) {
            return false;
        }
        return getId() != null && getId().equals(((Personnel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personnel{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", requitmentDate='" + getRequitmentDate() + "'" +
            ", father='" + getFather() + "'" +
            ", shenasname='" + getShenasname() + "'" +
            ", mahalesodur='" + getMahalesodur() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", isSingle='" + getIsSingle() + "'" +
            ", lastEducation='" + getLastEducation() + "'" +
            ", educationField='" + getEducationField() + "'" +
            ", children=" + getChildren() +
            "}";
    }
}
