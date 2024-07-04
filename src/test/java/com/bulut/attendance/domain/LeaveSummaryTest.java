package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.LeaveSummaryTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveSummaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveSummary.class);
        LeaveSummary leaveSummary1 = getLeaveSummarySample1();
        LeaveSummary leaveSummary2 = new LeaveSummary();
        assertThat(leaveSummary1).isNotEqualTo(leaveSummary2);

        leaveSummary2.setId(leaveSummary1.getId());
        assertThat(leaveSummary1).isEqualTo(leaveSummary2);

        leaveSummary2 = getLeaveSummarySample2();
        assertThat(leaveSummary1).isNotEqualTo(leaveSummary2);
    }

    @Test
    void statusTest() {
        LeaveSummary leaveSummary = getLeaveSummaryRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        leaveSummary.setStatus(recordStatusBack);
        assertThat(leaveSummary.getStatus()).isEqualTo(recordStatusBack);

        leaveSummary.status(null);
        assertThat(leaveSummary.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        LeaveSummary leaveSummary = getLeaveSummaryRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        leaveSummary.setInternalUser(applicationUserBack);
        assertThat(leaveSummary.getInternalUser()).isEqualTo(applicationUserBack);

        leaveSummary.internalUser(null);
        assertThat(leaveSummary.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        LeaveSummary leaveSummary = getLeaveSummaryRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        leaveSummary.setCompany(companyBack);
        assertThat(leaveSummary.getCompany()).isEqualTo(companyBack);

        leaveSummary.company(null);
        assertThat(leaveSummary.getCompany()).isNull();
    }
}
