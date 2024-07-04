package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void statusTest() {
        Company company = getCompanyRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        company.setStatus(recordStatusBack);
        assertThat(company.getStatus()).isEqualTo(recordStatusBack);

        company.status(null);
        assertThat(company.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        Company company = getCompanyRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        company.setInternalUser(applicationUserBack);
        assertThat(company.getInternalUser()).isEqualTo(applicationUserBack);

        company.internalUser(null);
        assertThat(company.getInternalUser()).isNull();
    }
}
