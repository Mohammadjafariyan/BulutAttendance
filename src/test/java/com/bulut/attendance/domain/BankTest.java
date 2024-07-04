package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.BankTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bank.class);
        Bank bank1 = getBankSample1();
        Bank bank2 = new Bank();
        assertThat(bank1).isNotEqualTo(bank2);

        bank2.setId(bank1.getId());
        assertThat(bank1).isEqualTo(bank2);

        bank2 = getBankSample2();
        assertThat(bank1).isNotEqualTo(bank2);
    }

    @Test
    void statusTest() {
        Bank bank = getBankRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        bank.setStatus(recordStatusBack);
        assertThat(bank.getStatus()).isEqualTo(recordStatusBack);

        bank.status(null);
        assertThat(bank.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        Bank bank = getBankRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        bank.setInternalUser(applicationUserBack);
        assertThat(bank.getInternalUser()).isEqualTo(applicationUserBack);

        bank.internalUser(null);
        assertThat(bank.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        Bank bank = getBankRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        bank.setCompany(companyBack);
        assertThat(bank.getCompany()).isEqualTo(companyBack);

        bank.company(null);
        assertThat(bank.getCompany()).isNull();
    }
}
