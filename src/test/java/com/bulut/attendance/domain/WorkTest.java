package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.WorkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Work.class);
        Work work1 = getWorkSample1();
        Work work2 = new Work();
        assertThat(work1).isNotEqualTo(work2);

        work2.setId(work1.getId());
        assertThat(work1).isEqualTo(work2);

        work2 = getWorkSample2();
        assertThat(work1).isNotEqualTo(work2);
    }

    @Test
    void internalUserTest() {
        Work work = getWorkRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        work.setInternalUser(applicationUserBack);
        assertThat(work.getInternalUser()).isEqualTo(applicationUserBack);

        work.internalUser(null);
        assertThat(work.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        Work work = getWorkRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        work.setCompany(companyBack);
        assertThat(work.getCompany()).isEqualTo(companyBack);

        work.company(null);
        assertThat(work.getCompany()).isNull();
    }
}
