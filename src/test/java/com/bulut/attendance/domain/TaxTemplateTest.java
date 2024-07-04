package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static com.bulut.attendance.domain.TaxTemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxTemplate.class);
        TaxTemplate taxTemplate1 = getTaxTemplateSample1();
        TaxTemplate taxTemplate2 = new TaxTemplate();
        assertThat(taxTemplate1).isNotEqualTo(taxTemplate2);

        taxTemplate2.setId(taxTemplate1.getId());
        assertThat(taxTemplate1).isEqualTo(taxTemplate2);

        taxTemplate2 = getTaxTemplateSample2();
        assertThat(taxTemplate1).isNotEqualTo(taxTemplate2);
    }

    @Test
    void statusTest() {
        TaxTemplate taxTemplate = getTaxTemplateRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        taxTemplate.setStatus(recordStatusBack);
        assertThat(taxTemplate.getStatus()).isEqualTo(recordStatusBack);

        taxTemplate.status(null);
        assertThat(taxTemplate.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        TaxTemplate taxTemplate = getTaxTemplateRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        taxTemplate.setInternalUser(applicationUserBack);
        assertThat(taxTemplate.getInternalUser()).isEqualTo(applicationUserBack);

        taxTemplate.internalUser(null);
        assertThat(taxTemplate.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        TaxTemplate taxTemplate = getTaxTemplateRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        taxTemplate.setCompany(companyBack);
        assertThat(taxTemplate.getCompany()).isEqualTo(companyBack);

        taxTemplate.company(null);
        assertThat(taxTemplate.getCompany()).isNull();
    }
}
