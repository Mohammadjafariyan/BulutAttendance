package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccountingProcedureExecutionTestSamples.*;
import static com.bulut.attendance.domain.AccountingProcedureTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountingProcedureExecutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingProcedureExecution.class);
        AccountingProcedureExecution accountingProcedureExecution1 = getAccountingProcedureExecutionSample1();
        AccountingProcedureExecution accountingProcedureExecution2 = new AccountingProcedureExecution();
        assertThat(accountingProcedureExecution1).isNotEqualTo(accountingProcedureExecution2);

        accountingProcedureExecution2.setId(accountingProcedureExecution1.getId());
        assertThat(accountingProcedureExecution1).isEqualTo(accountingProcedureExecution2);

        accountingProcedureExecution2 = getAccountingProcedureExecutionSample2();
        assertThat(accountingProcedureExecution1).isNotEqualTo(accountingProcedureExecution2);
    }

    @Test
    void internalUserTest() {
        AccountingProcedureExecution accountingProcedureExecution = getAccountingProcedureExecutionRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        accountingProcedureExecution.setInternalUser(applicationUserBack);
        assertThat(accountingProcedureExecution.getInternalUser()).isEqualTo(applicationUserBack);

        accountingProcedureExecution.internalUser(null);
        assertThat(accountingProcedureExecution.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        AccountingProcedureExecution accountingProcedureExecution = getAccountingProcedureExecutionRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        accountingProcedureExecution.setCompany(companyBack);
        assertThat(accountingProcedureExecution.getCompany()).isEqualTo(companyBack);

        accountingProcedureExecution.company(null);
        assertThat(accountingProcedureExecution.getCompany()).isNull();
    }

    @Test
    void procedureTest() {
        AccountingProcedureExecution accountingProcedureExecution = getAccountingProcedureExecutionRandomSampleGenerator();
        AccountingProcedure accountingProcedureBack = getAccountingProcedureRandomSampleGenerator();

        accountingProcedureExecution.setProcedure(accountingProcedureBack);
        assertThat(accountingProcedureExecution.getProcedure()).isEqualTo(accountingProcedureBack);

        accountingProcedureExecution.procedure(null);
        assertThat(accountingProcedureExecution.getProcedure()).isNull();
    }
}
