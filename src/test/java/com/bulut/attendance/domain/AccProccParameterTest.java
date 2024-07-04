package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccProccParameterTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccProccParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccProccParameter.class);
        AccProccParameter accProccParameter1 = getAccProccParameterSample1();
        AccProccParameter accProccParameter2 = new AccProccParameter();
        assertThat(accProccParameter1).isNotEqualTo(accProccParameter2);

        accProccParameter2.setId(accProccParameter1.getId());
        assertThat(accProccParameter1).isEqualTo(accProccParameter2);

        accProccParameter2 = getAccProccParameterSample2();
        assertThat(accProccParameter1).isNotEqualTo(accProccParameter2);
    }

    @Test
    void internalUserTest() {
        AccProccParameter accProccParameter = getAccProccParameterRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        accProccParameter.setInternalUser(applicationUserBack);
        assertThat(accProccParameter.getInternalUser()).isEqualTo(applicationUserBack);

        accProccParameter.internalUser(null);
        assertThat(accProccParameter.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        AccProccParameter accProccParameter = getAccProccParameterRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        accProccParameter.setCompany(companyBack);
        assertThat(accProccParameter.getCompany()).isEqualTo(companyBack);

        accProccParameter.company(null);
        assertThat(accProccParameter.getCompany()).isNull();
    }
}
