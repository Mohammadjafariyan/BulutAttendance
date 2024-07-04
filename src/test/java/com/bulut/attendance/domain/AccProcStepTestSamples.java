package com.bulut.attendance.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AccProcStepTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccProcStep getAccProcStepSample1() {
        return new AccProcStep().id(1L);
    }

    public static AccProcStep getAccProcStepSample2() {
        return new AccProcStep().id(2L);
    }

    public static AccProcStep getAccProcStepRandomSampleGenerator() {
        return new AccProcStep().id(longCount.incrementAndGet());
    }
}
