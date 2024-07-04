package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.ParameterTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parameter.class);
        Parameter parameter1 = getParameterSample1();
        Parameter parameter2 = new Parameter();
        assertThat(parameter1).isNotEqualTo(parameter2);

        parameter2.setId(parameter1.getId());
        assertThat(parameter1).isEqualTo(parameter2);

        parameter2 = getParameterSample2();
        assertThat(parameter1).isNotEqualTo(parameter2);
    }

    @Test
    void statusTest() {
        Parameter parameter = getParameterRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        parameter.setStatus(recordStatusBack);
        assertThat(parameter.getStatus()).isEqualTo(recordStatusBack);

        parameter.status(null);
        assertThat(parameter.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        Parameter parameter = getParameterRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        parameter.setInternalUser(applicationUserBack);
        assertThat(parameter.getInternalUser()).isEqualTo(applicationUserBack);

        parameter.internalUser(null);
        assertThat(parameter.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        Parameter parameter = getParameterRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        parameter.setCompany(companyBack);
        assertThat(parameter.getCompany()).isEqualTo(companyBack);

        parameter.company(null);
        assertThat(parameter.getCompany()).isNull();
    }
}
