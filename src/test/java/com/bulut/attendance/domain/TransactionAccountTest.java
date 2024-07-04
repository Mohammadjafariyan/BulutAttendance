package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AccountHesabTestSamples.*;
import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.TransactionAccountTestSamples.*;
import static com.bulut.attendance.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionAccount.class);
        TransactionAccount transactionAccount1 = getTransactionAccountSample1();
        TransactionAccount transactionAccount2 = new TransactionAccount();
        assertThat(transactionAccount1).isNotEqualTo(transactionAccount2);

        transactionAccount2.setId(transactionAccount1.getId());
        assertThat(transactionAccount1).isEqualTo(transactionAccount2);

        transactionAccount2 = getTransactionAccountSample2();
        assertThat(transactionAccount1).isNotEqualTo(transactionAccount2);
    }

    @Test
    void internalUserTest() {
        TransactionAccount transactionAccount = getTransactionAccountRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        transactionAccount.setInternalUser(applicationUserBack);
        assertThat(transactionAccount.getInternalUser()).isEqualTo(applicationUserBack);

        transactionAccount.internalUser(null);
        assertThat(transactionAccount.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        TransactionAccount transactionAccount = getTransactionAccountRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        transactionAccount.setCompany(companyBack);
        assertThat(transactionAccount.getCompany()).isEqualTo(companyBack);

        transactionAccount.company(null);
        assertThat(transactionAccount.getCompany()).isNull();
    }

    @Test
    void accountTest() {
        TransactionAccount transactionAccount = getTransactionAccountRandomSampleGenerator();
        AccountHesab accountHesabBack = getAccountHesabRandomSampleGenerator();

        transactionAccount.setAccount(accountHesabBack);
        assertThat(transactionAccount.getAccount()).isEqualTo(accountHesabBack);

        transactionAccount.account(null);
        assertThat(transactionAccount.getAccount()).isNull();
    }

    @Test
    void transactionTest() {
        TransactionAccount transactionAccount = getTransactionAccountRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        transactionAccount.setTransaction(transactionBack);
        assertThat(transactionAccount.getTransaction()).isEqualTo(transactionBack);

        transactionAccount.transaction(null);
        assertThat(transactionAccount.getTransaction()).isNull();
    }
}
