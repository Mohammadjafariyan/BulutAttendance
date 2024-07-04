package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.OrgPositionTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgPositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgPosition.class);
        OrgPosition orgPosition1 = getOrgPositionSample1();
        OrgPosition orgPosition2 = new OrgPosition();
        assertThat(orgPosition1).isNotEqualTo(orgPosition2);

        orgPosition2.setId(orgPosition1.getId());
        assertThat(orgPosition1).isEqualTo(orgPosition2);

        orgPosition2 = getOrgPositionSample2();
        assertThat(orgPosition1).isNotEqualTo(orgPosition2);
    }

    @Test
    void statusTest() {
        OrgPosition orgPosition = getOrgPositionRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        orgPosition.setStatus(recordStatusBack);
        assertThat(orgPosition.getStatus()).isEqualTo(recordStatusBack);

        orgPosition.status(null);
        assertThat(orgPosition.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        OrgPosition orgPosition = getOrgPositionRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        orgPosition.setInternalUser(applicationUserBack);
        assertThat(orgPosition.getInternalUser()).isEqualTo(applicationUserBack);

        orgPosition.internalUser(null);
        assertThat(orgPosition.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        OrgPosition orgPosition = getOrgPositionRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        orgPosition.setCompany(companyBack);
        assertThat(orgPosition.getCompany()).isEqualTo(companyBack);

        orgPosition.company(null);
        assertThat(orgPosition.getCompany()).isNull();
    }
}
