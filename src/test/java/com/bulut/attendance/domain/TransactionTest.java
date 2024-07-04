package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void internalUserTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        transaction.setInternalUser(applicationUserBack);
        assertThat(transaction.getInternalUser()).isEqualTo(applicationUserBack);

        transaction.internalUser(null);
        assertThat(transaction.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        transaction.setCompany(companyBack);
        assertThat(transaction.getCompany()).isEqualTo(companyBack);

        transaction.company(null);
        assertThat(transaction.getCompany()).isNull();
    }
}
