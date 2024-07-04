package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HrLetterParameterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HrLetterParameter getHrLetterParameterSample1() {
        return new HrLetterParameter().id(1L).title("title1").formula("formula1");
    }

    public static HrLetterParameter getHrLetterParameterSample2() {
        return new HrLetterParameter().id(2L).title("title2").formula("formula2");
    }

    public static HrLetterParameter getHrLetterParameterRandomSampleGenerator() {
        return new HrLetterParameter()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .formula(UUID.randomUUID().toString());
    }
}
