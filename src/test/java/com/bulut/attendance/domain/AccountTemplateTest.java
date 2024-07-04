package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccountTemplateTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountTemplate.class);
        AccountTemplate accountTemplate1 = getAccountTemplateSample1();
        AccountTemplate accountTemplate2 = new AccountTemplate();
        assertThat(accountTemplate1).isNotEqualTo(accountTemplate2);

        accountTemplate2.setId(accountTemplate1.getId());
        assertThat(accountTemplate1).isEqualTo(accountTemplate2);

        accountTemplate2 = getAccountTemplateSample2();
        assertThat(accountTemplate1).isNotEqualTo(accountTemplate2);
    }

    @Test
    void statusTest() {
        AccountTemplate accountTemplate = getAccountTemplateRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        accountTemplate.setStatus(recordStatusBack);
        assertThat(accountTemplate.getStatus()).isEqualTo(recordStatusBack);

        accountTemplate.status(null);
        assertThat(accountTemplate.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        AccountTemplate accountTemplate = getAccountTemplateRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        accountTemplate.setInternalUser(applicationUserBack);
        assertThat(accountTemplate.getInternalUser()).isEqualTo(applicationUserBack);

        accountTemplate.internalUser(null);
        assertThat(accountTemplate.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        AccountTemplate accountTemplate = getAccountTemplateRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        accountTemplate.setCompany(companyBack);
        assertThat(accountTemplate.getCompany()).isEqualTo(companyBack);

        accountTemplate.company(null);
        assertThat(accountTemplate.getCompany()).isNull();
    }
}
