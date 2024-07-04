package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ParameterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Parameter getParameterSample1() {
        return new Parameter().id(1L).title("title1").formula("formula1");
    }

    public static Parameter getParameterSample2() {
        return new Parameter().id(2L).title("title2").formula("formula2");
    }

    public static Parameter getParameterRandomSampleGenerator() {
        return new Parameter().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).formula(UUID.randomUUID().toString());
    }
}
