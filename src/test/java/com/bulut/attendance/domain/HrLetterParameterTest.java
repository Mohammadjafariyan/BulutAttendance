package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.HrLetterParameterTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static com.bulut.attendance.domain.WorkItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HrLetterParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HrLetterParameter.class);
        HrLetterParameter hrLetterParameter1 = getHrLetterParameterSample1();
        HrLetterParameter hrLetterParameter2 = new HrLetterParameter();
        assertThat(hrLetterParameter1).isNotEqualTo(hrLetterParameter2);

        hrLetterParameter2.setId(hrLetterParameter1.getId());
        assertThat(hrLetterParameter1).isEqualTo(hrLetterParameter2);

        hrLetterParameter2 = getHrLetterParameterSample2();
        assertThat(hrLetterParameter1).isNotEqualTo(hrLetterParameter2);
    }

    @Test
    void statusTest() {
        HrLetterParameter hrLetterParameter = getHrLetterParameterRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        hrLetterParameter.setStatus(recordStatusBack);
        assertThat(hrLetterParameter.getStatus()).isEqualTo(recordStatusBack);

        hrLetterParameter.status(null);
        assertThat(hrLetterParameter.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        HrLetterParameter hrLetterParameter = getHrLetterParameterRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        hrLetterParameter.setInternalUser(applicationUserBack);
        assertThat(hrLetterParameter.getInternalUser()).isEqualTo(applicationUserBack);

        hrLetterParameter.internalUser(null);
        assertThat(hrLetterParameter.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        HrLetterParameter hrLetterParameter = getHrLetterParameterRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        hrLetterParameter.setCompany(companyBack);
        assertThat(hrLetterParameter.getCompany()).isEqualTo(companyBack);

        hrLetterParameter.company(null);
        assertThat(hrLetterParameter.getCompany()).isNull();
    }

    @Test
    void workItemTest() {
        HrLetterParameter hrLetterParameter = getHrLetterParameterRandomSampleGenerator();
        WorkItem workItemBack = getWorkItemRandomSampleGenerator();

        hrLetterParameter.setWorkItem(workItemBack);
        assertThat(hrLetterParameter.getWorkItem()).isEqualTo(workItemBack);
        assertThat(workItemBack.getHrLetterParameter()).isEqualTo(hrLetterParameter);

        hrLetterParameter.workItem(null);
        assertThat(hrLetterParameter.getWorkItem()).isNull();
        assertThat(workItemBack.getHrLetterParameter()).isNull();
    }
}
