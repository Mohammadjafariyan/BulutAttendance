package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.ApplicationUserTestSamples.*;
import static com.bulut.attendance.domain.CompanyTestSamples.*;
import static com.bulut.attendance.domain.HrLetterTypeTestSamples.*;
import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HrLetterTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HrLetterType.class);
        HrLetterType hrLetterType1 = getHrLetterTypeSample1();
        HrLetterType hrLetterType2 = new HrLetterType();
        assertThat(hrLetterType1).isNotEqualTo(hrLetterType2);

        hrLetterType2.setId(hrLetterType1.getId());
        assertThat(hrLetterType1).isEqualTo(hrLetterType2);

        hrLetterType2 = getHrLetterTypeSample2();
        assertThat(hrLetterType1).isNotEqualTo(hrLetterType2);
    }

    @Test
    void statusTest() {
        HrLetterType hrLetterType = getHrLetterTypeRandomSampleGenerator();
        RecordStatus recordStatusBack = getRecordStatusRandomSampleGenerator();

        hrLetterType.setStatus(recordStatusBack);
        assertThat(hrLetterType.getStatus()).isEqualTo(recordStatusBack);

        hrLetterType.status(null);
        assertThat(hrLetterType.getStatus()).isNull();
    }

    @Test
    void internalUserTest() {
        HrLetterType hrLetterType = getHrLetterTypeRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        hrLetterType.setInternalUser(applicationUserBack);
        assertThat(hrLetterType.getInternalUser()).isEqualTo(applicationUserBack);

        hrLetterType.internalUser(null);
        assertThat(hrLetterType.getInternalUser()).isNull();
    }

    @Test
    void companyTest() {
        HrLetterType hrLetterType = getHrLetterTypeRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        hrLetterType.setCompany(companyBack);
        assertThat(hrLetterType.getCompany()).isEqualTo(companyBack);

        hrLetterType.company(null);
        assertThat(hrLetterType.getCompany()).isNull();
    }
}
