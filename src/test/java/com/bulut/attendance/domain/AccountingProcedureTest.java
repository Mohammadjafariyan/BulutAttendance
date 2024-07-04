package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccountingProcedureTestSamples.*;
import static com.bulut.attendance.domain.AccountingProcedureTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountingProcedureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingProcedure.class);
        AccountingProcedure accountingProcedure1 = getAccountingProcedureSample1();
        AccountingProcedure accountingProcedure2 = new AccountingProcedure();
        assertThat(accountingProcedure1).isNotEqualTo(accountingProcedure2);

        accountingProcedure2.setId(accountingProcedure1.getId());
        assertThat(accountingProcedure1).isEqualTo(accountingProcedure2);

        accountingProcedure2 = getAccountingProcedureSample2();
        assertThat(accountingProcedure1).isNotEqualTo(accountingProcedure2);
    }

    @Test
    void statusTest() {
        AccountingProcedure accountingProcedure = getAccountingProcedureRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        accountingProcedure.setStatus(recordStatusBack);
        assertThat(accountingProcedure.getStatus()).isEqualTo(recordStatusBack);

        accountingProcedure.status(null);
        assertThat(accountingProcedure.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        AccountingProcedure accountingProcedure = getAccountingProcedureRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        accountingProcedure.setInternalUser(applicationUserBack);
        assertThat(accountingProcedure.getInternalUser()).isEqualTo(applicationUserBack);

        accountingProcedure.internalUser(null);
        assertThat(accountingProcedure.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        AccountingProcedure accountingProcedure = getAccountingProcedureRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        accountingProcedure.setCompany(companyBack);
        assertThat(accountingProcedure.getCompany()).isEqualTo(companyBack);

        accountingProcedure.company(null);
        assertThat(accountingProcedure.getCompany()).isNull();
    }

    @Test
    void executeAfterTest() {
        AccountingProcedure accountingProcedure = getAccountingProcedureRandomSampleGenerator();
        AccountingProcedure accountingProcedureBack = getAccountingProcedureRandomSampleGenerator();

        accountingProcedure.setExecuteAfter(accountingProcedureBack);
        assertThat(accountingProcedure.getExecuteAfter()).isEqualTo(accountingProcedureBack);

        accountingProcedure.executeAfter(null);
        assertThat(accountingProcedure.getExecuteAfter()).isNull();
    }
}
