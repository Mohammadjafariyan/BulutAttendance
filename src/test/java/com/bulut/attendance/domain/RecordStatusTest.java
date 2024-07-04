package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.RecordStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bulut.attendance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordStatus.class);
        RecordStatus recordStatus1 = getRecordStatusSample1();
        RecordStatus recordStatus2 = new RecordStatus();
        assertThat(recordStatus1).isNotEqualTo(recordStatus2);

        recordStatus2.setId(recordStatus1.getId());
        assertThat(recordStatus1).isEqualTo(recordStatus2);

        recordStatus2 = getRecordStatusSample2();
        assertThat(recordStatus1).isNotEqualTo(recordStatus2);
    }
}
