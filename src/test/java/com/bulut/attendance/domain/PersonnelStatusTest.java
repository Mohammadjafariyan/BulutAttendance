package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.PersonnelStatusTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonnelStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonnelStatus.class);
        PersonnelStatus personnelStatus1 = getPersonnelStatusSample1();
        PersonnelStatus personnelStatus2 = new PersonnelStatus();
        assertThat(personnelStatus1).isNotEqualTo(personnelStatus2);

        personnelStatus2.setId(personnelStatus1.getId());
        assertThat(personnelStatus1).isEqualTo(personnelStatus2);

        personnelStatus2 = getPersonnelStatusSample2();
        assertThat(personnelStatus1).isNotEqualTo(personnelStatus2);
    }

    @Test
    void statusTest() {
        PersonnelStatus personnelStatus = getPersonnelStatusRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        personnelStatus.setStatus(recordStatusBack);
        assertThat(personnelStatus.getStatus()).isEqualTo(recordStatusBack);

        personnelStatus.status(null);
        assertThat(personnelStatus.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        PersonnelStatus personnelStatus = getPersonnelStatusRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        personnelStatus.setInternalUser(applicationUserBack);
        assertThat(personnelStatus.getInternalUser()).isEqualTo(applicationUserBack);

        personnelStatus.internalUser(null);
        assertThat(personnelStatus.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        PersonnelStatus personnelStatus = getPersonnelStatusRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        personnelStatus.setCompany(companyBack);
        assertThat(personnelStatus.getCompany()).isEqualTo(companyBack);

        personnelStatus.company(null);
        assertThat(personnelStatus.getCompany()).isNull();
    }
}
