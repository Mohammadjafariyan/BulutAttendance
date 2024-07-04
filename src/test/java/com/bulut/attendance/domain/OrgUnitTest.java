package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.OrgUnitTestSamples.*;
import static com.bulut.attendance.domain.OrgUnitTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUnit.class);
        OrgUnit orgUnit1 = getOrgUnitSample1();
        OrgUnit orgUnit2 = new OrgUnit();
        assertThat(orgUnit1).isNotEqualTo(orgUnit2);

        orgUnit2.setId(orgUnit1.getId());
        assertThat(orgUnit1).isEqualTo(orgUnit2);

        orgUnit2 = getOrgUnitSample2();
        assertThat(orgUnit1).isNotEqualTo(orgUnit2);
    }

    @Test
    void statusTest() {
        OrgUnit orgUnit = getOrgUnitRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        orgUnit.setStatus(recordStatusBack);
        assertThat(orgUnit.getStatus()).isEqualTo(recordStatusBack);

        orgUnit.status(null);
        assertThat(orgUnit.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        OrgUnit orgUnit = getOrgUnitRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        orgUnit.setInternalUser(applicationUserBack);
        assertThat(orgUnit.getInternalUser()).isEqualTo(applicationUserBack);

        orgUnit.internalUser(null);
        assertThat(orgUnit.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        OrgUnit orgUnit = getOrgUnitRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        orgUnit.setCompany(companyBack);
        assertThat(orgUnit.getCompany()).isEqualTo(companyBack);

        orgUnit.company(null);
        assertThat(orgUnit.getCompany()).isNull();
    }

    @Test
    void parentTest() {
        OrgUnit orgUnit = getOrgUnitRandomSampleGenerator();
        OrgUnit orgUnitBack = getOrgUnitRandomSampleGenerator();

        orgUnit.setParent(orgUnitBack);
        assertThat(orgUnit.getParent()).isEqualTo(orgUnitBack);

        orgUnit.parent(null);
        assertThat(orgUnit.getParent()).isNull();
    }
}
