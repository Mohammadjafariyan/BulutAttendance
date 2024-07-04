package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.LeaveTestSamples.*;
import static com.bulut.attendance.domain.PersonnelTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Leave.class);
        Leave leave1 = getLeaveSample1();
        Leave leave2 = new Leave();
        assertThat(leave1).isNotEqualTo(leave2);

        leave2.setId(leave1.getId());
        assertThat(leave1).isEqualTo(leave2);

        leave2 = getLeaveSample2();
        assertThat(leave1).isNotEqualTo(leave2);
    }

    @Test
    void statusTest() {
        Leave leave = getLeaveRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        leave.setStatus(recordStatusBack);
        assertThat(leave.getStatus()).isEqualTo(recordStatusBack);

        leave.status(null);
        assertThat(leave.getStatus()).isNull();
    }

    @Test
    void personnelIdTest() {
        Leave leave = getLeaveRandomSampleGenerator();
        Personnel personnelBack = getPersonnelRandomSampleGenerator();

        leave.setPersonnelId(personnelBack);
        assertThat(leave.getPersonnelId()).isEqualTo(personnelBack);

        leave.personnelId(null);
        assertThat(leave.getPersonnelId()).isNull();
    }

    @Test
    void internalUserTest() {
        Leave leave = getLeaveRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        leave.setInternalUser(applicationUserBack);
        assertThat(leave.getInternalUser()).isEqualTo(applicationUserBack);

        leave.internalUser(null);
        assertThat(leave.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        Leave leave = getLeaveRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        leave.setCompany(companyBack);
        assertThat(leave.getCompany()).isEqualTo(companyBack);

        leave.company(null);
        assertThat(leave.getCompany()).isNull();
    }
}
