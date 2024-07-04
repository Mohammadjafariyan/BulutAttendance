package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.HrLetterParameterTestSamples.*;
import static com.bulut.attendance.domain.HrLetterTestSamples.*;
import static com.bulut.attendance.domain.HrLetterTypeTestSamples.*;
import static com.bulut.attendance.domain.OrgPositionTestSamples.*;
import static com.bulut.attendance.domain.OrgUnitTestSamples.*;
import static com.bulut.attendance.domain.PersonnelStatusTestSamples.*;
import static com.bulut.attendance.domain.PersonnelTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HrLetterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HrLetter.class);
        HrLetter hrLetter1 = getHrLetterSample1();
        HrLetter hrLetter2 = new HrLetter();
        assertThat(hrLetter1).isNotEqualTo(hrLetter2);

        hrLetter2.setId(hrLetter1.getId());
        assertThat(hrLetter1).isEqualTo(hrLetter2);

        hrLetter2 = getHrLetterSample2();
        assertThat(hrLetter1).isNotEqualTo(hrLetter2);
    }

    @Test
    void statusTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        hrLetter.setStatus(recordStatusBack);
        assertThat(hrLetter.getStatus()).isEqualTo(recordStatusBack);

        hrLetter.status(null);
        assertThat(hrLetter.getStatus()).isNull();
    }

    @Test
    void typeTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        HrLetterType hrLetterTypeBack = getHrLetterTypeRandomSampleGenerator();

        hrLetter.setType(hrLetterTypeBack);
        assertThat(hrLetter.getType()).isEqualTo(hrLetterTypeBack);

        hrLetter.type(null);
        assertThat(hrLetter.getType()).isNull();
    }

    @Test
    void personnelIdTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        Personnel personnelBack = getPersonnelRandomSampleGenerator();

        hrLetter.setPersonnelId(personnelBack);
        assertThat(hrLetter.getPersonnelId()).isEqualTo(personnelBack);

        hrLetter.personnelId(null);
        assertThat(hrLetter.getPersonnelId()).isNull();
    }

    @Test
    void orgPositionTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        OrgPosition orgPositionBack = getOrgPositionRandomSampleGenerator();

        hrLetter.setOrgPosition(orgPositionBack);
        assertThat(hrLetter.getOrgPosition()).isEqualTo(orgPositionBack);

        hrLetter.orgPosition(null);
        assertThat(hrLetter.getOrgPosition()).isNull();
    }

    @Test
    void orgUnitTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        OrgUnit orgUnitBack = getOrgUnitRandomSampleGenerator();

        hrLetter.setOrgUnit(orgUnitBack);
        assertThat(hrLetter.getOrgUnit()).isEqualTo(orgUnitBack);

        hrLetter.orgUnit(null);
        assertThat(hrLetter.getOrgUnit()).isNull();
    }

    @Test
    void personnelStatusTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        PersonnelStatus personnelStatusBack = getPersonnelStatusRandomSampleGenerator();

        hrLetter.setPersonnelStatus(personnelStatusBack);
        assertThat(hrLetter.getPersonnelStatus()).isEqualTo(personnelStatusBack);

        hrLetter.personnelStatus(null);
        assertThat(hrLetter.getPersonnelStatus()).isNull();
    }

    @Test
    void hrLetterParameterTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        HrLetterParameter hrLetterParameterBack = getHrLetterParameterRandomSampleGenerator();

        hrLetter.setHrLetterParameter(hrLetterParameterBack);
        assertThat(hrLetter.getHrLetterParameter()).isEqualTo(hrLetterParameterBack);

        hrLetter.hrLetterParameter(null);
        assertThat(hrLetter.getHrLetterParameter()).isNull();
    }

    @Test
    void internalUserTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        hrLetter.setInternalUser(applicationUserBack);
        assertThat(hrLetter.getInternalUser()).isEqualTo(applicationUserBack);

        hrLetter.internalUser(null);
        assertThat(hrLetter.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        HrLetter hrLetter = getHrLetterRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        hrLetter.setCompany(companyBack);
        assertThat(hrLetter.getCompany()).isEqualTo(companyBack);

        hrLetter.company(null);
        assertThat(hrLetter.getCompany()).isNull();
    }
}
