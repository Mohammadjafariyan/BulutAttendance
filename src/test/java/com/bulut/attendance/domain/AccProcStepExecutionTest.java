package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccProcStepExecutionTestSamples.*;
import static com.bulut.attendance.domain.AccProcStepTestSamples.*;
import static com.bulut.attendance.domain.AccountHesabTestSamples.*;
import static com.bulut.attendance.domain.AccountingProcedureExecutionTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccProcStepExecutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccProcStepExecution.class);
        AccProcStepExecution accProcStepExecution1 = getAccProcStepExecutionSample1();
        AccProcStepExecution accProcStepExecution2 = new AccProcStepExecution();
        assertThat(accProcStepExecution1).isNotEqualTo(accProcStepExecution2);

        accProcStepExecution2.setId(accProcStepExecution1.getId());
        assertThat(accProcStepExecution1).isEqualTo(accProcStepExecution2);

        accProcStepExecution2 = getAccProcStepExecutionSample2();
        assertThat(accProcStepExecution1).isNotEqualTo(accProcStepExecution2);
    }

    @Test
    void internalUserTest() {
        AccProcStepExecution accProcStepExecution = getAccProcStepExecutionRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        accProcStepExecution.setInternalUser(applicationUserBack);
        assertThat(accProcStepExecution.getInternalUser()).isEqualTo(applicationUserBack);

        accProcStepExecution.internalUser(null);
        assertThat(accProcStepExecution.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        AccProcStepExecution accProcStepExecution = getAccProcStepExecutionRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        accProcStepExecution.setCompany(companyBack);
        assertThat(accProcStepExecution.getCompany()).isEqualTo(companyBack);

        accProcStepExecution.company(null);
        assertThat(accProcStepExecution.getCompany()).isNull();
    }

    @Test
    void creditAccountTest() {
        AccProcStepExecution accProcStepExecution = getAccProcStepExecutionRandomSampleGenerator();
        AccountHesab accountHesabBack = getAccountHesabRandomSampleGenerator();

        accProcStepExecution.setCreditAccount(accountHesabBack);
        assertThat(accProcStepExecution.getCreditAccount()).isEqualTo(accountHesabBack);

        accProcStepExecution.creditAccount(null);
        assertThat(accProcStepExecution.getCreditAccount()).isNull();
    }

    @Test
    void debitAccountTest() {
        AccProcStepExecution accProcStepExecution = getAccProcStepExecutionRandomSampleGenerator();
        AccountHesab accountHesabBack = getAccountHesabRandomSampleGenerator();

        accProcStepExecution.setDebitAccount(accountHesabBack);
        assertThat(accProcStepExecution.getDebitAccount()).isEqualTo(accountHesabBack);

        accProcStepExecution.debitAccount(null);
        assertThat(accProcStepExecution.getDebitAccount()).isNull();
    }

    @Test
    void procedureTest() {
        AccProcStepExecution accProcStepExecution = getAccProcStepExecutionRandomSampleGenerator();
        AccountingProcedureExecution accountingProcedureExecutionBack = getAccountingProcedureExecutionRandomSampleGenerator();

        accProcStepExecution.setProcedure(accountingProcedureExecutionBack);
        assertThat(accProcStepExecution.getProcedure()).isEqualTo(accountingProcedureExecutionBack);

        accProcStepExecution.procedure(null);
        assertThat(accProcStepExecution.getProcedure()).isNull();
    }

    @Test
    void stepTest() {
        AccProcStepExecution accProcStepExecution = getAccProcStepExecutionRandomSampleGenerator();
        AccProcStep accProcStepBack = getAccProcStepRandomSampleGenerator();

        accProcStepExecution.setStep(accProcStepBack);
        assertThat(accProcStepExecution.getStep()).isEqualTo(accProcStepBack);

        accProcStepExecution.step(null);
        assertThat(accProcStepExecution.getStep()).isNull();
    }
}
