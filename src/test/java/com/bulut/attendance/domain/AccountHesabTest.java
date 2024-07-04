package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccountHesabTestSamples.*;
import static com.bulut.attendance.domain.AccountHesabTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.BankTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.PersonnelTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountHesabTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountHesab.class);
        AccountHesab accountHesab1 = getAccountHesabSample1();
        AccountHesab accountHesab2 = new AccountHesab();
        assertThat(accountHesab1).isNotEqualTo(accountHesab2);

        accountHesab2.setId(accountHesab1.getId());
        assertThat(accountHesab1).isEqualTo(accountHesab2);

        accountHesab2 = getAccountHesabSample2();
        assertThat(accountHesab1).isNotEqualTo(accountHesab2);
    }

    @Test
    void statusTest() {
        AccountHesab accountHesab = getAccountHesabRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        accountHesab.setStatus(recordStatusBack);
        assertThat(accountHesab.getStatus()).isEqualTo(recordStatusBack);

        accountHesab.status(null);
        assertThat(accountHesab.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        AccountHesab accountHesab = getAccountHesabRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        accountHesab.setInternalUser(applicationUserBack);
        assertThat(accountHesab.getInternalUser()).isEqualTo(applicationUserBack);

        accountHesab.internalUser(null);
        assertThat(accountHesab.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        AccountHesab accountHesab = getAccountHesabRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        accountHesab.setCompany(companyBack);
        assertThat(accountHesab.getCompany()).isEqualTo(companyBack);

        accountHesab.company(null);
        assertThat(accountHesab.getCompany()).isNull();
    }

    @Test
    void parentAccountIdTest() {
        AccountHesab accountHesab = getAccountHesabRandomSampleGenerator();
        AccountHesab accountHesabBack = getAccountHesabRandomSampleGenerator();

        accountHesab.setParentAccountId(accountHesabBack);
        assertThat(accountHesab.getParentAccountId()).isEqualTo(accountHesabBack);

        accountHesab.parentAccountId(null);
        assertThat(accountHesab.getParentAccountId()).isNull();
    }

    @Test
    void personnelIdTest() {
        AccountHesab accountHesab = getAccountHesabRandomSampleGenerator();
        Personnel personnelBack = getPersonnelRandomSampleGenerator();

        accountHesab.setPersonnelId(personnelBack);
        assertThat(accountHesab.getPersonnelId()).isEqualTo(personnelBack);

        accountHesab.personnelId(null);
        assertThat(accountHesab.getPersonnelId()).isNull();
    }

    @Test
    void bankTest() {
        AccountHesab accountHesab = getAccountHesabRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        accountHesab.setBank(bankBack);
        assertThat(accountHesab.getBank()).isEqualTo(bankBack);

        accountHesab.bank(null);
        assertThat(accountHesab.getBank()).isNull();
    }
}
