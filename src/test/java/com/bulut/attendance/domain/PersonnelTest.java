package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.PersonnelTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonnelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personnel.class);
        Personnel personnel1 = getPersonnelSample1();
        Personnel personnel2 = new Personnel();
        assertThat(personnel1).isNotEqualTo(personnel2);

        personnel2.setId(personnel1.getId());
        assertThat(personnel1).isEqualTo(personnel2);

        personnel2 = getPersonnelSample2();
        assertThat(personnel1).isNotEqualTo(personnel2);
    }

    @Test
    void statusTest() {
        Personnel personnel = getPersonnelRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        personnel.setStatus(recordStatusBack);
        assertThat(personnel.getStatus()).isEqualTo(recordStatusBack);

        personnel.status(null);
        assertThat(personnel.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        Personnel personnel = getPersonnelRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        personnel.setInternalUser(applicationUserBack);
        assertThat(personnel.getInternalUser()).isEqualTo(applicationUserBack);

        personnel.internalUser(null);
        assertThat(personnel.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        Personnel personnel = getPersonnelRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        personnel.setCompany(companyBack);
        assertThat(personnel.getCompany()).isEqualTo(companyBack);

        personnel.company(null);
        assertThat(personnel.getCompany()).isNull();
    }
}
