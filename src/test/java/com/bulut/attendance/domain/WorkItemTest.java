package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.HrLetterParameterTestSamples.*;
import static com.bulut.attendance.domain.WorkItemTestSamples.*;
import static com.bulut.attendance.domain.WorkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkItem.class);
        WorkItem workItem1 = getWorkItemSample1();
        WorkItem workItem2 = new WorkItem();
        assertThat(workItem1).isNotEqualTo(workItem2);

        workItem2.setId(workItem1.getId());
        assertThat(workItem1).isEqualTo(workItem2);

        workItem2 = getWorkItemSample2();
        assertThat(workItem1).isNotEqualTo(workItem2);
    }

    @Test
    void hrLetterParameterTest() {
        WorkItem workItem = getWorkItemRandomSampleGenerator();
        HrLetterParameter hrLetterParameterBack = getHrLetterParameterRandomSampleGenerator();

        workItem.setHrLetterParameter(hrLetterParameterBack);
        assertThat(workItem.getHrLetterParameter()).isEqualTo(hrLetterParameterBack);

        workItem.hrLetterParameter(null);
        assertThat(workItem.getHrLetterParameter()).isNull();
    }

    @Test
    void internalUserTest() {
        WorkItem workItem = getWorkItemRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        workItem.setInternalUser(applicationUserBack);
        assertThat(workItem.getInternalUser()).isEqualTo(applicationUserBack);

        workItem.internalUser(null);
        assertThat(workItem.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        WorkItem workItem = getWorkItemRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        workItem.setCompany(companyBack);
        assertThat(workItem.getCompany()).isEqualTo(companyBack);

        workItem.company(null);
        assertThat(workItem.getCompany()).isNull();
    }

    @Test
    void workTest() {
        WorkItem workItem = getWorkItemRandomSampleGenerator();
        Work workBack = getWorkRandomSampleGenerator();

        workItem.setWork(workBack);
        assertThat(workItem.getWork()).isEqualTo(workBack);

        workItem.work(null);
        assertThat(workItem.getWork()).isNull();
    }
}
