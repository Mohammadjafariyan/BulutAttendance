package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccProccParameterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccProccParameter getAccProccParameterSample1() {
        return new AccProccParameter().id(1L).title("title1").formula("formula1");
    }

    public static AccProccParameter getAccProccParameterSample2() {
        return new AccProccParameter().id(2L).title("title2").formula("formula2");
    }

    public static AccProccParameter getAccProccParameterRandomSampleGenerator() {
        return new AccProccParameter()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .formula(UUID.randomUUID().toString());
    }
}
