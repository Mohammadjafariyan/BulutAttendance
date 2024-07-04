package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccProcStepTestSamples.*;
import static com.bulut.attendance.domain.AccProccParameterTestSamples.*;
import static com.bulut.attendance.domain.AccountHesabTestSamples.*;
import static com.bulut.attendance.domain.AccountingProcedureTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccProcStepTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccProcStep.class);
        AccProcStep accProcStep1 = getAccProcStepSample1();
        AccProcStep accProcStep2 = new AccProcStep();
        assertThat(accProcStep1).isNotEqualTo(accProcStep2);

        accProcStep2.setId(accProcStep1.getId());
        assertThat(accProcStep1).isEqualTo(accProcStep2);

        accProcStep2 = getAccProcStepSample2();
        assertThat(accProcStep1).isNotEqualTo(accProcStep2);
    }

    @Test
    void internalUserTest() {
        AccProcStep accProcStep = getAccProcStepRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        accProcStep.setInternalUser(applicationUserBack);
        assertThat(accProcStep.getInternalUser()).isEqualTo(applicationUserBack);

        accProcStep.internalUser(null);
        assertThat(accProcStep.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        AccProcStep accProcStep = getAccProcStepRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        accProcStep.setCompany(companyBack);
        assertThat(accProcStep.getCompany()).isEqualTo(companyBack);

        accProcStep.company(null);
        assertThat(accProcStep.getCompany()).isNull();
    }

    @Test
    void creditAccountTest() {
        AccProcStep accProcStep = getAccProcStepRandomSampleGenerator();
        AccountHesab accountHesabBack = getAccountHesabRandomSampleGenerator();

        accProcStep.setCreditAccount(accountHesabBack);
        assertThat(accProcStep.getCreditAccount()).isEqualTo(accountHesabBack);

        accProcStep.creditAccount(null);
        assertThat(accProcStep.getCreditAccount()).isNull();
    }

    @Test
    void debitAccountTest() {
        AccProcStep accProcStep = getAccProcStepRandomSampleGenerator();
        AccountHesab accountHesabBack = getAccountHesabRandomSampleGenerator();

        accProcStep.setDebitAccount(accountHesabBack);
        assertThat(accProcStep.getDebitAccount()).isEqualTo(accountHesabBack);

        accProcStep.debitAccount(null);
        assertThat(accProcStep.getDebitAccount()).isNull();
    }

    @Test
    void parameterTest() {
        AccProcStep accProcStep = getAccProcStepRandomSampleGenerator();
        AccProccParameter accProccParameterBack = getAccProccParameterRandomSampleGenerator();

        accProcStep.setParameter(accProccParameterBack);
        assertThat(accProcStep.getParameter()).isEqualTo(accProccParameterBack);

        accProcStep.parameter(null);
        assertThat(accProcStep.getParameter()).isNull();
    }

    @Test
    void procedureTest() {
        AccProcStep accProcStep = getAccProcStepRandomSampleGenerator();
        AccountingProcedure accountingProcedureBack = getAccountingProcedureRandomSampleGenerator();

        accProcStep.setProcedure(accountingProcedureBack);
        assertThat(accProcStep.getProcedure()).isEqualTo(accountingProcedureBack);

        accProcStep.procedure(null);
        assertThat(accProcStep.getProcedure()).isNull();
    }
}
