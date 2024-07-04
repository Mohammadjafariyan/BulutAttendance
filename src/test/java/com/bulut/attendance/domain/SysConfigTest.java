package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static com.bulut.attendance.domain.SysConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysConfig.class);
        SysConfig sysConfig1 = getSysConfigSample1();
        SysConfig sysConfig2 = new SysConfig();
        assertThat(sysConfig1).isNotEqualTo(sysConfig2);

        sysConfig2.setId(sysConfig1.getId());
        assertThat(sysConfig1).isEqualTo(sysConfig2);

        sysConfig2 = getSysConfigSample2();
        assertThat(sysConfig1).isNotEqualTo(sysConfig2);
    }

    @Test
    void statusTest() {
        SysConfig sysConfig = getSysConfigRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        sysConfig.setStatus(recordStatusBack);
        assertThat(sysConfig.getStatus()).isEqualTo(recordStatusBack);

        sysConfig.status(null);
        assertThat(sysConfig.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        SysConfig sysConfig = getSysConfigRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        sysConfig.setInternalUser(applicationUserBack);
        assertThat(sysConfig.getInternalUser()).isEqualTo(applicationUserBack);

        sysConfig.internalUser(null);
        assertThat(sysConfig.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        SysConfig sysConfig = getSysConfigRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        sysConfig.setCompany(companyBack);
        assertThat(sysConfig.getCompany()).isEqualTo(companyBack);

        sysConfig.company(null);
        assertThat(sysConfig.getCompany()).isNull();
    }
}
